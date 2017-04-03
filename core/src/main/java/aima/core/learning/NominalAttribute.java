package aima.core.learning;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Value;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Attribute implementation that only supports nominal (discrete) values.
 *
 * @author shantanusinghal
 */
public class NominalAttribute implements Attribute {

  private final String name;
  private List<String> possibleValues;

  public NominalAttribute(String name, List<String> possibleValues) {
    this.name = name;
    this.possibleValues = possibleValues;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public boolean canHold(String value) {
    return possibleValues.contains(value);
  }

  @Override
  public Optional<Value> valueFor(String value) {
    return Optional.ofNullable(NominalValue.create(value));
  }

  @Override
  public Map<Value, List<Example>> partitionByValue(List<Example> examples) {
    return possibleValues
        .stream()
        .map(v -> valueFor(v).get())
        .collect(toMap(identity(), v -> examples.stream()
            .filter(e -> e.valueOf(this).orElse(Value.NULL).equals(v))
            .collect(toList())));
  }

  @Override
  public String toString() {
    return "Attribute{" +
        "name='" + name + '\'' +
        '}';
  }
}
