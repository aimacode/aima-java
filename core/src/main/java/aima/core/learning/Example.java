package aima.core.learning;

import java.util.Map;

/**
 * Encapsulates the attributes and values of a data instance
 *
 * <p>Use {@link DataSetSpecs} to safely build an {@code Example} instances</p>
 *
 * @author shantanusinghal
 */
public class Example {

  private Attribute classAttribute;
  private Map<Attribute, String> values;

  public Example(Attribute classAttribute, Map<Attribute, String> values) {
    this.values = values;
    this.classAttribute = classAttribute;
  }

  public String getValue(Attribute attribute) {
    return values.get(attribute);
  }

  public String getClassValue() {
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
