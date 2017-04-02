package aima.core.learning;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import aima.core.learning.api.Learner;
import aima.core.util.ProbabilityUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 18.5, page 702. <br>
 *
 * <p>The DECISION-TREE-LEARNING algorithm is used to learn a decision tree. The Algorithm
 * adopts a greedy divide-and-conquer strategy: always test the most important attribute first. This
 * test divides the problem up into smaller sub-problems that can then be solved recursively.</p>
 *
 * <p><b>NOTE</b> that this implementation only supports nominal valued attributes</p>
 *
 * <pre>
 * <b>function</b> DECISION-TREE-LEARNING(examples, attributes, parent_examples) <b>returns</b> a tree
 *   <b>if</b> <i>examples</i> is empty <b>then return</b> PLURALITY-VALUE(<i>parent_examples</i>)
 *   <b>else if</b> all <i>examples</i> have the same classification <b>then return</b> the classification
 *   <b>else if</b> <i>attributes</i> is empty <b>then return</b> PLURALITY-VALUE(<i>examples</i>)
 *   <b>else</b>
 *     <i>A</i> ← argmax<i><sub>a ∈ attributes</sub></i> IMPORTANCE(<i>a</i>, <i>examples</i>)
 *     <i>tree</i> ← a new decision tree with root test <i>A</i>
 *     <b>for each</b> value <i>v<sub>k</sub></i> of A <b>do</b>
 *       <i>exs</i> ← { e : e ∈ <i>examples</i> <b>and</b> e.<i>A</i> = <i>v<sub>k</sub></i> }
 *       <i>subtree</i> ← DECISION-TREE-LEARNING(<i>exs</i>, <i>attributes</i> − <i>A</i>, <i>examples</i>)
 *       add a branch to <i>tree</i> with label (<i>A</i> = <i>v<sub>k</sub></i>) and subtree <i>subtree</i>
 *     <b>return</b> <i>tree</i>
 * </pre>
 *
 * Section 18.3.4 describes the notion of information gain which is used in the implementation of
 * the {@code IMPORTANCE} function.
 *
 * @author Shantanu Singhal
 */
public class DecisionTreeLearning implements Learner {

  @Override
  public DecisionTree train(DataSet dataSet) {
    return decisionTreeLearning(dataSet.getExamples(), dataSet.getAttributes(),
        Collections.emptyList());
  }

  /**
   * The decision-tree learning algorithm
   *
   * @param examples the labeled training data
   * @param attributes the set of features common across all examples
   * @param parentExamples list of examples used in constructing the node in previous iteration;
   * initially empty
   * @return a boolean decision tree classifier
   */
  private DecisionTree decisionTreeLearning(List<Example> examples, List<Attribute> attributes,
      List<Example> parentExamples) { List<String> values;

    if (examples.isEmpty()) {
      return DecisionTree.withNodeValue(
          pluralityValue(parentExamples));
    }
    else if ((values = getDistinctClassValues(examples)).size() == 1) {
      return DecisionTree.withNodeValue(values.get(0));
    }
    else if (attributes.isEmpty()) {
      return DecisionTree.withNodeValue(
          pluralityValue(examples));
    }
    else {
      Attribute A = pickMaxFrom( getImportance (examples, attributes) );

      DecisionTree tree = DecisionTree.withRoot(new DecisionNode(A));

      A.possibleValues().forEach(vK -> {
        List<Example> exs = examples.stream()
            .filter(e -> e.getValue(A).equals(vK))
            .collect(toList());
        List<Attribute> attributesExceptA = attributes
            .stream()
            .filter(a -> !a.equals(A))
            .collect(toList());
        DecisionTree subtree = decisionTreeLearning(exs, attributesExceptA, examples);
        tree.addBranch(vK, subtree);
      });

      return tree;
    }
  }

  /**
   * Selects the most common class value among a set of examples
   *
   * @param examples the list of input instances
   * @return the most common class value
   */
  private String pluralityValue(List<Example> examples) {
    return examples
        .stream()
        .collect(groupingBy(Example::getClassValue, counting()))
        .entrySet()
        .stream()
        .max(comparing(Map.Entry::getValue))
        .get().getKey();
  }

  /**
   * Returns a map of information gain from each attribute
   *
   * @param examples the training data for measuring information gain
   * @param attributes a list of attributes
   * @return a map of information gain from each attribute
   */
  private Map<Attribute, Double> getImportance(List<Example> examples, List<Attribute> attributes) {
    return attributes
        .stream()
        .collect(toMap(identity(), a -> ProbabilityUtils.gain(a, examples)));
  }

  /**
   * Selects the attribute key corresponding to the largest value in the map
   *
   * @param map the input map
   * @return the attribute with largest value
   */
  private Attribute pickMaxFrom(Map<Attribute, Double> map) {
    return map.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
  }

  /**
   * @param examples the set of examples being tested
   * @return a list of distinct class values in the input data
   */
  private List<String> getDistinctClassValues(List<Example> examples) {
    return examples
        .stream()
        .map(Example::getClassValue)
        .distinct()
        .collect(toList());
  }


}
