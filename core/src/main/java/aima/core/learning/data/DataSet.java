package aima.core.learning.data;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Encapsulates a set of examples that share the same schema defined by the class and feature
 * attributes that each examples includes.
 *
 * @author shantanusinghal
 */
public class DataSet {

  private final List<Example> examples;
  private final Attribute classAttribute;
  private final List<Attribute> featureAttributes;

  DataSet(DataSetBuilder spec) {
    this(spec, Collections.emptyList());
  }

  DataSet(DataSetBuilder spec, List<Example> examples) {
    this.examples = new ArrayList<>(examples);
    this.classAttribute = spec.getClassAttribute();
    this.featureAttributes = spec.getFeatureAttributes();
  }

  /**
   * Adds new examples to the dataset, if they all conform to the data-set specifications
   *
   * @param examples (varargs) zero or more examples
   * @return the updated {@code DataSet} instance with examples added
   */
  public DataSet addExamples(Example... examples) {
    if(!checkSchema(examples))
      throw new IllegalArgumentException("All examples should match the data-set schema");
    this.examples.addAll(Arrays.asList(examples));
    return this;
  }

  public List<Example> getExamples() {
    return examples;
  }

  public List<Attribute> getAttributes() {
    return featureAttributes;
  }

  private boolean checkSchema(Example[] examples) {
    return Arrays.stream(examples)
        .allMatch(e ->
            classAttribute.equals(e.classAttribute().orElse(null)) &&
                featureAttributes
                    .stream()
                    .allMatch(a -> e.valueOf(a).isPresent()));
  }

}
