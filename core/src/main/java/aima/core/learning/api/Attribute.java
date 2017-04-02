package aima.core.learning.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Defines the requirements for an object that can be used as an attribute in an {@link Example}.
 *
 * @author shantanusinghal
 */
public interface Attribute {

  /**
   * @return the name of attribute instance.
   */
  String name();

  /**
   * Check if specified value is compatible with the attribute instance
   *
   * @param value the input value as {@code String}
   * @return True if it's a valid value and false otherwise.
   */
  boolean canHold(String value);

  /**
   * Check if specified value is compatible with the attribute instance
   *
   * @param value the input value as {@code Value}
   * @return True if it's a valid value and false otherwise.
   */
  default boolean canHold(Value value) {
    return canHold(value.toString());
  }

  /**
   * Returns a {@link Value} instance corresponding to the string value specified, if one exists.
   *
   * @param value the inptut as {@code String}.
   * @return a {@link Value} instance wrapped in an {@code Optional}
   */
  Optional<Value> valueFor(String value);

  /**
   * Split examples over the various attribute values.
   *
   * @param examples input data to be split by this attribute.
   * @return a map of values and their corresponding examples.
   */
  Map<Value, List<Example>> partitionByValue(List<Example> examples);

}
