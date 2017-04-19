package aima.core.learning;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author shantanusinghal
 */
public class DecisionNode implements Node {

  private Attribute attribute;
  private Map<Predicate<String>, Node> children = new HashMap<>();

  public DecisionNode(Attribute attribute) {
    this.attribute = attribute;
  }

  /**
   * Add a branch to a child {@code Node} that is predicated on the attribute value as defined.
   *
   * @param predicate the predicate for testing attribute value
   * @param child the child {@code Node}
   */
  @Override
  public void addChild(Predicate<String> predicate, Node child) {
    children.put(predicate, child);
  }

  /**
   * Returns the value at the leaf-node reached by recursively performing attribute-value tests
   * required to follow the decision tree path corresponding to the example.
   *
   * @param example the input example
   * @return the predicted value
   */
  @Override
  public String process(Example example) {
    Optional<String> value = example.valueOf(attribute);

    if (!value.isPresent()) throw new IllegalArgumentException(
        "Example doesn't specify value for decision node attribute " + attribute);

    Optional<Node> child = getChild(value.get());

    if(!child.isPresent()) throw new NoSuchElementException(
        "Decision node doesn't contain a valid path of this example");

    return child.get().process(example);
  }

  @Override
  public Attribute getAttribute() {
    return attribute;
  }

  @Override
  public String getValue() {
    throw new UnsupportedOperationException(
        "A decision (interior) node doesn't have a fixed value assigned to it");
  }

  @Override
  public Optional<Node> getChild(String value) {
    return children.entrySet()
      .stream()
      .filter(e -> e.getKey().test(value))
      .map(Map.Entry::getValue)
      .collect(Collectors.reducing((a, b) -> null));
  }

  @Override
  public List<Node> getChildren() {
    return new ArrayList<>(children.values());
  }

  @Override
  public String toString() {
    return "DecisionNode{" +
        "attribute=" + attribute +
        '}';
  }

}
