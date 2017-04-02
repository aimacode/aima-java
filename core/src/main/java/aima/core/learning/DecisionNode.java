package aima.core.learning;

import aima.core.learning.api.TreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shantanusinghal
 */
public class DecisionNode implements TreeNode {

  private Attribute attribute;
  private Map<String, TreeNode> children = new HashMap<>();

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
  public void addChild(String value, TreeNode child) {
    if (!attribute.possibleValues().contains(value)) {
      throw new AssertionError("Attribute must predicate on a valid value");
    } else {
      children.put(value, child);
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
  public String process(Example example) {
    String value = example.getValue(attribute);
    if (value == null)
      throw new IllegalArgumentException("Example schema doesn't fit the decision tree");
    return children.get(value).process(example);
  }

  @Override
  public Attribute getAttribute() {
    return attribute;
  }

  @Override
  public List<TreeNode> getChildren() {
    return new ArrayList<>(children.values());
  }

  @Override
  public String toString() {
    return "DecisionNode{" +
        "attribute=" + attribute +
        '}';
  }

}
