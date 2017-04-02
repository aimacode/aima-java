package aima.core.learning;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Value;
import java.util.Map;
import java.util.Optional;

/**
 * Encapsulates the attributes and values of a data instance
 *
 * <p>Use {@link DataSetSpec#buildExample(Map)} to safely build an {@code Example} instances</p>
 *
 * @author shantanusinghal
 */
public class SimpleExample implements Example {

  private Attribute classAttribute;
  private Map<Attribute, Value> values;

  public SimpleExample(Attribute classAttribute, Map<Attribute, Value> values) {
    this.values = values;
    this.classAttribute = classAttribute;
  }

  @Override
  public Optional<Value> valueOf(Attribute attribute) {
    return Optional.ofNullable(values.get(attribute));
  }

  @Override
  public Value getClassValue() {
    return values.get(classAttribute);
  }

  @Override
  public String toString() {
    return "Example{" +
        "classAttribute=" + classAttribute +
        ", values=" + values +
        '}';
  }
}
