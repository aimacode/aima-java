package aima.core.learning;

import aima.core.learning.api.TreeNode;
import java.util.List;

/**
 * @author shantanusinghal
 */
public class LeafNode implements TreeNode {

  private String value;

  public LeafNode(String value) {
    this.value = value;
  }

  @Override
  public String process(Example example) {
    return value;
  }

  @Override
  public Attribute getAttribute() {
    throw new UnsupportedOperationException(
        "A terminating (end) node doesn't have an associated attribute");
  }

  @Override
  public List<TreeNode> getChildren() {
    throw new UnsupportedOperationException(
        "A terminating (end) node doesn't have any children");
  }

  @Override
  public void addChild(String value, TreeNode child) {
    throw new UnsupportedOperationException("Can not add a child node to a terminating (end) node");
  }

  @Override
  public String toString() {
    return "EndNode{" +
        "value='" + value + '\'' +
        '}';
  }

}
