package aima.core.util;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.api.Value;
import java.util.List;
import java.util.Map;

/**
 * @author shantanusinghal
 */
public class ProbabilityUtils {

  /**
   * Calculates the information gain from an attribute as the expected reduction in entropy
   *
   * @param attribute the attribute being tested
   * @param examples the sample space for testing
   * @return the information gain
   */
  public static Double gain(Attribute attribute, List<Example> examples) {
    return infoNeeded(examples) - infoRemaining(attribute, examples);
  }

  /**
   * Calculates the entropy of the set of examples
   *
   * @param examples the set of examples being tested
   * @return the expected information needed to determine the category of one these examples
   */
  public static Double infoNeeded(List<Example> examples) {
    double total = examples.size();
    return examples
        .stream()
        .collect(groupingBy(Example::getClassValue, counting()))
        .values()
        .stream()
        .mapToDouble(Long::doubleValue)
        .map(count -> count / total)
        .map(prob -> (-1.0 * Util.lg2(prob) * prob))
        .sum();
  }

  /**
   * Calculates the expected entropy remaining after testing attribute {@code a}
   *
   * @param attribute the attribute being tested
   * @param examples the sample space for testing
   * @return the expected entropy remaining
   */
  public static Double infoRemaining(Attribute attribute, List<Example> examples) {
    double total = examples.size();
    return examples
      .stream()
      .collect(groupingBy(e -> e.valueOf(attribute).orElse(Value.NULL)))
      .values()
      .stream()
      .mapToDouble(exs -> (exs.size() / total) * infoNeeded(exs))
      .sum();
  }

}
