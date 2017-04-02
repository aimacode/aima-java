package aima.core.learning;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author shantanusinghal
 */
public class DataSetSpecs {

  private Attribute classAttribute = null;
  private Map<String, Attribute> attributes = new HashMap<>();

  public DataSetSpecs(final String classAttribute, Map<String, List<String>> attributeAndValues) {
    if (!attributeAndValues.keySet().contains(classAttribute))
      throw new IllegalStateException(
          "Didn't find nominal values for the specified class attribute");
    attributeAndValues.forEach((k, v) -> this.attributes.put(k, new Attribute(k, v)));
    this.classAttribute = attributes.get(classAttribute);
  }

  public Attribute getClassAttribute() {
    return classAttribute;
  }

  public Attribute getAttribute(String name) {
    return attributes.get(name);
  }

  public List<Attribute> getAttributes() {
    return new ArrayList<>(attributes.values()
        .stream()
        .filter(a -> !a.equals(classAttribute))
        .collect(toList()));
  }

  public Map<Attribute, String> transform(Map<String, String> data) throws RuntimeException {
    if (!valid(data)) {
      throw new RuntimeException("Input doesn't conform to the specifications");
    } else {
      return data
          .entrySet()
          .stream()
          .collect(Collectors.toMap(e -> attributes.get(e.getKey()), Map.Entry::getValue));
    }
  }

  private boolean valid(Map<String, String> example) {
    return attributes
        .keySet()
        .containsAll(example.keySet())
        &&
        example
            .entrySet()
            .stream()
            .allMatch(
                es -> attributes.get(es.getKey()).possibleValues().contains(es.getValue()));
  }
}
