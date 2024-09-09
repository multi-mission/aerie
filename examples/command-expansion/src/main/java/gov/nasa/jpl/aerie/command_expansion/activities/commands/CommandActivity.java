package gov.nasa.jpl.aerie.command_expansion.activities.commands;

import gov.nasa.jpl.aerie.command_expansion.Mission;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;

public interface CommandActivity {
    Duration DEFAULT_COMMAND_DURATION = Duration.SECOND;

  /**
     * Calls {@link gov.nasa.jpl.aerie.command_expansion.generated.ActivityActions#call}
     * for the concrete activity type implemented by this command.
     * <p>
     *     This can be implemented by copying the following code into the implementing class:
     *     <pre>
     public void callMe(Mission mission) {
         ActivityActions.call(mission, this);
     }
     *     </pre>
     *     The concrete type of <code>this</code> in that context will statically choose
     *     the correct overload of <code>call</code>.
     * </p>
     */
    void call(Mission mission);

    public record Comp_att(int seqId, int index){}

}
