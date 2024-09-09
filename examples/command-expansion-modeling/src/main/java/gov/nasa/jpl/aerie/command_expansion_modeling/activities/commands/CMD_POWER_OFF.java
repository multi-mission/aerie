package gov.nasa.jpl.aerie.command_expansion_modeling.activities.commands;

import gov.nasa.jpl.aerie.command_expansion_modeling.Mission;
import gov.nasa.jpl.aerie.command_expansion_modeling.generated.ActivityActions;
import gov.nasa.jpl.aerie.command_expansion_modeling.sequencing.Sequencing;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

@ActivityType("CMD_POWER_OFF")
public class CMD_POWER_OFF implements CommandActivity {
    @Export.Parameter
    public String device;

//    sequence Expansion(mission) {
//      var seq = new Sequence();
//
//      if (device == "Tr"){
//        seq.append(CMD_POWER_OFF)
//      } else{
//        seq.append(CMD_10MB_detailed)
//      }
//
//      ....
//      return sequence
//    }
    @ActivityType.EffectModel
    public Comp_att run(Mission mission) {
//        var expansion = Expansion(mission);
//        mission.loggerBigBou.emit(() -> String.format("bout to run expansion %s", expansion))
//        mission.sequencing.seqEng(Expansion(mission));
        // Effects handled by hooks instead of direct implementation
        delay(DEFAULT_COMMAND_DURATION);
        return new Comp_att(0,0);
    }

    @Override
    public void call(Mission mission) {
        ActivityActions.call(mission, this);
    }
}
