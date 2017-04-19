package aima.test.unit.learning;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import aima.core.learning.LabeledExample;
import aima.core.learning.NominalAttribute;
import aima.core.learning.NumericAttribute;
import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.junit.Before;
import org.junit.Test;

/**
 * @author shantanusinghal
 */
public class NumericAttributeTest {

  private Attribute foo, bar;

  @Before
  public void setUp() throws Exception {
    foo = new NumericAttribute("foo");
    bar = new NominalAttribute("bar", Arrays.asList("Y", "N"));
  }

  /**
   * Should split examples at this point:
   *
   *                ⬇
   *  0 - 10 - 20 - 30 - 40
   *     (N)  (N)  (N)
   */
  @Test
  public void itShouldSplitUniformlyClassifiedExamplesAtTheLargestValue() {
    // Given
    List<Example> examples = new ArrayList<Example>() {{
      add(new LabeledExample(bar, "Y", map(foo, 10)));
      add(new LabeledExample(bar, "Y", map(foo, 20)));
      add(new LabeledExample(bar, "Y", map(foo, 30)));
    }};

    // When
    List<Predicate<String>> predicates = foo.getPredicates(examples);

    // Then
    assertThat(predicates.get(0).test("30"), is(true));
    assertThat(predicates.get(0).test("31"), is(false));

    assertThat(predicates.get(1).test("30"), is(false));
    assertThat(predicates.get(1).test("31"), is(true));
  }

  /**
   * Should split examples at this point:
   *
   *                   ⬇
   *  0 - 10 - 20 - 30 - 40
   *     (N)  (N)  (N)  (Y)
   */
  @Test
  public void itShouldSplitMidwayBetweenTwoConsecutiveDifferentlyClassifiedExamples() {
    // Given
    List<Example> examples = new ArrayList<Example>() {{
      add(new LabeledExample(bar, "N", map(foo, 10)));
      add(new LabeledExample(bar, "N", map(foo, 20)));
      add(new LabeledExample(bar, "N", map(foo, 30)));
      add(new LabeledExample(bar, "Y", map(foo, 40)));
    }};

    // When
    List<Predicate<String>> predicates = foo.getPredicates(examples);

    // Then
    assertThat(predicates.get(0).test("35"), is(true));
    assertThat(predicates.get(0).test("36"), is(false));

    assertThat(predicates.get(1).test("35"), is(false));
    assertThat(predicates.get(1).test("36"), is(true));
  }

  private Map<Attribute, String> map(Attribute attribute, int value) {
    return new HashMap<Attribute, String>() {{
      put(attribute, String.valueOf(value));
    }};
  }
}
