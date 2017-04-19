package aima.core.learning;

import static java.util.stream.Collectors.toList;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import java.util.List;
import java.util.function.Predicate;

/**
 * {@link Attribute} implementation that only supports nominal (discrete) values.
 *
 * @author shantanusinghal
 */
public class NominalAttribute extends Attribute {

  private List<String> possibleValues;

  public NominalAttribute(String name, List<String> predicate) {
    super(name);
    this.possibleValues = predicate;
  }

  /**
   * @return an equality checking predicate for each possible value of the nominal attribute.
   */
  @Override
  protected List<Predicate<String>> initPredicates(List<Example> examples) {
    return possibleValues
        .stream()
        .map(v -> (Predicate<String>) s -> v.toLowerCase().equals(s.toLowerCase()))
        .collect(toList());
  }

  /**
   * @return True if the input {@code o} is a non-null, {@code NominalAttribute} instance, with the
   * same name and an identical list of possible values, False otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NominalAttribute)) {
      return false;
    }

    NominalAttribute that = (NominalAttribute) o;

    if (!name.equals(that.name)) {
      return false;
    }
    return possibleValues.equals(that.possibleValues);

  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + possibleValues.hashCode();
    return result;
  }


}
