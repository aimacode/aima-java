package aima.core.learning.api;

import java.util.Optional;

/**
 * Defines the requirements for an object that can be used as a <b>labeled data sample</b>.
 *
 * @author shantanusinghal
 */
public interface Example {

  /**
   * Get the value for feature attribute
   *
   * @param attribute the input attribute
   * @return the corresponding value as an {@link Optional}
   */
  Optional<Value> valueOf(Attribute attribute);

  /**
   * Get the value for class attribute
   *
   * @return the value of class attribute
   */
  Value getClassValue();

}
