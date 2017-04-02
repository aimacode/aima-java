package aima.test.unit.data;

import aima.core.learning.DataSetSpecs;
import aima.core.learning.Example;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shantanusinghal
 */
public class SingleAttributeDataSet {

  public enum FirstAttribute {
    VALUE1, VALUE2, VALUE3;
    public static String label() {
      return "First";
    }
  }

  public enum ClassAttribute {
    True, False;
    public static String label() {
      return "Class";
    }
  }

  private static DataSetSpecs DATA_SPEC = new DataSetSpecs(getClassAttribute(),
    new HashMap<String, List<String>>() {{
      put(FirstAttribute.label(), valList(FirstAttribute.values()));
      put(ClassAttribute.label(), valList(ClassAttribute.values()));
    }});

  public static DataSetSpecs specs() {
    return DATA_SPEC;
  }

  public static String getClassAttribute() {
    return ClassAttribute.label();
  }

  public static Example createExample(final FirstAttribute firstAttribute, final ClassAttribute classAttribute) {
    return new Example(DATA_SPEC.getAttribute(getClassAttribute()),
      DATA_SPEC.transform(new HashMap<String, String>() {{
        put(FirstAttribute.label(), firstAttribute.name());
        put(ClassAttribute.label(), classAttribute.name());
      }}));
  }

  protected static List<String> valList(Enum[] values) {
    return Arrays.stream(values).map(v -> v.name()).collect(Collectors.toList());
  }

}
