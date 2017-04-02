package aima.test.unit.learning;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.learning.DataSet;
import aima.core.learning.DecisionNode;
import aima.core.learning.DecisionTree;
import aima.core.learning.DecisionTreeLearning;
import aima.core.learning.LeafNode;
import aima.core.learning.Example;
import aima.test.unit.data.SingleAttributeDataSet;
import aima.test.unit.data.SingleAttributeDataSet.FirstAttribute;
import aima.test.unit.data.SingleAttributeDataSet.ClassAttribute;
import org.junit.Before;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class DecisionTreeLearningTest {

  private Example trainingExample;
  private DataSet dataSet, anotherDataSet;

  @Before
  public void initializeDataSet() {
    dataSet = new DataSet(SingleAttributeDataSet.specs());
    anotherDataSet = new DataSet(SingleAttributeDataSet.specs());
  }

  @Test
  public void itShouldReturnDecisionTreeWithOneValueNodeThatAlwaysPredictsTheSameClass() {
    // Given
    trainingExample = SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.True);
    // When
    DecisionTree tree = new DecisionTreeLearning().train(dataSet.addExamples(trainingExample));
    // Then
    assertThat(tree.getRoot(), is(instanceOf(LeafNode.class)));
    tree.predict(anotherDataSet.addExamples(
      SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.True),
      SingleAttributeDataSet.createExample(FirstAttribute.VALUE3, ClassAttribute.False),
      SingleAttributeDataSet.createExample(FirstAttribute.VALUE2, ClassAttribute.True))).forEach((e, prediction) ->
        assertThat(prediction, is(trainingExample.getClassValue())));
  }

  @Test
  public void itShouldReturnDecisionTreeWithOneChoiceNodeThatPredictsTheMajorityClass() {
    // Given
    String majorityClassValue = ClassAttribute.False.name();
    // When
    DecisionTree tree = new DecisionTreeLearning().train(dataSet.addExamples(
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.True),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.False),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.False),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.False)
    ));
    // Then
    assertThat(tree.getRoot(), is(instanceOf(DecisionNode.class)));
    assertThat(tree.getRoot().getChildren().size(), is(FirstAttribute.values().length));
    tree.predict(anotherDataSet.addExamples(
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.True),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.False),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE3, ClassAttribute.False),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE3, ClassAttribute.True),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE2, ClassAttribute.False),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE2, ClassAttribute.True))).forEach((e, prediction) ->
        assertThat(prediction, is(majorityClassValue)));
  }

}
