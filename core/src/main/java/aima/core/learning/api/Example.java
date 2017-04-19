package aima.core.learning.api;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Defines the requirements for an object that can be used as a <b>data sample</b>.
 *
 * @author shantanusinghal
 */
public interface Example {

  /**
   * Get the value for feature attribute, if one exists
   *
   * @param attribute the input attribute
   * @return the corresponding value as an {@link Optional}
   */
  Optional<String> valueOf(Attribute attribute);

  /**
   * Get the value for class attribute, if one exists
   *
   * @return the class attribute label as an {@link Optional}
   */
  Optional<String> classValue();

  /**
   * Tests if the example's value for give attribute matches the predicate.
   *
   * @param attribute the attribute whose value is being tested.
   * @param predicate the test function to be applied on the attribute value.
   * @return True if the example's attribute value matches the predicate and False otherwise.
   */
  default boolean testAttribute(Attribute attribute, Predicate<String> predicate) {
    Optional<String> optional = valueOf(attribute);
    return optional.isPresent() ? predicate.test(optional.get()) : false;
  }

  Optional<Attribute> classAttribute();
}
