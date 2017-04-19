package aima.core.learning.data;

import aima.core.learning.api.Attribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for safely creating a {@link DataSet} instance
 *
 * @author shantanusinghal
 */
public class DataSetBuilder {

  private Attribute classAttribute = null;
  private List<Attribute> featureAttributes = new ArrayList<>();

  public DataSetBuilder withFeatureAttribute(Attribute attribute) {
    this.featureAttributes.add(attribute);
    return this;
  }

  public DataSetBuilder withClassAttribute(Attribute attribute) {
    this.classAttribute = attribute;
    return this;
  }

  public DataSet build() {
    if (classAttribute == null) {
      throw new IllegalStateException(
          "Define the class attribute before building data-set specifications");
    } else if (featureAttributes.isEmpty()) {
      throw new IllegalStateException(
          "Define one or more feature attributes before building data-set specifications");
    } else {
      return new DataSet(this);
    }
  }

  Attribute getClassAttribute() {
    return classAttribute;
  }

  List<Attribute> getFeatureAttributes() {
    return featureAttributes;
  }

}
