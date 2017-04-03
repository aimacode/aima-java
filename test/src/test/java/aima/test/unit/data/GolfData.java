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
public class GolfData {

  /************************** Attributes as Enums **************************/

  public enum Humid {
    TRUE, FALSE;
    public static String label() { return "Outlook"; }
    public static String[] stringValues() {
      return Arrays.stream(values()).map(v -> v.name()).toArray(String[]::new);
    }
  }

  public enum Windy {
    TRUE, FALSE;
    public static String label() { return "Windy"; }
    public static String[] stringValues() {
      return Arrays.stream(values()).map(v -> v.name()).toArray(String[]::new);
    }
  }

  public enum Play {
    TRUE, FALSE;
    public static String label() { return "Play"; }
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

  public static Example createExample(final Humid humid, final Windy windy, final Play play) {
    return specs().buildExample(new HashMap<String, String>() {{
      put(Humid.label(), humid.name());
      put(Windy.label(), windy.name());
      put(Play.label(), play.name());
    }});
  }

  /*********************** DataSet Configuration as Private Constants ***********************/

  private static final DataSetSpec SPEC = new Builder()
      .withNominalAttributeWithValues(Humid.label(), Humid.stringValues())
      .withNominalAttributeWithValues(Windy.label(), Windy.stringValues())
      .withNominalClassAttributeWithValues(Play.label(), Play.stringValues())
      .build();

  private static final String DATA_CLASS_ATTR = Play.label();

}
