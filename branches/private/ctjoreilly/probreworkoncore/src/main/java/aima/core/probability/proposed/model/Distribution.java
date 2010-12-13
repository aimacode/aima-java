package aima.core.probability.proposed.model;

import java.util.LinkedHashSet;

import aima.core.probability.proposed.model.proposition.TermProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 487.
 * 
 * A vector of numbers, where we assume a predefined ordering on the domain(s)
 * of the Term Proposition(s) used to create the distribution.
 * 
 * This class is used to represent both Probability and Joint Probability
 * distributions for finite domains.
 * 
 * @author oreilly
 */
public class Distribution {
	private double[] distribution = null;
	private LinkedHashSet<TermProposition> propositions = new LinkedHashSet<TermProposition>();
	//
	private String toString = null;

	public Distribution(double[] values, TermProposition... props) {
		if (null == values) {
			throw new IllegalArgumentException(
					"Distribution values must be specified");
		}
		if (null == props) {
			throw new IllegalArgumentException("Term propositions must be specified.");
		}
		for (TermProposition t : props) {
			for (RandomVariable rvt : t.getScope()) {
				if (rvt.getDomain().isInfinite()) {
					throw new IllegalArgumentException(
							"Cannot have an infinite domain for a variable in a Distribution:" + rvt);
				}
			}

			
		}
		
		// TODO - create ordered domains for each proposition that is not a singular assignment
		// TODO - propositions with scope().size() > 1 will map to the TRUE/FALSE domain.
		//        document difference between Total and Double variables and Dice1 Dice2 examples.

		distribution = new double[values.length];
		System.arraycopy(values, 0, distribution, 0, values.length);
	}

	public double[] getValues() {
		return distribution;
	}
	
	public double getValueAt(Object... vals) {
		// TODO - create API getValueAt() which takes a list of values to index a particular
		//        probability
		return 0;
	}

	@Override
	public String toString() {
		if (null == toString) {
			StringBuilder sb = new StringBuilder();
			sb.append("<");
			for (int i = 0; i < distribution.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				sb.append(distribution[i]);
			}
			sb.append(">");

			toString = sb.toString();
		}
		return toString;
	}
}