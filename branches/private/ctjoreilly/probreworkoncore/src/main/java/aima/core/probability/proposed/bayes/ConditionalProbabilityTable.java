package aima.core.probability.proposed.bayes;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.ProbabilityModel;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.domain.FiniteDomain;
import aima.core.probability.proposed.proposition.AssignmentProposition;
import aima.core.probability.proposed.util.ProbabilityTable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 512.<br>
 * <br>
 * A Conditional Probability Table, or CPT, can be used for representing
 * conditional probabilities for discrete random variables. Each row in a CPT
 * contains the conditional probability of each node value for a <b>conditioning
 * case</b>. A conditioning case is just a possible combination of values for
 * the parent nodes - a miniature possible world, if you like. Each row must sum
 * to 1, because the entries represent an exhaustive set of cases for the random
 * variable.
 * 
 * @author Ciaran O'Reilly
 */
public class ConditionalProbabilityTable {

	private ProbabilityTable table = null;
	private List<Object> varDomain = new ArrayList<Object>();

	public ConditionalProbabilityTable(RandomVariable var, double[] values,
			RandomVariable... conditionedOn) {
		if (null == conditionedOn) {
			conditionedOn = new RandomVariable[0];
		}
		RandomVariable[] tableVars = new RandomVariable[conditionedOn.length + 1];
		for (int i = 0; i < conditionedOn.length; i++) {
			tableVars[i] = conditionedOn[i];
		}
		tableVars[conditionedOn.length] = var;
		table = new ProbabilityTable(values, tableVars);
		varDomain.addAll(((FiniteDomain) var.getDomain()).getPossibleValues());

		checkEachRowTotalsOne();
	}

	public boolean contains(RandomVariable rv) {
		return table.contains(rv);
	}

	public double probabilityFor(final AssignmentProposition... values) {
		return table.getValue(values);
	}

	public ProbabilityTable valueOf(final AssignmentProposition... values) {
		Set<RandomVariable> vofVars = new LinkedHashSet<RandomVariable>(
				table.getFor());
		for (AssignmentProposition ap : values) {
			vofVars.remove(ap.getTermVariable());
		}
		final ProbabilityTable valueOf = new ProbabilityTable(vofVars);
		// Otherwise need to iterate through this distribution
		// to calculate the summed out distribution.
		final Object[] termValues = new Object[vofVars.size()];
		ProbabilityTable.Iterator di = new ProbabilityTable.Iterator() {
			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				boolean holds = true;
				for (AssignmentProposition ap : values) {
					if (!ap.holds(possibleWorld)) {
						holds = false;
						break;
					}
				}
				if (holds) {
					if (0 == termValues.length) {
						valueOf.getValues()[0] += probability;
					} else {
						int i = 0;
						for (RandomVariable rv : valueOf.getFor()) {
							termValues[i] = possibleWorld.get(rv);
							i++;
						}
						valueOf.getValues()[valueOf.getIndex(termValues)] += probability;
					}
				}
			}

			public Object getPostIterateValue() {
				return null; // N/A
			}
		};
		table.iterateDistribution(di);

		return valueOf;
	}

	//
	// PRIVATE METHODS
	//
	private void checkEachRowTotalsOne() {
		ProbabilityTable.Iterator di = new ProbabilityTable.Iterator() {
			private int rowSize = varDomain.size();
			private int iterateCnt = 0;
			private double rowProb = 0;

			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				iterateCnt++;
				rowProb += probability;
				if (iterateCnt % rowSize == 0) {
					if (Math.abs(1 - rowProb) > ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD) {
						throw new IllegalArgumentException("Row "
								+ (iterateCnt / rowSize)
								+ " of CPT does not sum to 1.0.");
					}
					rowProb = 0;
				}
			}

			public Object getPostIterateValue() {
				return null; // N/A
			}
		};

		table.iterateDistribution(di);
	}
}
