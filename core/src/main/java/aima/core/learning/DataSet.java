package aima.core.learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Encapsulates a list of examples instances along with the specifications of attribute type and
 * values
 *
 * @author shantanusinghal
 */
public class DataSet {

  private List<Example> examples;
  private final DataSetSpecs specs;

  public DataSet(DataSetSpecs specs) {
    this.specs = specs;
    this.examples = new ArrayList<>();
  }

  public DataSet addExamples(Example... examples) {
    this.examples.addAll(Arrays.asList(examples));
    return this;
  }

  public List<Example> getExamples() {
    return examples;
  }

  public List<Attribute> getAttributes() {
    return specs.getAttributes();
  }
}
