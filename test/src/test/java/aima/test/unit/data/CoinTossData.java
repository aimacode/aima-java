package aima.test.unit.data;

import aima.core.learning.DataSetSpec;
import aima.core.learning.DataSetSpec.Builder;
import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author shantanusinghal
 */
public class CoinTossData {

  /************************** Attributes as Enums **************************/

  public enum FingerCrossed {
    YES, NO;
    public static String label() { return "Fingers Crossed"; }
    public static String[] stringValues() {
      return Arrays.stream(values()).map(v -> v.name()).toArray(String[]::new);
    }
  }

  public enum Result {
    HEADS, TAILS;
    public static String label() { return "Result"; }
    public static String[] stringValues() {
      return Arrays.stream(values()).map(v -> v.name()).toArray(String[]::new);
    }
  }

  /************************** Static Utility Methods **************************/

  public static DataSetSpec specs() {
    return SPEC;
  }

  public static String classAttribute() {
    return DATA_CLASS_ATTR;
  }

  public static Attribute getAttribute(String name) {
    return specs().getAttribute(name);
  }

  public static Example createExample(final FingerCrossed fingerCrossed, final Result result) {
    return specs().buildExample(new HashMap<String, String>() {{
      put(FingerCrossed.label(), fingerCrossed.name());
      put(Result.label(), result.name());
    }});
  }

  /*********************** DataSet Configuration as Private Constants ***********************/

  private static final DataSetSpec SPEC = new Builder()
      .withNominalAttributeWithValues(FingerCrossed.label(), FingerCrossed.stringValues())
      .withNominalClassAttributeWithValues(Result.label(), Result.stringValues())
      .build();

  private static final String DATA_CLASS_ATTR = Result.label();

}
