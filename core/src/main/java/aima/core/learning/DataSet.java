package aima.core.learning;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Encapsulates a prepared data-set
 *
 * @author shantanusinghal
 */
public class DataSet {

  private final List<Example> examples;
  private final Attribute classAttribute;
  private final List<Attribute> featureAttributes;

  public DataSet(DataSetSpec spec) {
    this.examples = new ArrayList<>();
    this.featureAttributes = spec.getFeatureAttributes();
    this.classAttribute = spec.getClassAttribute();
  }

  public DataSet withExamples(Example... examples) {
    this.examples.addAll(Arrays.asList(examples));
    return this;
  }

  public List<Example> getExamples() {
    return examples;
  }

  public List<Attribute> getAttributes() {
    return featureAttributes;
  }

}
