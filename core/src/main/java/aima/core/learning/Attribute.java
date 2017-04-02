package aima.core.learning;

import java.util.List;

/**
 * Encapsulates the name of the attribute and it's possible values.
 *
 * <p>This data-structure only supports nominal valued attributes.</p>
 *
 * @author shantanusinghal
 */
public class Attribute {

  private String name;
  private List<String> possibleValues;

  public Attribute(String name, List<String> possibleValues) {
    this.name = name;
    this.possibleValues = possibleValues;
  }

  public String name() {
    return name;
  }

  public List<String> possibleValues() {
    return possibleValues;
  }

  @Override
  public String toString() {
    return "Attribute{" +
        "name='" + name + '\'' +
        '}';
  }
}
