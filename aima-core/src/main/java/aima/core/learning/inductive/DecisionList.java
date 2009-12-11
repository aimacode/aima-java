package aima.core.learning.inductive;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.learning.framework.Example;

/**
 * @author Ravi Mohan
 * 
 */
public class DecisionList {
	private String positive, negative;

	private List<DLTest> tests;

	private Hashtable<DLTest, String> testOutcomes;

	public DecisionList(String positive, String negative) {
		this.positive = positive;
		this.negative = negative;
		this.tests = new ArrayList<DLTest>();
		testOutcomes = new Hashtable<DLTest, String>();
	}

	public String predict(Example example) {
		if (tests.size() == 0) {
			return negative;
		}
		for (DLTest test : tests) {
			if (test.matches(example)) {
				return testOutcomes.get(test);
			}
		}
		return negative;
	}

	public void add(DLTest test, String outcome) {
		tests.add(test);
		testOutcomes.put(test, outcome);
	}

	public DecisionList mergeWith(DecisionList dlist2) {
		DecisionList merged = new DecisionList(positive, negative);
		for (DLTest test : tests) {
			merged.add(test, testOutcomes.get(test));
		}
		for (DLTest test : dlist2.tests) {
			merged.add(test, dlist2.testOutcomes.get(test));
		}
		return merged;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (DLTest test : tests) {
			buf.append(test.toString() + " => " + testOutcomes.get(test)
					+ " ELSE \n");
		}
		buf.append("END");
		return buf.toString();
	}
}
