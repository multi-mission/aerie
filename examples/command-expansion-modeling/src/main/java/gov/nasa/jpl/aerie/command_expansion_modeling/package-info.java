@MissionModel(model = Mission.class)

@WithConfiguration(Configuration.class)

@WithMappers(BasicValueMappers.class)

@WithActivityType(AuthoredSequence.class)

@WithActivityType(CMD_ECHO.class)
@WithActivityType(CMD_NO_OP.class)
@WithActivityType(CMD_POWER_OFF.class)
@WithActivityType(CMD_POWER_ON.class)
@WithActivityType(CMD_WAIT.class)

package gov.nasa.jpl.aerie.command_expansion_modeling;

import gov.nasa.jpl.aerie.command_expansion_modeling.activities.AuthoredSequence;
import gov.nasa.jpl.aerie.command_expansion_modeling.activities.commands.*;
import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithConfiguration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithMappers;
