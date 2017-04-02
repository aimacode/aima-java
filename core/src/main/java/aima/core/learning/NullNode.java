package aima.core.learning;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Node;
import aima.core.learning.api.Value;
import java.util.List;

/**
 * {@link Node} implementation with well defined neutral ("null") behavior
 *
 * @author shantanusinghal
 */
public class NullNode implements Node {

  @Override
  public Value process(Example example) {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public Attribute getAttribute() {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public Value getValue() {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public Node getChild(Value value) {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public List<Node> getChildren() {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public void addChild(Value value, Node child) {
    throw new UnsupportedOperationException("Empty Node doesn't support this operation");
  }

  @Override
  public String toString() {
    return "NullNode{}";
  }

}
