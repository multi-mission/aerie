package gov.nasa.jpl.aerie.command_expansion.activities;

import gov.nasa.jpl.aerie.command_expansion.Mission;
import gov.nasa.jpl.aerie.command_expansion.activities.commands.CMD_ECHO;
import gov.nasa.jpl.aerie.command_expansion.generated.ActivityActions;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import static gov.nasa.jpl.aerie.contrib.streamline.debugging.Logging.LOGGER;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

@ActivityType("Activity_B")
public class Activity_B {
  @Export.Parameter
  public String message;

  @ActivityType.EffectModel
  public void run(Mission mission) {
    LOGGER.info("CMD_ECHO: %s", message);
    delay(Duration.of(3, Duration.MINUTES));
    ActivityActions.call(mission, new CMD_ECHO());


  }
}
