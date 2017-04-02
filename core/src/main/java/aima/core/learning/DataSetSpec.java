package aima.core.learning;

/**
 * Created by shantanusinghal on 03/04/17 @ 8:42 AM.
 * NET-ID: singhal5
 * Campus ID: 9076101956
 */

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Value;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Encapsulates the following specifications
 *
 * <ol>
 * <li>List of Attributes</li>
 * <li>The Class Attribute common to the list of Examples</li>
 * </ol>
 *
 * An instance of {@code DataSetSpec} is required by the {@link DataSet} constructor.
 *
 * @author shantanusinghal
 */
public class DataSetSpec {

  private final Attribute classAttribute;
  private final Map<String, Attribute> allAttributes;
  private final List<Attribute> featureAttributes;

  private DataSetSpec(Builder builder) {
    this.allAttributes = builder.attributes;
    this.classAttribute = builder.classAttribute;
    this.featureAttributes = allAttributes.values().stream().filter(a -> !a.equals(classAttribute)).collect(Collectors.toList());
  }

  /**
   * Factory method for creating an {@link Example} instance.
   *
   * @param values is a map of attribute names and their values
   * @return an instance of an {@code Example}
   */
  public Example buildExample(Map<String, String> values) {
    return getExample(classAttribute, allAttributes, values);
  }

  public Attribute getClassAttribute() {
    return classAttribute;
  }

  public List<Attribute> getFeatureAttributes() {
    return featureAttributes;
  }

  public Attribute getAttribute(String name) {
    return allAttributes.get(name);
  }

  private static Example getExample(Attribute classAttribute, Map<String, Attribute> attributes,
      Map<String, String> values) { Map<Attribute, Value> valueMap;

    if (attributes.size() < values.keySet().size()) {
      throw new IllegalStateException("Define all attributes before adding examples");

    } else if (!attributes.keySet().containsAll(values.keySet())) {
      throw new IllegalStateException(
          "Ensure that the examples contains a mapping for each attribute");

    } else if (classAttribute == null) {
      throw new IllegalStateException("Define the class attribute before adding examples");

    } else {
      try {
        valueMap = attributes
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Entry::getValue,
                e -> e.getValue().valueFor(values.get(e.getKey())).get()));
      } catch (NoSuchElementException e) {
        throw new IllegalStateException(
            "Ensure that the examples contains a maps each attribute to a valid value");
      }
      return new SimpleExample(classAttribute, valueMap);
    }
  }

  /**
   * Builder for safely creating a Data Set Specification instance
   */
  public static class Builder {

    private Attribute classAttribute = null;
    private Map<String, Attribute> attributes = new HashMap<>();

    public Builder withNominalAttributeWithValues(String name, String... values) {
      this.attributes.put(name, new NominalAttribute(name, Arrays.asList(values)));
      return this;
    }

    public Builder withNominalClassAttributeWithValues(String name, String... values) {
      this.attributes.put(name, this.classAttribute = new NominalAttribute(name, Arrays.asList(values)));
      return this;
    }

    public DataSetSpec build() {
      if (classAttribute == null) {
        throw new IllegalStateException(
            "Define the class attribute before completing the data-set specifications");
      } else if (attributes.isEmpty()) {
        throw new IllegalStateException(
            "Define one or more feature attributes before completing the data-set specifications");
      } else {
        return new DataSetSpec(this);
      }
    }
  }

}
