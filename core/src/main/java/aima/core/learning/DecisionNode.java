package aima.core.learning;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Node;
import aima.core.learning.api.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author shantanusinghal
 */
public class DecisionNode implements Node {

  private Attribute attribute;
  private Map<Value, Node> children = new HashMap<>();

  public DecisionNode(Attribute attribute) {
    this.attribute = attribute;
  }

  /**
   * Add a branch to a child {@code Node} that is predicated on the attribute value
   *
   * @param value the predicate value for selecting the child {@code Node}
   * @param child the child {@code Node}
   * @throws AssertionError if the {@code value} doesn't match the {@code attribute}
   */
  @Override
  public void addChild(Value value, Node child) {
    if (attribute.canHold(value)) {
      children.put(value, child);
    } else {
      throw new AssertionError("Attribute must predicate on a valid value");
    }
  }

  /**
   * Returns the value at the leaf-node reached by recursively performing attribute-value tests
   * required to follow the decision tree path corresponding to the example.
   *
   * @param example the input example
   * @return the predicted value
   */
  @Override
  public Value process(Example example) {
    Optional<Value> value = example.valueOf(attribute);
    if (value.isPresent()) {
      return children.get(value.get()).process(example);
    } else {
      throw new IllegalArgumentException(
          "Node's decision attribute doesn't match the Example schema");
    }
  }

  @Override
  public Attribute getAttribute() {
    return attribute;
  }

  @Override
  public Value getValue() {
    throw new UnsupportedOperationException(
        "A decision (interior) node doesn't have a fixed value assigned to it");
  }

  @Override
  public Node getChild(Value value) {
    return children.get(value);
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
