package aima.core.learning.api;

/**
 * Defines the requirements for an object that can be used as an {@link Attribute} value.
 *
 * Defined as abstract class to force overriding of equals and hashCode methods.
 *
 * @author shantanusinghal
 */
public abstract class Value {

  /**
   * static final instance with well defined neutral ("null") behavior
   */
  public static final Value NULL = new Value() {
    @Override
    public String toString() {
      return "MISSING-VALUE";
    }

    @Override
    public boolean equals(Object o) {
      return false;
    }

    @Override
    public int hashCode() {
      return 0;
    }
  };

  /**
   * Forces child class to override {@link Object#toString()}.
   */
  public abstract String toString();

  /**
   * Forces child class to override {@link Object#equals(Object)}.
   */
  public abstract boolean equals(final Object o);

  /**
   * Forces child class to override {@link Object#hashCode()}.
   */
  public abstract int hashCode();

}
