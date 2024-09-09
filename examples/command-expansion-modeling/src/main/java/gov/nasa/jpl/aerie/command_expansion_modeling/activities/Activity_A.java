package gov.nasa.jpl.aerie.command_expansion_modeling.activities;

import gov.nasa.jpl.aerie.command_expansion_modeling.Mission;
import gov.nasa.jpl.aerie.command_expansion_modeling.activities.commands.CMD_ECHO;
import gov.nasa.jpl.aerie.command_expansion_modeling.activities.commands.CommandActivity;
import gov.nasa.jpl.aerie.command_expansion_modeling.generated.ActivityActions;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

import static gov.nasa.jpl.aerie.contrib.streamline.debugging.Logging.LOGGER;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.call;
import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

@ActivityType("CMD_ECHO")
public class Activity_A {
  @Export.Parameter
  public String message;

  @ActivityType.EffectModel
  public void run(Mission mission) {
    LOGGER.info("CMD_ECHO: %s", message);
    delay(Duration.of(3, Duration.MINUTES));
    ActivityActions.call(mission, new CMD_ECHO());


  }
}

