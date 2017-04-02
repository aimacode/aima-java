package aima.core.learning;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import aima.core.learning.api.Classifier;
import aima.core.learning.api.TreeNode;
import java.util.Map;

/**
 * Decision Tree Classifier capable of performing multi-class classification on a dataset.
 *
 * <p>The tree is built using decision nodes (that perform tests on attributes) and leaf nodes(that
 * represents a classification)</p>
 *
 * @author shantanusinghal
 */
public class DecisionTree implements Classifier {

  private TreeNode root;

  DecisionTree(TreeNode root) {
    this.root = root;
  }

  /**
   * Factory method for creating a decision tree
   *
   * @param root the root {@code Node}
   * @return an initialized {@code DecisionTree}
   */
  public static DecisionTree withRoot(TreeNode root) {
    return new DecisionTree(root);
  }

  /**
   * Factory method for creating a decision tree with a terminating {@link LeafNode} at it's
   * root.
   *
   * @param value the value for root node
   * @return an initialized {@code DecisionTree}
   */
  public static DecisionTree withNodeValue(String value) {
    return new DecisionTree(new LeafNode(value));
  }

  /**
   * Add a branch to the tree along with a value for an attribute-value-check
   *
   * @param value the predicate value
   * @param subtree the subtree
   * @throws AssertionError if the {@code root} doesn't support this operation
   */
  public void addBranch(String value, DecisionTree subtree) {
    try {
      this.root.addChild(value, subtree.getRoot());
    } catch (UnsupportedOperationException e) {
      throw new AssertionError(
          "[DESIGN-NOTE] We shouldn't be adding branches to value nodes, please verify and make corrections.");
    }
  }

  public TreeNode getRoot() {
    return root;
  }

  @Override
  public Map<Example, String> predict(DataSet dataSet) {
    return dataSet.getExamples()
        .stream()
        .collect(toMap(identity(), e -> getRoot().process(e)));
  }

}
