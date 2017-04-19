package aima.core.learning;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Node;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author shantanusinghal
 */
public class LeafNode implements Node {

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
  public String getValue() {
    return value;
  }

  @Override
  public Optional<Node> getChild(String value) {
    throw new UnsupportedOperationException(
        "A terminating (end) node doesn't have any children");
  }

  @Override
  public List<Node> getChildren() {
    throw new UnsupportedOperationException(
        "A terminating (end) node doesn't have any children");
  }

  @Override
  public void addChild(Predicate<String> predicate, Node child) {
    throw new UnsupportedOperationException("Can not add a child node to a terminating (end) node");
  }

  @Override
  public String toString() {
    return "EndNode{" +
        "value='" + value + '\'' +
        '}';
  }

}
