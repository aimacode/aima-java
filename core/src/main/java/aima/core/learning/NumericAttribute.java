package aima.core.learning;

import static aima.core.util.MathUtils.isNumber;
import static aima.core.util.ProbabilityUtils.entropy;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * {@link Attribute} implementation that only supports numeric (number) values.
 *
 * @author shantanusinghal
 */
public class NumericAttribute extends Attribute {

  public NumericAttribute(String name) {
    super(name);
  }

  /**
   * @return a list of two predicates checking for values less-than-equal-to or greater-than the
   * split value.
   */
  @Override
  protected List<Predicate<String>> initPredicates(List<Example> examples) {
    double split = getSplitsFor(examples);
    predicates = new ArrayList<>();
    predicates.add(s -> s != null && isNumber(s) ? Double.valueOf(s) <= split : false);
    predicates.add(s -> s != null && isNumber(s) ? Double.valueOf(s) > split : false);
    return predicates;
  }

  /**
   * @return True if the input {@code o} is a non-null, {@code NominalAttribute} instance with the
   * same name, False otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof NumericAttribute)) {
      return false;
    }

    NumericAttribute that = (NumericAttribute) o;

    return name.equals(that.name);

  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return "Attribute{" +
        "name='" + name + '\'' +
        '}';
  }

  /**
   * Section 18.3.6 Page 707 describes the approach taken here for identifying the split point in
   * Continuous and integer-valued input attributes.
   */
  private double getSplitsFor(List<Example> examples) {
    Attribute that = this;
    Example thisExample, nextExample;
    Map<Double, Double> splitBuckets = new HashMap<>();

    // sort the examples in ascending order of the value of 'this' attribute
    List<Example> sortedExamples = sort(examples);

    // if all examples have the same classification, just return the largest value of 'that'
    // attribute in the list of examples
    if (examples.stream().allMatch(e -> e.classValue().equals(examples.get(0).classValue()))) {
      return Double.valueOf(sortedExamples.get(examples.size() - 1).valueOf(that).get());
    }
    else {
      // build map of split points to the average entropy after splitting the input example over this point
      for (int i = 0; i < sortedExamples.size() - 1; i++) {
        thisExample = sortedExamples.get(i);
        nextExample = sortedExamples.get(i + 1);

        // if consecutive examples are differently classified
        if (!thisExample.classValue().equals(nextExample.classValue())) {
          // pick the mid-value between examples as a possible split point
          double split = (attributeValueIn(thisExample) + attributeValueIn(nextExample)) / 2.0;
          splitBuckets.put(split,
              averageEntropy(
                  split,
                  examples.subList(0, i + 1),
                  examples.subList(i + 1, examples.size())));
        }
      }

      // pick and return the split point corresponding to the lowest average entropy score
      return splitBuckets.entrySet()
          .stream()
          .min(comparing(Entry::getValue))
          .get().getKey();
    }
  }

  private List<Example> sort(List<Example> examples) {
    Attribute that = this;
    return examples
      .stream()
      .filter(e -> e.valueOf(that).isPresent())
      .sorted((o1, o2) -> Double.valueOf(o1.valueOf(that).get())
        .compareTo(Double.valueOf(o2.valueOf(that).get())))
      .collect(toList());
  }

  private Double attributeValueIn(Example example) {
    Optional<String> opt = example.valueOf(this);
    return opt.isPresent() && isNumber(opt.get()) ? Double.valueOf(opt.get()) : Double.NaN;
  }

  private Double averageEntropy(Double split, List<Example> lEq, List<Example> g) {
    long lEqCorrect = lEq.stream()
        .filter(ex -> attributeValueIn(ex) <= split).count();
    long gCorrect = g.stream()
        .filter(ex -> attributeValueIn(ex) > split).count();
    return (entropy(lEqCorrect, lEq.size()) + entropy(gCorrect, g.size())) / (double) 2;
  }

}
