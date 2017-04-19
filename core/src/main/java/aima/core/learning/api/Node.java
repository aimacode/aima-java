package aima.core.learning.api;

import aima.core.learning.DecisionNode;
import aima.core.learning.LeafNode;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Defines the requirements for an object that can be used as a node in a Decision Tree
 *
 * @author shantanusinghal
 */
public interface Node {

  /**
   * Return the class value of the input example found at a leaf node in it's subtree.
   *
   * @param example the input example
   * @return the predicted class or value
   */
  String process(Example example);

  /**
   * @return the attribute that this node predicates on
   * @throws UnsupportedOperationException if called on an {@link LeafNode}
   */
  Attribute getAttribute();

  /**
   * @return the value at the node
   * @throws UnsupportedOperationException if called on an {@link DecisionNode}
   */
  String getValue();

  /**
   * @param value the predicate value for the node attribute
   * @return the child node corresponding to the input value
   * @throws UnsupportedOperationException if called on an {@link LeafNode}
   */
  Optional<Node> getChild(String value);

  /**
   * @return a list of child nodes
   * @throws UnsupportedOperationException if called on an {@link LeafNode}
   */
  List<Node> getChildren();

  /**
   * Add a branch to tree along with a attribute value predicate that the node tests to follow the
   * path to this child node.
   *
   * @param predicate the predicate for testing attribute value
   * @param child the child {@code Node}
   * @throws UnsupportedOperationException if called on an {@link LeafNode}
   */
  void addChild(Predicate<String> predicate, Node child);

}
