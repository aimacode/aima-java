package aima.test.unit.learning;

import static aima.test.unit.data.CoinTossData.HEADS;
import static aima.test.unit.data.GolfData.createExample;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.learning.DecisionNode;
import aima.core.learning.DecisionTree;
import aima.core.learning.DecisionTreeLearning;
import aima.core.learning.LeafNode;
import aima.core.learning.api.Example;
import aima.core.learning.api.Node;
import aima.test.unit.data.CoinTossData;
import aima.test.unit.data.CoinTossData.FingersCrossed;
import aima.test.unit.data.CoinTossData.TossResult;
import aima.test.unit.data.GolfData;
import aima.test.unit.data.GolfData.Humid;
import aima.test.unit.data.GolfData.Play;
import aima.test.unit.data.GolfData.Windy;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class DecisionTreeLearningTest {

  private static DecisionTreeLearning decisionTreeLearner;

  @BeforeClass
  public static void setUp() {
    decisionTreeLearner = new DecisionTreeLearning();
  }

  /**
   * Test if decision tree correctly learns a single node tree
   * Should learn tree like this:
   *    *
   *    |
   * (TAILS)
   */
  @Test
  public void itShouldReturnDecisionTreeWithOneValueNodeThatAlwaysPredictsTheSameClass() {
    // Given
    Example trainingExample = CoinTossData.createExample(FingersCrossed.NO, TossResult.TAILS);

    // When
    DecisionTree tree = decisionTreeLearner.train(
        CoinTossData.set().addExamples(trainingExample));

    // Then
    assertThat(tree.getRoot(), is(instanceOf(LeafNode.class)));
    tree.predict(CoinTossData.set().addExamples(
        CoinTossData.createExample(FingersCrossed.NO, TossResult.HEADS),
        CoinTossData.createExample(FingersCrossed.NO, TossResult.TAILS),
        CoinTossData.createExample(FingersCrossed.YES, TossResult.HEADS),
        CoinTossData.createExample(FingersCrossed.YES, TossResult.TAILS)))
        .forEach((e, prediction) ->
            assertThat(prediction, is(trainingExample.classValue().get())));
  }

  /**
   * Test if decision tree correctly learns a tree with one decision node
   * Should learn tree like this:
   *          *
   *          |
   *    FingerCrossed
   *     /        \
   *    YES       NO
   *   /           \
   * (HEADS)     (HEADS)
   */
  @Test
  public void itShouldReturnDecisionTreeWithOneChoiceNodeThatPredictsTheMajorityClass() {
    // Given
    String majorityClassValue = HEADS;

    // When
    DecisionTree tree = decisionTreeLearner.train(
        CoinTossData.set().addExamples(
            CoinTossData.createExample(FingersCrossed.YES, TossResult.HEADS),
            CoinTossData.createExample(FingersCrossed.YES, TossResult.HEADS),
            CoinTossData.createExample(FingersCrossed.YES, TossResult.HEADS),
            CoinTossData.createExample(FingersCrossed.YES, TossResult.TAILS)));

    // Then
    assertThat(tree.getRoot(), is(instanceOf(DecisionNode.class)));
    tree.predict(CoinTossData.set().addExamples(
        CoinTossData.createExample(FingersCrossed.YES, TossResult.HEADS),
        CoinTossData.createExample(FingersCrossed.NO, TossResult.HEADS),
        CoinTossData.createExample(FingersCrossed.YES, TossResult.TAILS),
        CoinTossData.createExample(FingersCrossed.NO, TossResult.TAILS)))
        .forEach((e, prediction) ->
            assertThat(prediction, is(majorityClassValue)));
  }

  /**
   * Test if decision tree correctly learns simple AND function.
   * Should learn tree like this:
   *             *
   *             |
   *           Humid
   *          /    \
   *        TRUE   FALSE
   *        /       \
   *      Windy    (FALSE)
   *      /   \
   *    TRUE  FALSE
   *    /      \
   * (TRUE)   (FALSE)
   */
  @Test
  public void itShouldLearnLogicalAndFunction() {
    // When
    DecisionTree tree = new DecisionTreeLearning().train(GolfData.set().addExamples(
        createExample(Humid.TRUE, Windy.TRUE, Play.TRUE),
        createExample(Humid.TRUE, Windy.FALSE, Play.FALSE),
        createExample(Humid.FALSE, Windy.TRUE, Play.FALSE),
        createExample(Humid.FALSE, Windy.FALSE, Play.FALSE)));

    // Then
    Node root = tree.getRoot();
    assertThat(root, is(instanceOf(DecisionNode.class)));
    assertThat(root.getAttribute(), is(Humid.attribute()));

    Node humidFalse = root.getChild(GolfData.FALSE).get();
    assertThat(humidFalse, is(instanceOf(LeafNode.class)));
    assertThat(humidFalse.getValue(), is(GolfData.FALSE));

    Node humidTrue = root.getChild(GolfData.TRUE).get();
    assertThat(humidTrue, is(instanceOf(DecisionNode.class)));
    assertThat(humidTrue.getAttribute(), is(Windy.attribute()));

    Node humidTrueAndWindyFalse = humidTrue.getChild(GolfData.FALSE).get();
    assertThat(humidTrueAndWindyFalse, is(instanceOf(LeafNode.class)));
    assertThat(humidTrueAndWindyFalse.getValue(), is(GolfData.FALSE));

    Node humidTrueAndWindyTrue = humidTrue.getChild(GolfData.TRUE).get();
    assertThat(humidTrueAndWindyTrue, is(instanceOf(LeafNode.class)));
    assertThat(humidTrueAndWindyTrue.getValue(), is(GolfData.TRUE));
  }

  /**
   * Test if decision tree correctly learns simple OR function.
   * Should learn tree like this:
   *            *
   *            |
   *          Humid
   *         /    \
   *      FALSE   TRUE
   *       /       \
   *     Windy    (TRUE)
   *     /   \
   *   TRUE  FALSE
   *   /      \
   * (TRUE)  (FALSE)
   */
  @Test
  public void itShouldLearnLogicalOrFunction() {
    // When
    DecisionTree tree = new DecisionTreeLearning().train(GolfData.set().addExamples(
        createExample(Humid.TRUE, Windy.TRUE, Play.TRUE),
        createExample(Humid.TRUE, Windy.FALSE, Play.TRUE),
        createExample(Humid.FALSE, Windy.TRUE, Play.TRUE),
        createExample(Humid.FALSE, Windy.FALSE, Play.FALSE)));

    // Then
    Node root = tree.getRoot();
    assertThat(root, is(instanceOf(DecisionNode.class)));
    assertThat(root.getAttribute(), is(Humid.attribute()));

    Node humidTrue = root.getChild(GolfData.TRUE).get();
    assertThat(humidTrue, is(instanceOf(LeafNode.class)));
    assertThat(humidTrue.getValue(), is(GolfData.TRUE));

    Node humidFalse = root.getChild(GolfData.FALSE).get();
    assertThat(humidFalse, is(instanceOf(DecisionNode.class)));
    assertThat(humidFalse.getAttribute(), is(Windy.attribute()));

    Node humidFalseAndWindyFalse = humidFalse.getChild(GolfData.FALSE).get();
    assertThat(humidFalseAndWindyFalse, is(instanceOf(LeafNode.class)));
    assertThat(humidFalseAndWindyFalse.getValue(), is(GolfData.FALSE));

    Node humidFalseAndWindyTrue = humidFalse.getChild(GolfData.TRUE).get();
    assertThat(humidFalseAndWindyTrue, is(instanceOf(LeafNode.class)));
    assertThat(humidFalseAndWindyTrue.getValue(), is(GolfData.TRUE));
  }

}
