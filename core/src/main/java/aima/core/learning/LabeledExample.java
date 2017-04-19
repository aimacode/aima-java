package aima.core.learning;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.data.DataSetBuilder;
import java.util.Map;
import java.util.Optional;

/**
 * An concrete {@link Example} implementation that always has a class label.
 *
 * @author shantanusinghal
 */
public class LabeledExample implements Example {

  private String classValue;
  private Attribute classAttribute;
  private Map<Attribute, String> featureAttributeValues;

  public LabeledExample(Attribute classAttribute, String classValue,
      Map<Attribute, String> featureAttributeValues) {
    this.classValue = classValue;
    this.classAttribute = classAttribute;
    this.featureAttributeValues = featureAttributeValues;
  }

  @Override
  public Optional<String> valueOf(Attribute attribute) {
    return Optional.ofNullable(featureAttributeValues.get(attribute));
  }

  @Override
  public Optional<String> classValue() {
    return Optional.ofNullable(classValue);
  }

  @Override
  public Optional<Attribute> classAttribute() {
    return Optional.ofNullable(classAttribute);
  }

  @Override
  public String toString() {
    return "Example{" +
        "classAttribute=" + classAttribute +
        ", value=" + classValue +
        '}';
  }
}
