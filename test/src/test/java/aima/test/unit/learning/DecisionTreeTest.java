package aima.test.unit.learning;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.learning.DataSet;
import aima.core.learning.DecisionNode;
import aima.core.learning.DecisionTree;
import aima.test.unit.data.SingleAttributeDataSet;
import aima.test.unit.data.SingleAttributeDataSet.ClassAttribute;
import aima.test.unit.data.SingleAttributeDataSet.FirstAttribute;
import aima.test.unit.data.TwoAttributeDataSet;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class DecisionTreeTest {

  // Then
  @Test(expected = IllegalArgumentException.class)
  public void itShouldNotBeAbleToMakePredictionsOnIncompatibleDataSets() {
    // Given
    DecisionTree tree = DecisionTree
        .withRoot(new DecisionNode(SingleAttributeDataSet.specs().getAttribute(FirstAttribute.label())));
    // When
    tree.predict(new DataSet(TwoAttributeDataSet.specs())
        .addExamples(TwoAttributeDataSet.createExample(
            TwoAttributeDataSet.FirstAttribute.VALUE1,
            TwoAttributeDataSet.SecondAttribute.VALUE2,
            ClassAttribute.False)));
  }

  @Test
  public void itShouldNotAddBranchWithInvalidValue() {
    // Given
    DecisionTree tree = DecisionTree
        .withRoot(new DecisionNode(SingleAttributeDataSet.specs().getAttribute(FirstAttribute.label())));
    // When
    try {
      tree.addBranch("Thunderstorm", DecisionTree.withNodeValue("foo"));
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
    DecisionTree treeWithEndNode = DecisionTree.withNodeValue("foo");
    // When
    try {
      treeWithEndNode.addBranch(FirstAttribute.VALUE3.name(), DecisionTree.withNodeValue("bar"));
    } catch (AssertionError e) {
      // Then
      assertThat(e.getMessage(), startsWith("[DESIGN-NOTE]"));
      return;
    }
    throw new AssertionError("Expected exception: java.lang.AssertionError");
  }

}
