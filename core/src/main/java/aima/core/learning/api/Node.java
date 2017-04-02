package aima.core.learning.api;

import aima.core.learning.DecisionNode;
import aima.core.learning.LeafNode;
import aima.core.learning.NullNode;
import java.util.List;

/**
 * Defines the requirements for an object that can be used as a node in a Decision Tree
 *
 * @author shantanusinghal
 */
public interface Node {

  /**
   * static final instance with well defined neutral ("null") behavior
   */
  Node NULL = new NullNode();

  /**
   * Return the target value of the input example represented by the path from the root to the leaf,
   * or {@code null} if the decision tree doesn't contain a valid path.
   *
   * @param example the input example
   * @return the predicted class or value
   * @throws UnsupportedOperationException if called on a {@link NullNode}
   */
  Value process(Example example);

  /**
   * @return the attribute that this node predicates on
   * @throws UnsupportedOperationException if called on an {@link LeafNode} or {@link NullNode}
   */
  Attribute getAttribute();

  /**
   * @return the value at the node
   * @throws UnsupportedOperationException if called on an {@link DecisionNode} or {@link NullNode}
   */
  Value getValue();

  /**
   * @return the child node predicated on the input value
   *
   * @param value the predicate value
   * @throws UnsupportedOperationException if called on an {@link LeafNode} or {@link NullNode}
   */
  Node getChild(Value value);

  /**
   * @return a list of child nodes
   * @throws UnsupportedOperationException if called on an {@link LeafNode} or {@link NullNode}
   */
  List<Node> getChildren();

  /**
   * Add a branch to tree with a value that the node attribute predicates on to reach this child
   * node
   *
   * @param value the predicate value
   * @param child the child {@code Node}
   * @throws UnsupportedOperationException if called on an {@link LeafNode} or {@link NullNode}
   */
  void addChild(Value value, Node child);

}
