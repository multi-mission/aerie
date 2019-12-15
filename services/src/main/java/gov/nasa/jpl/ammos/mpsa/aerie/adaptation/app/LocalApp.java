package gov.nasa.jpl.ammos.mpsa.aerie.adaptation.app;

import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.exceptions.NoSuchActivityTypeException;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.exceptions.NoSuchAdaptationException;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.exceptions.UnconstructableActivityInstanceException;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.exceptions.ValidationException;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.models.ActivityType;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.models.Adaptation;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.models.AdaptationJar;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.models.NewAdaptation;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.remotes.AdaptationRepository;
import gov.nasa.jpl.ammos.mpsa.aerie.adaptation.utilities.AdaptationLoader;
import gov.nasa.jpl.ammos.mpsa.aerie.merlinsdk.MerlinAdaptation;
import gov.nasa.jpl.ammos.mpsa.aerie.merlinsdk.activities.Activity;
import gov.nasa.jpl.ammos.mpsa.aerie.merlinsdk.activities.ActivityMapper;
import gov.nasa.jpl.ammos.mpsa.aerie.merlinsdk.activities.representation.ParameterSchema;
import gov.nasa.jpl.ammos.mpsa.aerie.merlinsdk.activities.representation.SerializedActivity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class LocalApp implements App {
    private final AdaptationRepository adaptationRepository;

    public LocalApp(final AdaptationRepository adaptationRepository) {
        this.adaptationRepository = adaptationRepository;
    }

    @Override
    public Map<String, AdaptationJar> getAdaptations() {
        return this.adaptationRepository.getAllAdaptations().collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    @Override
    public AdaptationJar getAdaptationById(String id) throws NoSuchAdaptationException {
        return this.adaptationRepository.getAdaptation(id);
    }

    @Override
    public String addAdaptation(NewAdaptation adaptation) throws ValidationException {
        final AdaptationJar adaptationJar = new AdaptationJar();
        adaptationJar.name = adaptation.name;
        adaptationJar.version = adaptation.version;
        adaptationJar.mission = adaptation.mission;
        adaptationJar.owner = adaptation.owner;
        adaptationJar.path = adaptation.path;

        validateAdaptation(adaptationJar);

        return this.adaptationRepository.createAdaptation(adaptationJar);
    }

    @Override
    public void removeAdaptation(String id) throws NoSuchAdaptationException {
        this.adaptationRepository.deleteAdaptation(id);
    }

    @Override
    public Map<String, ActivityType> getActivityTypes(String adaptationId)
        throws NoSuchAdaptationException, AdaptationLoader.AdaptationLoadException, Adaptation.AdaptationContractException
    {
        return loadAdaptation(adaptationId)
            .getActivityTypes();
    }

    @Override
    public ActivityType getActivityType(String adaptationId, String activityTypeId)
        throws NoSuchAdaptationException, AdaptationLoader.AdaptationLoadException, Adaptation.AdaptationContractException,
        NoSuchActivityTypeException
    {
        return loadAdaptation(adaptationId)
            .getActivityType(activityTypeId);
    }

    @Override
    public List<String> validateActivityParameters(final String adaptationId, final SerializedActivity activityParameters)
        throws NoSuchAdaptationException, AdaptationLoader.AdaptationLoadException, Adaptation.AdaptationContractException,
        NoSuchActivityTypeException, UnconstructableActivityInstanceException
    {
        final Activity<?> activity = loadAdaptation(adaptationId)
            .instantiateActivity(activityParameters);

        final List<String> failures = activity.validateParameters();
        if (failures == null) {
            // TODO: The top-level application layer is a poor place to put knowledge about the adaptation contract.
            //   Move this logic somewhere better.
            throw new Adaptation.AdaptationContractException(activity.getClass().getName() + ".validateParameters() returned null");
        }

        return failures;
    }

    private Adaptation loadAdaptation(final String adaptationId) throws NoSuchAdaptationException, AdaptationLoader.AdaptationLoadException, Adaptation.AdaptationContractException {
        final AdaptationJar adaptationJar = this.adaptationRepository.getAdaptation(adaptationId);
        final MerlinAdaptation<?> adaptation = AdaptationLoader.loadAdaptation(adaptationJar.path);
        return new Adaptation(adaptationId, adaptation);
    }

    private void validateAdaptation(final AdaptationJar adaptationJar) throws ValidationException {
        final List<String> validationErrors = new ArrayList<>();

        if (adaptationJar.name == null) validationErrors.add("name must be non-null");
        if (adaptationJar.version == null) validationErrors.add("version must be non-null");

        if (adaptationJar.path == null) {
            validationErrors.add("path must be non-null");
        } else {
            try {
                final MerlinAdaptation<?> adaptation = AdaptationLoader.loadAdaptation(adaptationJar.path);

                final ActivityMapper activityMapper = adaptation.getActivityMapper();
                if (activityMapper == null) throw new Adaptation.AdaptationContractException(adaptation.getClass().getCanonicalName() + ".getActivityMapper() returned null");

                final Map<String, Map<String, ParameterSchema>> activitySchemas = activityMapper.getActivitySchemas();
                if (activitySchemas == null) throw new Adaptation.AdaptationContractException(activityMapper.getClass().getCanonicalName() + ".getActivitySchemas() returned null");

                if (activitySchemas.size() < 1) validationErrors.add("No activities found. Must include at least one activity");
            } catch (final AdaptationLoader.AdaptationLoadException | Adaptation.AdaptationContractException ex) {
                validationErrors.add("Adaptation JAR does not meet contract: " + ex.getMessage());
            }
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException("invalid adaptation", validationErrors);
        }
    }
}
