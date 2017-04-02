package aima.test.unit.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import aima.core.learning.Attribute;
import aima.core.util.ProbabilityUtils;
import aima.test.unit.data.SingleAttributeDataSet;
import aima.test.unit.data.SingleAttributeDataSet.FirstAttribute;
import aima.test.unit.data.SingleAttributeDataSet.ClassAttribute;
import java.util.Arrays;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class ProbabilityUtilTest {

  @Test
  public void itShouldCalculateInformationRemainingAsZeroForAttributeWithPerfectSplit() {
    // Given
    Attribute firstAttribute = SingleAttributeDataSet.specs().getAttribute(FirstAttribute.label());
    // When
    Double infoRemaining = ProbabilityUtils
        .infoRemaining(firstAttribute, Arrays.asList(
            SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.True),
            SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.True)));
    // Then
    assertThat(infoRemaining, is(0.0));
  }

  @Test
  public void itShouldCalculateInformationRemainingAsOneForAttributeWithEqualSplit() {
    // Given
    Attribute firstAttribute = SingleAttributeDataSet.specs().getAttribute(FirstAttribute.label());
    // When
    Double infoRemaining = ProbabilityUtils
        .infoRemaining(firstAttribute, Arrays.asList(
            SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.True),
            SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.False)));
    // Then
    assertThat(infoRemaining, is(1.0));
  }

  @Test
  public void itShouldCalculateInformationNeededAsZeroForExamplesWithSameClassification() {
    // When
    Double infoRemaining = ProbabilityUtils.infoNeeded(Arrays.asList(
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.True),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE3, ClassAttribute.True)));
    // Then
    assertThat(infoRemaining, is(0.0));
  }

  @Test
  public void itShouldCalculateInformationNeededAsOneForExamplesWithEqualNumberOfEachClassification() {
    // When
    Double infoRemaining = ProbabilityUtils.infoNeeded(Arrays.asList(
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE1, ClassAttribute.False),
        SingleAttributeDataSet.createExample(FirstAttribute.VALUE3, ClassAttribute.True)));
    // Then
    assertThat(infoRemaining, is(1.0));
  }

}
