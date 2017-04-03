package aima.core.learning;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Node;
import aima.core.learning.api.Value;
import java.util.List;

/**
 * @author shantanusinghal
 */
public class LeafNode implements Node {

  private Value value;

  public LeafNode(Value value) {
    this.value = value;
  }

  @Override
  public Value process(Example example) {
    return value;
  }

  @Override
  public Attribute getAttribute() {
    throw new UnsupportedOperationException(
        "A terminating (end) node doesn't have an associated attribute");
  }

  @Override
  public Value getValue() {
    return value;
  }

  @Override
  public Node getChild(Value value) {
    throw new UnsupportedOperationException(
        "A terminating (end) node doesn't have any children");
  }

  @Override
  public List<Node> getChildren() {
    throw new UnsupportedOperationException(
        "A terminating (end) node doesn't have any children");
  }

  @Override
  public void addChild(Value value, Node child) {
    throw new UnsupportedOperationException("Can not add a child node to a terminating (end) node");
  }

  @Override
  public String toString() {
    return "EndNode{" +
        "value='" + value + '\'' +
        '}';
  }

}
