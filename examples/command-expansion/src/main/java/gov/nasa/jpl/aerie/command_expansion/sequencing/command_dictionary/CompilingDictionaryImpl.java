package gov.nasa.jpl.aerie.command_expansion.sequencing.command_dictionary;

import gov.nasa.jpl.aerie.command_expansion.activities.commands.*;
import gov.nasa.jpl.aerie.command_expansion.generated.ActivityTypes;
import gov.nasa.jpl.aerie.command_expansion.sequencing.Command;
import gov.nasa.jpl.aerie.merlin.protocol.types.InstantiationException;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompilingDictionaryImpl implements CompilingDictionary {

    @Override
    public CommandActivity interpret(Command command) {
      var valueMapper = ActivityTypes.directiveTypes.get(command.stem());
      var outputType = valueMapper.getInputAsOutput().getSchema();
      var argumentMap = IntStream.range(0, valueMapper.getInputType().getParameters().size()).boxed().collect(
          Collectors.toMap(i -> valueMapper.getInputType().getParameters().get(i).name(),
                           i -> SerializedValue.of(command.arguments().get(i)))); // might be able to use the parameter value schema?
      try{
        return (CommandActivity) valueMapper.getInputType().instantiate((Map<String, SerializedValue>) argumentMap);
      } catch(Exception e) {
        throw new RuntimeException("Stem %s is not a recognized command.".formatted(command.stem()));
      }
    }
}
