package aima.core.util;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import aima.core.learning.Attribute;
import aima.core.learning.Example;
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
   * @param a the attribute being tested
   * @param examples the sample space for testing
   * @return the expected entropy remaining
   */
  public static Double infoRemaining(Attribute a, List<Example> examples) {
    double total = examples.size();
    Map<String, List<Example>> valueToExamplesMap = examples
        .stream()
        .collect(groupingBy(e -> e.getValue(a)));
    return a.possibleValues()
        .stream()
        .mapToDouble(val -> {
          List<Example> matchingExamples = valueToExamplesMap.get(val);
          return matchingExamples == null ? 0
              : (matchingExamples.size() / total) * infoNeeded(matchingExamples);
        })
        .sum();
  }

}
