package aima.core.learning.learners;

import java.util.*;

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
public class DecisionTreeLearner extends SizeSpecifiedLearner {
	private DecisionTree tree;
	private String defaultValue;

	public DecisionTreeLearner() {
		super();
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
	 * List<String> attributes = ds.getNonTargetAttributes();
		this.tree = decisionTreeLearning(ds, attributes,
				new ConstantDecisonTree(defaultValue));
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
	public void train(int size, DataSet ds) {
		List<String> attributes = ds.getNonTargetAttributes();
		this.tree = decisionTreeLearningBFS(ds, size, attributes,
				new ConstantDecisonTree(defaultValue));
	}

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
		for (String v : values) {
			DataSet filtered = ds.matchingDataSet(chosenAttribute, v);
			List<String> newAttribs = Util.removeFrom(attributeNames,
					chosenAttribute);
			DecisionTree subTree = decisionTreeLearning(filtered, newAttribs, m);
			tree.addNode(v, subTree);

		}

		return tree;
	}

	private ConstantDecisonTree createConstantDecisonTree(DataSet ds,
														  List<String> attributeNames,
														  ConstantDecisonTree defaultTree) {
		if (ds.size() == 0) {
			return defaultTree;
		}
		if (allExamplesHaveSameClassification(ds)) {
			return new ConstantDecisonTree(ds.getExample(0).targetValue());
		}
		if (attributeNames.size() == 0) {
			return majorityValue(ds);
		}
		return null;
	}

	private class DecTreeWithDS {
		private DataSet ds;
		private DecisionTree dt;

		public DecTreeWithDS(DataSet ds, DecisionTree dt) {
			this.ds = ds;
			this.dt = dt;
		}
	}

	//with a size limitation on the decision tree, create tree using bfs rather than dfs
	private DecisionTree decisionTreeLearningBFS(DataSet ds, int maxSize,
												 List<String> attributeNames,
												 ConstantDecisonTree defaultTree) {
		int numNodes = 0;

		if (createConstantDecisonTree(ds, attributeNames, defaultTree) != null)
			return createConstantDecisonTree(ds, attributeNames, defaultTree);

		String chosenAttribute = chooseAttribute(ds, attributeNames);
		DecisionTree newTree = new DecisionTree(chosenAttribute);
		numNodes++;

		Queue<DecTreeWithDS> decisionTrees = new LinkedList<>();
		decisionTrees.add(new DecTreeWithDS(ds, newTree));
		ConstantDecisonTree m = majorityValue(ds);
		attributeNames.remove(chosenAttribute);

		while (numNodes < maxSize && !decisionTrees.isEmpty()) {
			DecTreeWithDS decTreeWithDS = decisionTrees.remove();
			DecisionTree currTree = decTreeWithDS.dt;
			DataSet currDS = decTreeWithDS.ds;

			String currAttribute = currTree.getAttributeName();

			List<String> values = currDS.getPossibleAttributeValues(currAttribute);
			for (String v : values) {

				if (numNodes >= maxSize)
					return newTree;

				DataSet filtered = currDS.matchingDataSet(currAttribute, v);

				ConstantDecisonTree subTree = createConstantDecisonTree(filtered, attributeNames, m);
				if (subTree != null) {
					currTree.addNode(v, subTree);
					numNodes++;
				}
				else {
					String newAttribute = chooseAttribute(filtered, attributeNames);
					DecisionTree toProcess = new DecisionTree(newAttribute);
					attributeNames.remove(newAttribute);
					currTree.addNode(v, toProcess);
					decisionTrees.add(new DecTreeWithDS(filtered, toProcess));
					numNodes++;
				}
			}

		}
		return newTree;

	}

	@Override
	public String predict(Example e) {
		return (String) tree.predict(e);
	}

	@Override
	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Example e : ds.examples) {
			if (e.targetValue().equals(tree.predict(e))) {
				results[0] = results[0] + 1;
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

	private ConstantDecisonTree majorityValue(DataSet ds) {
		Learner learner = new MajorityLearner();
		learner.train(ds);
		return new ConstantDecisonTree(learner.predict(ds.getExample(0)));
	}

	private String chooseAttribute(DataSet ds, List<String> attributeNames) {
		double greatestGain = 0.0;
		String attributeWithGreatestGain = attributeNames.get(0);
		for (String attr : attributeNames) {
			double gain = ds.calculateGainFor(attr);
			if (gain > greatestGain) {
				greatestGain = gain;
				attributeWithGreatestGain = attr;
			}
		}

		return attributeWithGreatestGain;
	}

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
