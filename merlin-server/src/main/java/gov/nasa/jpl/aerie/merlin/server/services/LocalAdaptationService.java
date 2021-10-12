package gov.nasa.jpl.aerie.merlin.server.services;

import gov.nasa.jpl.aerie.merlin.driver.Adaptation;
import gov.nasa.jpl.aerie.merlin.driver.SerializedActivity;
import gov.nasa.jpl.aerie.merlin.driver.SimulationDriver;
import gov.nasa.jpl.aerie.merlin.driver.SimulationResults;
import gov.nasa.jpl.aerie.merlin.protocol.model.MerlinPlugin;
import gov.nasa.jpl.aerie.merlin.protocol.types.Parameter;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;
import gov.nasa.jpl.aerie.merlin.protocol.types.ValueSchema;
import gov.nasa.jpl.aerie.merlin.server.models.ActivityType;
import gov.nasa.jpl.aerie.merlin.server.models.AdaptationFacade;
import gov.nasa.jpl.aerie.merlin.server.models.AdaptationJar;
import gov.nasa.jpl.aerie.merlin.server.models.Constraint;
import gov.nasa.jpl.aerie.merlin.server.models.NewAdaptation;
import gov.nasa.jpl.aerie.merlin.server.remotes.AdaptationRepository;
import gov.nasa.jpl.aerie.merlin.server.utilities.AdaptationLoader;
import io.javalin.core.util.FileUtil;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implements the adaptation service {@link AdaptationService} interface on a set of local domain objects.
 *
 * May throw unchecked exceptions:
 * * {@link LocalAdaptationService.AdaptationLoadException}: When an adaptation cannot be loaded from the JAR provided by the
 * connected
 * adaptation repository.
 */
public final class LocalAdaptationService implements AdaptationService {
  private static final Logger log = Logger.getLogger(LocalAdaptationService.class.getName());

  private final Path missionModelDataPath;
  private final AdaptationRepository adaptationRepository;

  public LocalAdaptationService(
      final Path missionModelDataPath,
      final AdaptationRepository adaptationRepository
  ) {
    this.missionModelDataPath = missionModelDataPath;
    this.adaptationRepository = adaptationRepository;
  }

  @Override
  public Map<String, AdaptationJar> getAdaptations() {
    return this.adaptationRepository.getAllAdaptations();
  }

  @Override
  public AdaptationJar getAdaptationById(String id) throws NoSuchAdaptationException {
    try {
      return this.adaptationRepository.getAdaptation(id);
    } catch (AdaptationRepository.NoSuchAdaptationException ex) {
      throw new NoSuchAdaptationException(id, ex);
    }
  }

  @Override
  public String addAdaptation(NewAdaptation adaptation) throws AdaptationRejectedException {
    final Path path;
    try {
      path = Files.createTempFile("adaptation", ".jar");
    } catch (final IOException ex) {
      throw new Error(ex);
    }
    FileUtil.streamToFile(adaptation.jarSource, path.toString());

    final String adClassStr;
    try {
      adClassStr = getImplementingClassName(path, MerlinPlugin.class);
    } catch (final IOException ex) {
      throw new AdaptationRejectedException(ex);
    }

    try {
      final var adClass = new ClassParser(path.toString(), getClasspathRelativePath(adClassStr));
      final var javaAdClass = adClass.parse();

      if (!isClassCompatibleWithThisVM(javaAdClass)) {
        throw new AdaptationRejectedException(String.format(
            "Adaptation was compiled with an older Java version. Please compile with Java %d.",
            Runtime.version().feature()));
      }
    } catch (final IOException ex) {
      throw new AdaptationRejectedException(ex);
    }

    try {
      AdaptationLoader.loadAdaptationProvider(path, adaptation.name, adaptation.version);
    } catch (final AdaptationLoader.AdaptationLoadException ex) {
      throw new AdaptationRejectedException(ex);
    }

    final AdaptationJar adaptationJar = new AdaptationJar();
    adaptationJar.name = adaptation.name;
    adaptationJar.version = adaptation.version;
    adaptationJar.mission = adaptation.mission;
    adaptationJar.owner = adaptation.owner;
    adaptationJar.path = path;

    return this.adaptationRepository.createAdaptation(adaptationJar);
  }

  @Override
  public void removeAdaptation(String id) throws NoSuchAdaptationException {
    try {
      this.adaptationRepository.deleteAdaptation(id);
    } catch (final AdaptationRepository.NoSuchAdaptationException ex) {
      throw new NoSuchAdaptationException(id, ex);
    }
  }

  @Override
  public Map<String, Constraint> getConstraints(final String adaptationId) throws NoSuchAdaptationException {
    try {
      return this.adaptationRepository.getConstraints(adaptationId);
    } catch (final AdaptationRepository.NoSuchAdaptationException ex) {
      throw new NoSuchAdaptationException(adaptationId, ex);
    }
  }

  @Override
  public void replaceConstraints(final String adaptationId, final Map<String, Constraint> constraints)
  throws NoSuchAdaptationException
  {
    try {
      this.adaptationRepository.replaceAdaptationConstraints(adaptationId, constraints);
    } catch (final AdaptationRepository.NoSuchAdaptationException ex) {
      throw new NoSuchAdaptationException(adaptationId, ex);
    }
  }

  @Override
  public void deleteConstraint(final String adaptationId, final String constraintName)
  throws NoSuchAdaptationException
  {
    try {
      this.adaptationRepository.deleteAdaptationConstraint(adaptationId, constraintName);
    } catch (final AdaptationRepository.NoSuchAdaptationException ex) {
      throw new NoSuchAdaptationException(adaptationId, ex);
    }
  }

  @Override
  public Map<String, ValueSchema> getStatesSchemas(final String adaptationId)
  throws NoSuchAdaptationException, AdaptationLoadException
  {
    return loadConfiguredAdaptation(adaptationId).getStateSchemas();
  }

  /**
   * Get information about all activity types in the named adaptation.
   *
   * @param adaptationId The ID of the adaptation to load.
   * @return The set of all activity types in the named adaptation, indexed by name.
   * @throws NoSuchAdaptationException If no adaptation is known by the given ID.
   * @throws AdaptationLoadException If the adaptation cannot be loaded -- the JAR may be invalid, or the adaptation
   * it contains may not abide by the expected contract at load time.
   */
  @Override
  public Map<String, ActivityType> getActivityTypes(String adaptationId)
  throws NoSuchAdaptationException, AdaptationLoadException
  {
    return loadUnconfiguredAdaptation(adaptationId)
        .getActivityTypes();
  }

  /**
   * Get information about the named activity type in the named adaptation.
   *
   * @param adaptationId The ID of the adaptation to load.
   * @param activityTypeId The ID of the activity type to query in the named adaptation.
   * @return Information about the named activity type.
   * @throws NoSuchAdaptationException If no adaptation is known by the given ID.
   * @throws NoSuchActivityTypeException If no activity type exists for the given serialized activity.
   * @throws AdaptationLoadException If the adaptation cannot be loaded -- the JAR may be invalid, or the adaptation
   * it contains may not abide by the expected contract at load time.
   */
  @Override
  public ActivityType getActivityType(String adaptationId, String activityTypeId)
  throws NoSuchAdaptationException, NoSuchActivityTypeException, AdaptationLoadException
  {
    try {
      return loadUnconfiguredAdaptation(adaptationId).getActivityType(activityTypeId);
    } catch (final AdaptationFacade.NoSuchActivityTypeException ex) {
      throw new NoSuchActivityTypeException(activityTypeId, ex);
    }
  }

  /**
   * Validate that a set of activity parameters conforms to the expectations of a named adaptation.
   *
   * @param adaptationId The ID of the adaptation to load.
   * @param activityParameters The serialized activity to validate against the named adaptation.
   * @return A list of validation errors that is empty if validation succeeds.
   * @throws NoSuchAdaptationException If no adaptation is known by the given ID.
   * @throws AdaptationFacade.AdaptationContractException If the named adaptation does not abide by the expected contract.
   * @throws AdaptationLoadException If the adaptation cannot be loaded -- the JAR may be invalid, or the adaptation
   * it contains may not abide by the expected contract at load time.
   */
  @Override
  public List<String> validateActivityParameters(final String adaptationId, final SerializedActivity activityParameters)
  throws NoSuchAdaptationException, AdaptationFacade.AdaptationContractException, AdaptationLoadException
  {
    try {
      return this.loadConfiguredAdaptation(adaptationId)
                 .validateActivity(activityParameters.getTypeName(), activityParameters.getParameters());
    } catch (final AdaptationFacade.NoSuchActivityTypeException ex) {
      return List.of("unknown activity type");
    } catch (final AdaptationFacade.UnconstructableActivityInstanceException ex) {
      return List.of(ex.getMessage());
    }
  }

  @Override
  public List<Parameter> getModelParameters(final String adaptationId)
  throws NoSuchAdaptationException, AdaptationLoadException
  {
    return loadUnconfiguredAdaptation(adaptationId).getParameters();
  }

  /**
   * Validate that a set of activity parameters conforms to the expectations of a named adaptation.
   *
   * @param message The parameters defining the simulation to perform.
   * @return A set of samples over the course of the simulation.
   * @throws NoSuchAdaptationException If no adaptation is known by the given ID.
   */
  @Override
  public SimulationResults runSimulation(final CreateSimulationMessage message)
  throws NoSuchAdaptationException
  {
    final var config = message.configuration();
    if (config.isEmpty()) {
      log.warning(
          "No mission model configuration defined for adaptation. Simulations will receive an empty set of configuration arguments.");
    }

    return loadConfiguredAdaptation(message.adaptationId(), SerializedValue.of(config))
        .simulate(message.activityInstances(), message.samplingDuration(), message.startTime());
  }

  @Override
  public List<Path> getAvailableFilePaths() throws IOException {
    return Files
        .list(missionModelDataPath)
        .map(missionModelDataPath::relativize)
        .collect(Collectors.toList());
  }

  @Override
  public void createFile(final String filename, final InputStream content) throws IOException {
    final var path = missionModelDataPath.resolve(filename).toAbsolutePath().normalize();
    if (!path.startsWith(missionModelDataPath.toAbsolutePath())) { // Only allow sub-paths
      throw new FileNotFoundException(path.toString());
    }
    Files.copy(content, path, StandardCopyOption.REPLACE_EXISTING);
  }

  @Override
  public void deleteFile(final String filename) throws IOException {
    final var path = missionModelDataPath.resolve(filename).toAbsolutePath().normalize();
    if (!path.startsWith(missionModelDataPath.toAbsolutePath())) { // Only allow sub-paths
      throw new FileNotFoundException(path.toString());
    }
    Files.delete(path);
  }

  @Override
  public void updateDerivedData(final String adaptationId)
  throws NoSuchAdaptationException
  {
    try {
      this.adaptationRepository.updateAdaptationDerivedData(adaptationId, getModelParameters(adaptationId), getActivityTypes(adaptationId));
    } catch (final AdaptationRepository.NoSuchAdaptationException ex) {
      throw new NoSuchAdaptationException(adaptationId, ex);
    }
  }

  private static String getImplementingClassName(final Path jarPath, final Class<?> javaClass)
  throws IOException, AdaptationRejectedException {
    final var jarFile = new JarFile(jarPath.toFile());
    final var jarEntry = jarFile.getEntry("META-INF/services/" + javaClass.getCanonicalName());
    final var inputStream = jarFile.getInputStream(jarEntry);

    final var classPathList = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.toList());

    if (classPathList.size() != 1) {
      throw new AdaptationRejectedException(
          "Adaptation contains zero/multiple registered implementations of %s.".formatted(javaClass));
    }

    return classPathList.get(0);
  }

  private static String getClasspathRelativePath(final String className) {
    return className.replaceAll("\\.", "/").concat(".class");
  }

  private static boolean isClassCompatibleWithThisVM(final JavaClass javaClass) {
    // Refer to this link https://docs.oracle.com/javase/specs/jvms/se15/html/jvms-4.html
    return Runtime.version().feature() + 44 >= javaClass.getMajor();
  }

  private AdaptationFacade.Unconfigured<?> loadUnconfiguredAdaptation(final String adaptationId)
  throws NoSuchAdaptationException, AdaptationLoadException
  {
    try {
      final var adaptationJar = this.adaptationRepository.getAdaptation(adaptationId);
      final var adaptation =
          AdaptationLoader.loadAdaptationFactory(missionModelDataPath.resolve(adaptationJar.path), adaptationJar.name, adaptationJar.version);
      return new AdaptationFacade.Unconfigured<>(adaptation);
    } catch (final AdaptationRepository.NoSuchAdaptationException ex) {
      throw new NoSuchAdaptationException(adaptationId, ex);
    } catch (final AdaptationLoader.AdaptationLoadException ex) {
      throw new AdaptationLoadException(ex);
    }
  }

  /**
   * Load an {@link Adaptation} from the adaptation repository using the adaptation's default mission model configuration,
   * and wrap it in an {@link AdaptationFacade} domain object.
   *
   * @param adaptationId The ID of the adaptation in the adaptation repository to load.
   * @return An {@link AdaptationFacade} domain object allowing use of the loaded adaptation.
   * @throws AdaptationLoadException If the adaptation cannot be loaded -- the JAR may be invalid, or the adaptation
   * it contains may not abide by the expected contract at load time.
   * @throws NoSuchAdaptationException If no adaptation is known by the given ID.
   */
  private AdaptationFacade<?> loadConfiguredAdaptation(final String adaptationId)
  throws NoSuchAdaptationException, AdaptationLoadException
  {
    return loadConfiguredAdaptation(adaptationId, SerializedValue.of(Map.of()));
  }

  /**
   * Load an {@link Adaptation} from the adaptation repository, and wrap it in an {@link AdaptationFacade} domain object.
   *
   * @param adaptationId The ID of the adaptation in the adaptation repository to load.
   * @param configuration The mission model configuration to to load the adaptation with.
   * @return An {@link AdaptationFacade} domain object allowing use of the loaded adaptation.
   * @throws AdaptationLoadException If the adaptation cannot be loaded -- the JAR may be invalid, or the adaptation
   * it contains may not abide by the expected contract at load time.
   * @throws NoSuchAdaptationException If no adaptation is known by the given ID.
   */
  private AdaptationFacade<?> loadConfiguredAdaptation(final String adaptationId, final SerializedValue configuration)
  throws NoSuchAdaptationException, AdaptationLoadException
  {
    try {
      final var adaptationJar = this.adaptationRepository.getAdaptation(adaptationId);
      final var adaptation =
          AdaptationLoader.loadAdaptation(configuration, missionModelDataPath.resolve(adaptationJar.path), adaptationJar.name, adaptationJar.version);
      return new AdaptationFacade<>(adaptation);
    } catch (final AdaptationRepository.NoSuchAdaptationException ex) {
      throw new NoSuchAdaptationException(adaptationId, ex);
    } catch (final AdaptationLoader.AdaptationLoadException ex) {
      throw new AdaptationLoadException(ex);
    }
  }

  public static class AdaptationLoadException extends RuntimeException {
    public AdaptationLoadException(final Throwable cause) { super(cause); }
  }
}
