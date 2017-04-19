package aima.test.unit.util;

import static aima.test.unit.data.CoinTossData.createExample;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.util.ProbabilityUtils;
import aima.test.unit.data.CoinTossData.FingersCrossed;
import aima.test.unit.data.CoinTossData.TossResult;
import java.util.Arrays;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class ProbabilityUtilTest {

  @Test
  public void itShouldCalculateInfoRemainingAsZeroForAttributeWithPerfectSplit() {
    // When
    Double infoRemaining = ProbabilityUtils.infoRemaining(
        FingersCrossed.attribute(),
        Arrays.asList(
            createExample(FingersCrossed.YES, TossResult.HEADS),
            createExample(FingersCrossed.YES, TossResult.HEADS)));

    // Then
    assertThat(infoRemaining, is(0.0));
  }

  @Test
  public void itShouldCalculateInfoRemainingAsOneForAttributeWithEqualSplit() {
    // When
    Double infoRemaining = ProbabilityUtils.infoRemaining(
        FingersCrossed.attribute(),
        Arrays.asList(
            createExample(FingersCrossed.YES, TossResult.HEADS),
            createExample(FingersCrossed.YES, TossResult.TAILS)));

    // Then
    assertThat(infoRemaining, is(1.0));
  }

  @Test
  public void itShouldCalculateInfoRemainingAsWeightedFraction() {
    // When
    Double infoRemaining = ProbabilityUtils.infoRemaining(
        FingersCrossed.attribute(),
        Arrays.asList(
            createExample(FingersCrossed.YES, TossResult.HEADS),
            createExample(FingersCrossed.YES, TossResult.HEADS),
            createExample(FingersCrossed.NO, TossResult.HEADS),
            createExample(FingersCrossed.NO, TossResult.TAILS)));

    // Then
    assertThat(infoRemaining, is(0.5));
  }

  @Test
  public void itShouldCalculateInfoNeededAsZeroForExamplesWithSameClassification() {
    // When
    Double infoRemaining = ProbabilityUtils.infoNeeded(
        Arrays.asList(
            createExample(FingersCrossed.YES, TossResult.HEADS),
            createExample(FingersCrossed.NO, TossResult.HEADS)));

    // Then
    assertThat(infoRemaining, is(0.0));
  }

  @Test
  public void itShouldCalculateInfoNeededAsOneForExamplesWithEqualNumberOfEachClassification() {
    // When
    Double infoRemaining = ProbabilityUtils.infoNeeded(
        Arrays.asList(
            createExample(FingersCrossed.YES, TossResult.TAILS),
            createExample(FingersCrossed.NO, TossResult.HEADS)));

    // Then
    assertThat(infoRemaining, is(1.0));
  }

}
