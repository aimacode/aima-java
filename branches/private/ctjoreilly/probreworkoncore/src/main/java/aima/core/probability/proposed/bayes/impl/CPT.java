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
		if (parentValues.length != parents.size()) {
			throw new IllegalArgumentException(
					"The number of parent value arguments ["
							+ parentValues.length
							+ "] is not equal to the number of parents ["
							+ parents.size() + "] for this CPT.");
		}
		AssignmentProposition[] aps = new AssignmentProposition[parentValues.length];
		int idx = 0;
		for (RandomVariable parentRV : parents) {
			aps[idx] = new AssignmentProposition(parentRV, parentValues[idx]);
			idx++;
		}

		return getConditioningCase(aps);
	}

	@Override
	public CategoricalDistribution getConditioningCase(
			AssignmentProposition... parentValues) {
		if (parentValues.length != parents.size()) {
			throw new IllegalArgumentException(
					"The number of parent value arguments ["
							+ parentValues.length
							+ "] is not equal to the number of parents ["
							+ parents.size() + "] for this CPT.");
		}
		final ProbabilityTable cc = new ProbabilityTable(getOn());
		ProbabilityTable.Iterator pti = new ProbabilityTable.Iterator() {
			private int idx = 0;

			@Override
			public void iterate(Map<RandomVariable, Object> possibleAssignment,
					double probability) {
				cc.getValues()[idx] = probability;
				idx++;
			}

			@Override
			public Object getPostIterateValue() {
				return null; // N/A
			}
		};
		table.iterateOverTable(pti, parentValues);

		return cc;
	}

	public Factor getFactorFor(final AssignmentProposition... evidence) {
		Set<RandomVariable> fofVars = new LinkedHashSet<RandomVariable>(table
				.getFor());
		for (AssignmentProposition ap : evidence) {
			fofVars.remove(ap.getTermVariable());
		}
		final ProbabilityTable fof = new ProbabilityTable(fofVars);
		// Otherwise need to iterate through the table for the
		// non evidence variables.
		final Object[] termValues = new Object[fofVars.size()];
		ProbabilityTable.Iterator di = new ProbabilityTable.Iterator() {
			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				if (0 == termValues.length) {
					fof.getValues()[0] += probability;
				} else {
					int i = 0;
					for (RandomVariable rv : fof.getFor()) {
						termValues[i] = possibleWorld.get(rv);
						i++;
					}
					fof.getValues()[fof.getIndex(termValues)] += probability;
				}
			}

			public Object getPostIterateValue() {
				return null; // N/A
			}
		};
		table.iterateOverTable(di, evidence);

		return fof;
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

		table.iterateOverTable(di);
	}
}
