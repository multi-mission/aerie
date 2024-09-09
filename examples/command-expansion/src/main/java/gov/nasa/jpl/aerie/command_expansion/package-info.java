@MissionModel(model = Mission.class)

@WithConfiguration(Configuration.class)

@WithMappers(BasicValueMappers.class)

@WithActivityType(AuthoredSequence.class)

@WithActivityType(CMD_ECHO.class)
@WithActivityType(CMD_NO_OP.class)
@WithActivityType(CMD_WAIT.class)
@WithActivityType(Activity_A.class)
@WithActivityType(Activity_B.class)
@WithActivityType(Activity_C.class)

package gov.nasa.jpl.aerie.command_expansion;

import gov.nasa.jpl.aerie.command_expansion.activities.Activity_A;
import gov.nasa.jpl.aerie.command_expansion.activities.Activity_B;
import gov.nasa.jpl.aerie.command_expansion.activities.Activity_C;
import gov.nasa.jpl.aerie.command_expansion.activities.AuthoredSequence;
import gov.nasa.jpl.aerie.command_expansion.activities.commands.*;
import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithConfiguration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithMappers;
