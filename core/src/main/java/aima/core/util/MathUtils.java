package aima.core.util;

/**
 * @author shantanusinghal
 */
public class MathUtils {

  /**
   * Calculates binary logarithm i.e the logarithm to the base 2 of a number.
   *
   * <p><b>NOTE:</b> {@code lg2(0) is defined as 0}</p>
   *
   * @param n the input number
   *
   * @return the log base 2
   */
  public static double lg2(double n) {
    return n == 0.0 ? 0.0 : (Math.log(n) / Math.log(2));
  }

  /**
   * @param str input value as {@code String}
   * @return True if the string represents a real number, False otherwise
   */
  public static boolean isNumber(String str) {
    return str.matches("[-+]?\\d*\\.?\\d+");
  }

}
