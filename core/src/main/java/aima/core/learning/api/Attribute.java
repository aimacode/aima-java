package aima.core.learning.api;

import java.util.List;
import java.util.function.Predicate;

/**
 * Defines the requirements for an object that can be used as an attribute in an {@link Example}.
 *
 * <p>Implemented as an Abstract Class to enforce it's implementations to override the {@link
 * Object#equals(Object)} and {@link Object#hashCode()} methods.</p>
 *
 * @author shantanusinghal
 */
public abstract class Attribute {

  protected final String name;
  protected List<Predicate<String>> predicates;

  protected Attribute(String name) {
    this.name = name;
  }

  /**
   * @return the name of attribute instance.
   */
  public String name() {
    return name;
  }

  /**
   * List of predicates, on the value of this attribute, that best split the given list of examples.
   *
   * @param examples input data to be split by this attribute.
   * @return a list of predicates for splitting given examples.
   */
  public List<Predicate<String>> getPredicates(List<Example> examples) {
    return predicates == null ? (predicates = initPredicates(examples)) : predicates;
  }

  @Override
  public String toString() {
    return "Attribute{" +
        "name='" + name + '\'' +
        '}';
  }

  abstract protected List<Predicate<String>> initPredicates(List<Example> examples);

  @Override
  abstract public int hashCode();

  @Override
  abstract public boolean equals(Object obj);

}
