package aima.core.learning.learners;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.ConstantDecisonTree;
import aima.core.learning.inductive.DecisionTree;
import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class DecisionTreeLearner implements Learner {
	private DecisionTree tree;

	private String defaultValue;

	public DecisionTreeLearner() {
		this.defaultValue = "Unable To Classify";

	}

	// used when you have to test a non induced tree (eg: for testing)
	public DecisionTreeLearner(DecisionTree tree, String defaultValue) {
		this.tree = tree;
		this.defaultValue = defaultValue;
	}

	//
	// START-Learner

	/**
	 * Induces the decision tree from the specified set of examples
	 * 
	 * @param ds
	 *            a set of examples for constructing the decision tree
	 */
	@Override
	public void train(DataSet ds) {
		List<String> attributes = ds.getNonTargetAttributes();
		this.tree = decisionTreeLearning(ds, attributes,
										 new ConstantDecisonTree(defaultValue));
	}

	@Override
	public String predict(Example e) {
		return (String) tree.predict(e);
	}

	@Override
	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		ds.examples.stream().forEach(example -> {
			if (example.targetValue().equals(tree.predict(example))) {
				results[0] = results[0] + 1;
			} else {
				results[1] = results[1] + 1;
			}
		});

		return results;
	}

	// END-Learner
	//

	/**
	 * Returns the decision tree of this decision tree learner
	 * 
	 * @return the decision tree of this decision tree learner
	 */
	public DecisionTree getDecisionTree() {
		return tree;
	}

	//
	// PRIVATE METHODS
	//

	private DecisionTree decisionTreeLearning(DataSet ds,
			List<String> attributeNames, ConstantDecisonTree defaultTree) {
		if (ds.size() == 0) {
			return defaultTree;
		}
		if (allExamplesHaveSameClassification(ds)) {
			return new ConstantDecisonTree(ds.getExample(0).targetValue());
		}
		if (attributeNames.size() == 0) {
			return majorityValue(ds);
		}
		String chosenAttribute = chooseAttribute(ds, attributeNames);

		DecisionTree tree = new DecisionTree(chosenAttribute);
		ConstantDecisonTree m = majorityValue(ds);

		List<String> values = ds.getPossibleAttributeValues(chosenAttribute);

		values.stream().forEach(value -> {
			DataSet filtered = ds.matchingDataSet(chosenAttribute, value);
			List<String> newAttribs = Util.removeFrom(attributeNames, chosenAttribute);
			DecisionTree subTree = decisionTreeLearning(filtered, newAttribs, m);
			tree.addNode(value, subTree);
		});

		return tree;
	}

	private ConstantDecisonTree majorityValue(DataSet ds) {
		Learner learner = new MajorityLearner();
		learner.train(ds);
		return new ConstantDecisonTree(learner.predict(ds.getExample(0)));
	}

	private String chooseAttribute(DataSet ds, List<String> attributeNames) {
		/* Use stream over List and use maxBy with a Comparator */
		Optional optAttributeWithGreatestGain = attributeNames.stream()
				.collect(Collectors.maxBy(new Comparator<String>() {
					public int compare(String str1, String str2) {
						return Double.compare(ds.calculateGainFor(str1), ds.calculateGainFor(str2));
					}
				}));

        /* Check value is available in `Optional` */
		String attributeWithGreatestGain = optAttributeWithGreatestGain.isPresent() ?
											(String)optAttributeWithGreatestGain.get() :
											"No String found";

		return attributeWithGreatestGain;
	}

	private boolean allExamplesHaveSameClassification(DataSet ds) {
		String classification = ds.getExample(0).targetValue();
		return !ds.examples.stream()
						   .anyMatch(example -> !(example.targetValue().equals(classification)));
	}
}
