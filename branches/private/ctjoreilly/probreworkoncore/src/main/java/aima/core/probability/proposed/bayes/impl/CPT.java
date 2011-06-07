package aima.core.probability.proposed.bayes.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.CategoricalDistribution;
import aima.core.probability.proposed.Factor;
import aima.core.probability.proposed.ProbabilityModel;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.bayes.ConditionalProbabilityTable;
import aima.core.probability.proposed.domain.FiniteDomain;
import aima.core.probability.proposed.proposition.AssignmentProposition;
import aima.core.probability.proposed.util.ProbabilityTable;

/**
 * Default implementation of the ConditionalProbabilityTable interface.
 * 
 * @author Ciaran O'Reilly
 *
 */
public class CPT implements ConditionalProbabilityTable {
	private RandomVariable on = null;
	private LinkedHashSet<RandomVariable> parents = new LinkedHashSet<RandomVariable>();
	private ProbabilityTable table = null;
	private List<Object> onDomain = new ArrayList<Object>();

	public CPT(RandomVariable on, double[] values,
			RandomVariable... conditionedOn) {
		this.on = on;
		if (null == conditionedOn) {
			conditionedOn = new RandomVariable[0];
		}
		RandomVariable[] tableVars = new RandomVariable[conditionedOn.length + 1];
		for (int i = 0; i < conditionedOn.length; i++) {
			tableVars[i] = conditionedOn[i];
			parents.add(conditionedOn[i]);
		}
		tableVars[conditionedOn.length] = on;
		table = new ProbabilityTable(values, tableVars);
		onDomain.addAll(((FiniteDomain) on.getDomain()).getPossibleValues());

		checkEachRowTotalsOne();
	}

	public double probabilityFor(final Object... values) {
		return table.getValue(values);
	}

	//
	// START-ConditionalProbabilityDistribution

	@Override
	public RandomVariable getOn() {
		return on;
	}

	@Override
	public Set<RandomVariable> getParents() {
		return parents;
	}

	@Override
	public Set<RandomVariable> getFor() {
		return table.getFor();
	}

	@Override
	public boolean contains(RandomVariable rv) {
		return table.contains(rv);
	}

	@Override
	public double getValue(Object... eventValues) {
		return table.getValue(eventValues);
	}

	@Override
	public double getValue(AssignmentProposition... eventValues) {
		return table.getValue(eventValues);
	}

	// END-ConditionalProbabilityDistribution
	//

	//
	// START-ConditionalProbabilityTable
	@Override
	public CategoricalDistribution getConditioningCase(Object... parentValues) {
		// TODO
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public CategoricalDistribution getConditioningCase(
			AssignmentProposition... parentValues) {
		// TODO
		throw new UnsupportedOperationException("TODO");
	}

	public Factor getFactorFor(final AssignmentProposition... evidence) {
		// TODO - a more efficient implementation of this
		// as I should not need to iterate over all the
		// possible worlds, just those matching the evidence
		// Note: Could do with an additional itereate(Iterator,
		// AssignmentProposition...) on ProbabilityTable.
		Set<RandomVariable> vofVars = new LinkedHashSet<RandomVariable>(table
				.getFor());
		for (AssignmentProposition ap : evidence) {
			vofVars.remove(ap.getTermVariable());
		}
		final ProbabilityTable valueOf = new ProbabilityTable(vofVars);
		// Otherwise need to iterate through this distribution
		// to calculate the Factor.
		final Object[] termValues = new Object[vofVars.size()];
		ProbabilityTable.Iterator di = new ProbabilityTable.Iterator() {
			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				boolean holds = true;
				for (AssignmentProposition ap : evidence) {
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

	// END-ConditionalProbabilityTable
	//

	//
	// PRIVATE METHODS
	//
	private void checkEachRowTotalsOne() {
		ProbabilityTable.Iterator di = new ProbabilityTable.Iterator() {
			private int rowSize = onDomain.size();
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
