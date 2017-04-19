package aima.core.learning;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Classifier;
import aima.core.learning.api.Example;
import aima.core.learning.api.Node;
import aima.core.learning.data.DataSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Decision Tree Classifier capable of performing multi-class classification on a dataset.
 *
 * <p>The tree is built using decision nodes (that perform tests on attributes) and leaf nodes(that
 * represents a classification)</p>
 *
 * @author shantanusinghal
 */
public class DecisionTree implements Classifier {

  private Node root;

  private DecisionTree(Node root) {
    this.root = root;
  }

  @Override
  public Map<Example, String> predict(DataSet dataSet) {
    return dataSet.getExamples()
        .stream()
        .collect(toMap(identity(), e -> getRoot().process(e)));
  }

  @Override
  public String predict(Example example) {
    return getRoot().process(example);
  }

  /**
   * Static Factory method for creating a decision tree with a {@link DecisionNode} at it's root.
   *
   * @param attribute the decision attribute for the root node
   * @return an initialized {@code DecisionTree}
   */
  public static DecisionTree withDecisionNode(Attribute attribute) {
    return new DecisionTree(new DecisionNode(attribute));
  }

  /**
   * Static Factory method for creating a decision tree with a terminating {@link LeafNode} at it's
   * root.
   *
   * @param value the classification value for the root node
   * @return an initialized {@code DecisionTree}
   */
  public static DecisionTree withLeafNode(String value) {
    return new DecisionTree(new LeafNode(value));
  }

  /**
   * @return the root node of the decision tree
   */
  public Node getRoot() {
    return root;
  }

  /**
   * Add a branch to the tree along with a predicate for an attribute-value-check
   *
   * @param predicate the predicate value
   * @param subtree the child subtree
   */
  public void addBranch(Predicate<String> predicate, DecisionTree subtree) {
    try {
      this.root.addChild(predicate, subtree.getRoot());
    } catch (UnsupportedOperationException e) {
      throw new AssertionError(
          "[DESIGN-NOTE] We shouldn't be adding branches to leaf nodes, please verify and make corrections.");
    }
  }

}
