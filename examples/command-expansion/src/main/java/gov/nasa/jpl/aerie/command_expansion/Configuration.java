package gov.nasa.jpl.aerie.command_expansion;

import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

public final class Configuration {
    @Export.Parameter
    public Duration defaultCommandDuration = Duration.SECOND;
}
