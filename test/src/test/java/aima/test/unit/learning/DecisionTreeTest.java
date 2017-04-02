package aima.test.unit.learning;

import static aima.core.learning.NominalValue.create;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.learning.DecisionTree;
import aima.core.learning.api.Value;
import aima.test.unit.data.CoinTossData;
import aima.test.unit.data.CoinTossData.FingerCrossed;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class DecisionTreeTest {

  @Test
  public void itShouldNotAddBranchWithInvalidValue() {
    // Given
    DecisionTree tree = DecisionTree
        .withDecisionNode(CoinTossData.specs().getAttribute(FingerCrossed.label()));
    // When
    try {
      tree.addBranch(Value.NULL, DecisionTree.withLeafNode(Value.NULL));
    } catch (AssertionError e) {
      // Then
      assertThat(e.getMessage(), equalTo("Attribute must predicate on a valid value"));
      return;
    }
    throw new AssertionError("Expected exception: java.lang.AssertionError");
  }

  @Test
  public void itShouldNotAddBranchToEndNodes() {
    // Given
    DecisionTree treeWithEndNode = DecisionTree.withLeafNode(create("foo"));
    // When
    try {
      treeWithEndNode.addBranch(create("bar"), DecisionTree.withLeafNode(create("qux")));
    } catch (AssertionError e) {
      // Then
      assertThat(e.getMessage(), startsWith("[DESIGN-NOTE]"));
      return;
    }
    throw new AssertionError("Expected exception: java.lang.AssertionError");
  }


}
