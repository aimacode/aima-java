package aima.core.util;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
        .collect(groupingBy(Example::classValue, counting()))
        .values()
        .stream()
        .mapToDouble(Long::doubleValue)
        .map(count -> count / total)
        .map(prob -> (-1.0 * MathUtils.lg2(prob) * prob))
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
      List<Predicate<String>> predicates = attribute.getPredicates(examples);
      return predicates
      .stream()
      .map(p -> examples
          .stream()
          .filter(e -> {
              Optional<String> opt = e.valueOf(attribute);
              return opt.isPresent() ? p.test(opt.get()) : false;
            })
          .collect(toList()))
      .mapToDouble(exs -> (exs.size() / total) * infoNeeded(exs))
      .sum();
    }

  /**
   * Calculates entropy in an input set with binary class labels.
   *
   * @param p number of positive examples
   * @param n number of negative examples
   *
   * @return the entropy of the input set
   */
  public static Double entropy(long p, long n) {
    double fP = p <= 0 ? 0.0 : (double) p / (double) (n + p);
    double fN = n <= 0 ? 0.0 : (double) n / (double) (n + p);
    return (-1 * fP * MathUtils.lg2(fP)) + (-1 * fN * MathUtils.lg2(fN));
  }

}
