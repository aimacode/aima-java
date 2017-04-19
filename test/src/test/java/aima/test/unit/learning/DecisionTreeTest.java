package aima.test.unit.learning;

import static aima.test.unit.data.CoinTossData.NO;
import static aima.test.unit.data.CoinTossData.YES;
import static aima.test.unit.data.CoinTossData.createExample;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.learning.DecisionTree;
import aima.core.learning.api.Example;
import aima.core.learning.data.DataSet;
import aima.test.unit.data.CoinTossData;
import aima.test.unit.data.CoinTossData.FingersCrossed;
import aima.test.unit.data.CoinTossData.TossResult;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class DecisionTreeTest {

  private String prediction;
  private Map<Example, String> predictions;
  private DecisionTree decisionTree, subTree;
  private Predicate<String> ifFingersAreCrossed;

  @Before
  public void setUp() throws Exception {
    subTree = DecisionTree.withLeafNode(TossResult.HEADS.name());
    ifFingersAreCrossed = s -> s.equals(FingersCrossed.YES.name());
  }

  @Test
  public void itShouldNotAddBranchesToLeafNodes() {
    // Given
    decisionTree = DecisionTree.withLeafNode(YES);

    // When
    try {
      decisionTree.addBranch(ifFingersAreCrossed, subTree);
    } catch (AssertionError e) {

      // Then
      assertThat(e.getMessage(), startsWith("[DESIGN-NOTE]"));
      return;
    }
    throw new AssertionError("Expected exception: java.lang.AssertionError");
  }

  @Test
  public void itShouldAddPredicatedBranchesToDecisionNodes() {
    // Given
    decisionTree = DecisionTree.withDecisionNode(FingersCrossed.attribute());

    // Then
    assertThat(decisionTree.getRoot().getChildren().size(), is(0));

    // And
    // When
    decisionTree.addBranch(ifFingersAreCrossed, subTree);

    // Then
    assertThat(decisionTree.getRoot().getChildren().size(), is(1));
    assertThat(decisionTree.getRoot().getChild(NO).isPresent(), is(false));
    assertThat(decisionTree.getRoot().getChild(YES).isPresent(), is(true));
    assertThat(decisionTree.getRoot().getChild(YES).get(), is(subTree.getRoot()));
  }

  // Then
  @Test(expected = IllegalArgumentException.class)
  public void itShouldRaiseExceptionWhenExampleHasMissingData() {
    // Given
    decisionTree = DecisionTree.withDecisionNode(FingersCrossed.attribute());
    Example exampleWithMissingData = createExample(TossResult.HEADS);

    // When
    decisionTree.predict(exampleWithMissingData);
  }

  // Then
  @Test(expected = NoSuchElementException.class)
  public void itShouldRaiseExceptionWhenExampleDoesntFitTheDecisionTree() {
    // Given
    decisionTree = DecisionTree.withDecisionNode(FingersCrossed.attribute());
    Example exampleWhereFingersAreNotCrossed =
        createExample(FingersCrossed.NO, TossResult.HEADS);

    // When
    decisionTree.addBranch(ifFingersAreCrossed, subTree);
    // And
    decisionTree.predict(exampleWhereFingersAreNotCrossed);
  }

  @Test
  public void itShouldPredictClassValueWhenExampleFitsTheDecisionTree() {
    // Given
    decisionTree = DecisionTree.withDecisionNode(FingersCrossed.attribute());
    decisionTree.addBranch(ifFingersAreCrossed, subTree);
    Example example = createExample(FingersCrossed.YES, TossResult.HEADS);

    // When
    prediction = decisionTree.predict(example);

    // Then
    assertThat(prediction, is(subTree.getRoot().getValue()));
  }

  @Test
  public void itShouldPredictClassValuesIfAllExampleInDatasetFitTheDecisionTree() {
    // Given
    decisionTree = DecisionTree.withDecisionNode(FingersCrossed.attribute());
    decisionTree.addBranch(ifFingersAreCrossed, subTree);
    DataSet dataSet = CoinTossData.set().addExamples(
        createExample(FingersCrossed.YES, TossResult.HEADS),
        createExample(FingersCrossed.YES, TossResult.TAILS));

    // When
    predictions = decisionTree.predict(dataSet);

    // Then
    assertThat(predictions.size(), is(dataSet.getExamples().size()));
    assertThat(predictions.get(dataSet.getExamples().get(0)), is(subTree.getRoot().getValue()));
    assertThat(predictions.get(dataSet.getExamples().get(1)), is(subTree.getRoot().getValue()));
  }

  @After
  public void tearDown() throws Exception {
    decisionTree = null;
    prediction = null;
    predictions = null;
  }

}
