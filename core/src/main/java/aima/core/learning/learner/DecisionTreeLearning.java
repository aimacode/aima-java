package aima.core.learning.learners;

import java.util.Iterator;
import java.util.List;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.ConstantDecisonTree;
import aima.core.learning.inductive.DecisionTree;
import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Mayank Kumar
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
	
		// Get names of attributes
		List<String> attributes = ds.getNonTargetAttributes();
		
		// Learn the decision tree
		this.tree = decisionTreeLearning(ds, attributes,
				new ConstantDecisonTree(defaultValue));
	}

	@Override
	public String predict(Example e) {
		// Use the decision tree to predict data label
		return (String) tree.predict(e);
	}

	@Override
	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Example e : ds.examples) {
		
			// If predicted label matches the original label
			if (e.targetValue().equals(tree.predict(e))) {
				results[0] = results[0] + 1;
				
			// If predicted label does not match the original label
			} else {
				results[1] = results[1] + 1;
			}
		}
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
			
		// If the dataset is empty
		if (ds.size() == 0) {
			return defaultTree;
		}
		
		// If the dataset is completely skewed i.e. all examples have same labels
		if (allExamplesHaveSameClassification(ds)) {
			return new ConstantDecisonTree(ds.getExample(0).targetValue());
		}
		
		// If no attribute info is given, only the data labels. This chooses the data label occuring the most number of times and returns it
		if (attributeNames.size() == 0) {
			return majorityValue(ds);
		}
		
		// Select the attribute with the highest information gain 
		String chosenAttribute = chooseAttribute(ds, attributeNames);

		DecisionTree tree = new DecisionTree(chosenAttribute);
		ConstantDecisonTree m = majorityValue(ds);

		// Split the remaining dataset into different classes based on the chosenAttribute 
		List<String> values = ds.getPossibleAttributeValues(chosenAttribute);
		for (String v : values) {
			DataSet filtered = ds.matchingDataSet(chosenAttribute, v);
			List<String> newAttribs = Util.removeFrom(attributeNames,
					chosenAttribute);
			DecisionTree subTree = decisionTreeLearning(filtered, newAttribs, m);
			tree.addNode(v, subTree);

		}

		return tree;
	}

	// Use the majority learner class to get the data labels occuring the most frequent
	private ConstantDecisonTree majorityValue(DataSet ds) {
		Learner learner = new MajorityLearner();
		learner.train(ds);
		return new ConstantDecisonTree(learner.predict(ds.getExample(0)));
	}

	// Find out the attribute with the highest information gain, to further split the decision tree
	private String chooseAttribute(DataSet ds, List<String> attributeNames) {
		double greatestGain = 0.0;
		String attributeWithGreatestGain = attributeNames.get(0);
		for (String attr : attributeNames) {
		
			// Calculate information gain corresponding to each attribute 
			double gain = ds.calculateGainFor(attr);
			if (gain > greatestGain) {
				greatestGain = gain;
				attributeWithGreatestGain = attr;
			}
		}

		return attributeWithGreatestGain;
	}

	// Check if all examples have the same label or not 
	private boolean allExamplesHaveSameClassification(DataSet ds) {
		String classification = ds.getExample(0).targetValue();
		Iterator<Example> iter = ds.iterator();
		while (iter.hasNext()) {
			Example element = iter.next();
			if (!(element.targetValue().equals(classification))) {
				return false;
			}

		}
		return true;
	}
}
