package gov.nasa.jpl.aerie.command_expansion.sequencing;

import gov.nasa.jpl.aerie.merlin.framework.annotations.AutoValueMapper;

import java.time.Instant;
import java.util.Map;

@AutoValueMapper.Record
public record SubSequence(Map<Instant,Command> commands) {
}
