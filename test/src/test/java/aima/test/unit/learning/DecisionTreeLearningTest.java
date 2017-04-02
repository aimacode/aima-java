package aima.test.unit.learning;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.learning.DataSet;
import aima.core.learning.DecisionNode;
import aima.core.learning.DecisionTree;
import aima.core.learning.DecisionTreeLearning;
import aima.core.learning.LeafNode;
import static aima.core.learning.NominalValue.create;

import aima.core.learning.NominalValue;
import aima.core.learning.api.Example;
import aima.core.learning.api.Value;
import aima.test.unit.data.CoinTossData;
import aima.test.unit.data.CoinTossData.FingerCrossed;
import aima.test.unit.data.CoinTossData.Result;
import aima.test.unit.data.GolfData;
import aima.test.unit.data.GolfData.Humid;
import aima.test.unit.data.GolfData.Play;
import aima.test.unit.data.GolfData.Windy;
import org.junit.Before;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class DecisionTreeLearningTest {

  private DataSet golfDataSet, coinDataSet;

  @Before
  public void setUpData() {
    golfDataSet = new DataSet(GolfData.specs());
    coinDataSet = new DataSet(CoinTossData.specs());
  }

  // Then
  @Test(expected = IllegalArgumentException.class)
  public void itShouldNotBeAbleToMakePredictionsOnIncompatibleDataSets() {
    // Given
    DecisionTree tree = DecisionTree
        .withDecisionNode(GolfData.specs().getAttribute(Humid.label()));
    // When
    tree.predict(coinDataSet.withExamples(
        CoinTossData.createExample(FingerCrossed.YES, Result.TAILS)));
  }

  /**
   * Test if decision tree correctly learns a single node tree
   * Should learn tree like this:
   *                 *
   *                 |
   *              (TAILS)
   */
  @Test
  public void itShouldReturnDecisionTreeWithOneValueNodeThatAlwaysPredictsTheSameClass() {
    // Given
    Example trainingExample = CoinTossData.createExample(FingerCrossed.NO, Result.TAILS);
    // When
    DecisionTree tree = new DecisionTreeLearning()
        .train(new DataSet(CoinTossData.specs()).withExamples(trainingExample));
    // Then
    Value sameClassValue = trainingExample.getClassValue();
    assertThat(tree.getRoot(), is(instanceOf(LeafNode.class)));
    tree.predict(coinDataSet.withExamples(
        CoinTossData.createExample(FingerCrossed.NO, Result.HEADS),
        CoinTossData.createExample(FingerCrossed.NO, Result.TAILS),
        CoinTossData.createExample(FingerCrossed.YES, Result.HEADS),
        CoinTossData.createExample(FingerCrossed.YES, Result.HEADS)))
        .forEach((e, prediction) ->
            assertThat(prediction, is(sameClassValue)));
  }

  /**
   * Test if decision tree correctly learns a tree with one decision node
   * Should learn tree like this:
   *                  *
   *                  |
   *            FingerCrossed
   *              /       \
   *            yes       No
   *            /           \
   *        (HEADS)      (HEADS)
   */
  @Test
  public void itShouldReturnDecisionTreeWithOneChoiceNodeThatPredictsTheMajorityClass() {
    // Given
    Value majorityClassValue = create(Result.HEADS.name());
    // When
    DecisionTree tree = new DecisionTreeLearning().train(coinDataSet.withExamples(
        CoinTossData.createExample(FingerCrossed.YES, Result.TAILS),
        CoinTossData.createExample(FingerCrossed.YES, Result.HEADS),
        CoinTossData.createExample(FingerCrossed.YES, Result.HEADS),
        CoinTossData.createExample(FingerCrossed.YES, Result.HEADS)
    ));
    // Then
    assertThat(tree.getRoot(), is(instanceOf(DecisionNode.class)));
    assertThat(tree.getRoot().getChildren().size(), is(FingerCrossed.values().length));
    tree.predict(new DataSet(CoinTossData.specs()).withExamples(
        CoinTossData.createExample(FingerCrossed.NO, Result.HEADS),
        CoinTossData.createExample(FingerCrossed.NO, Result.TAILS),
        CoinTossData.createExample(FingerCrossed.YES, Result.HEADS),
        CoinTossData.createExample(FingerCrossed.YES, Result.HEADS)))
        .forEach((e, prediction) ->
          assertThat(prediction, is(majorityClassValue)));
  }

  /**
   * Test if decision tree correctly learns simple AND function.
   * Should learn tree like this:
   *                  *
   *                  |
   *                Humid
   *              /      \
   *           TRUE     FALSE
   *            /          \
   *         Windy       (FALSE)
   *        /    \
   *     TRUE   FALSE
   *     /         \
   *  (TRUE)     (FALSE)
   */
  @Test
  public void itShouldLearnLogicalAndFunction() {
    // Given
    // When
    DecisionTree tree = new DecisionTreeLearning().train(golfDataSet.withExamples(
        GolfData.createExample(Humid.TRUE, Windy.TRUE, Play.TRUE),
        GolfData.createExample(Humid.TRUE, Windy.FALSE, Play.FALSE),
        GolfData.createExample(Humid.FALSE, Windy.TRUE, Play.FALSE),
        GolfData.createExample(Humid.FALSE, Windy.FALSE, Play.FALSE)));
    // Then
    assertThat(tree.getRoot().getChild(NominalValue.FALSE).getValue(), is(NominalValue.FALSE));
    assertThat(tree.getRoot().getChild(NominalValue.TRUE).getChild(NominalValue.FALSE).getValue(), is(NominalValue.FALSE));
    assertThat(tree.getRoot().getChild(NominalValue.TRUE).getChild(NominalValue.TRUE).getValue(), is(NominalValue.TRUE));
  }

  /**
   * Test if decision tree correctly learns simple AND function.
   * Should learn tree like this:
   *                  *
   *                  |
   *                Humid
   *              /      \
   *           FALSE    TRUE
   *            /         \
   *         Windy      (TRUE)
   *        /    \
   *     TRUE   FALSE
   *     /         \
   *  (TRUE)     (FALSE)
   */
  @Test
  public void itShouldLearnLogicalOrFunction() {
    // Given
    // When
    DecisionTree tree = new DecisionTreeLearning().train(golfDataSet.withExamples(
        GolfData.createExample(Humid.TRUE, Windy.TRUE, Play.TRUE),
        GolfData.createExample(Humid.TRUE, Windy.FALSE, Play.TRUE),
        GolfData.createExample(Humid.FALSE, Windy.TRUE, Play.TRUE),
        GolfData.createExample(Humid.FALSE, Windy.FALSE, Play.FALSE)));
    // Then
    assertThat(tree.getRoot().getChild(NominalValue.TRUE).getValue(), is(NominalValue.TRUE));
    assertThat(tree.getRoot().getChild(NominalValue.FALSE).getChild(NominalValue.FALSE).getValue(), is(NominalValue.FALSE));
    assertThat(tree.getRoot().getChild(NominalValue.FALSE).getChild(NominalValue.TRUE).getValue(), is(NominalValue.TRUE));
  }

}
