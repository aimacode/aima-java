package aima.core.learning;

import aima.core.learning.api.Value;

/**
 * {@link Value} implementation that supports discrete values only
 *
 * @author shantanusinghal
 */
public class NominalValue extends Value {

  public static final Value TRUE = new NominalValue("TRUE");
  public static final Value FALSE = new NominalValue("FALSE");

  private final String value;

  /**
   * Use {@link NominalValue#create(String)} to create new instances.
   */
  private NominalValue(String value) {
    super();
    this.value = value;
  }

  /**
   * Static factory method for creating new {@code Label} instances
   */
  public static Value create(String value) {
    return new NominalValue(value);
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NominalValue that = (NominalValue) o;
    return value.equals(that.value);
  }

}
