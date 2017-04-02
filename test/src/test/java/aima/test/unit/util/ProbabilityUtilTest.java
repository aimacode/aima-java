package aima.test.unit.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.learning.api.Attribute;
import aima.core.util.ProbabilityUtils;
import aima.test.unit.data.CoinTossData;
import aima.test.unit.data.CoinTossData.FingerCrossed;
import aima.test.unit.data.CoinTossData.Result;
import java.util.Arrays;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class ProbabilityUtilTest {

  @Test
  public void itShouldCalculateInformationRemainingAsZeroForAttributeWithPerfectSplit() {
    // Given
    Attribute attribute = CoinTossData.getAttribute(FingerCrossed.label());
    // When
    Double infoRemaining = ProbabilityUtils
        .infoRemaining(attribute, Arrays.asList(
            CoinTossData.createExample(FingerCrossed.YES, Result.HEADS),
            CoinTossData.createExample(FingerCrossed.YES, Result.HEADS)));
    // Then
    assertThat(infoRemaining, is(0.0));
  }


  @Test
  public void itShouldCalculateInformationRemainingAsOneForAttributeWithEqualSplit() {
    // Given
    Attribute attribute = CoinTossData.getAttribute(FingerCrossed.label());
    // When
    Double infoRemaining = ProbabilityUtils
        .infoRemaining(attribute, Arrays.asList(
            CoinTossData.createExample(FingerCrossed.YES, Result.HEADS),
            CoinTossData.createExample(FingerCrossed.YES, Result.TAILS)));
    // Then
    assertThat(infoRemaining, is(1.0));
  }

  @Test
  public void itShouldCalculateInformationNeededAsZeroForExamplesWithSameClassification() {
    // When
    Double infoRemaining = ProbabilityUtils
        .infoNeeded(Arrays.asList(
            CoinTossData.createExample(FingerCrossed.YES, Result.HEADS),
            CoinTossData.createExample(FingerCrossed.NO, Result.HEADS)));
    // Then
    assertThat(infoRemaining, is(0.0));
  }

  @Test
  public void itShouldCalculateInformationNeededAsOneForExamplesWithEqualNumberOfEachClassification() {
    // When
    Double infoRemaining = ProbabilityUtils
        .infoNeeded(Arrays.asList(
            CoinTossData.createExample(FingerCrossed.YES, Result.TAILS),
            CoinTossData.createExample(FingerCrossed.NO, Result.HEADS)));
    // Then
    assertThat(infoRemaining, is(1.0));
  }

}
