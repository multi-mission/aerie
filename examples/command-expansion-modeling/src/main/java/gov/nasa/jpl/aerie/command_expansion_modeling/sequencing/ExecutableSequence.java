package gov.nasa.jpl.aerie.command_expansion_modeling.sequencing;

import java.util.List;
import java.util.Optional;

public record ExecutableSequence(String id, List<ExecutableCommand> commands) {
    public Optional<ExecutableCommand> getCommand(int index) {
        if (0 <= index && index < commands.size()) {
            return Optional.of(commands.get(index));
        } else {
            return Optional.empty();
        }
    }
}
