package aima.core.probability.proposed.model.impl.bayes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.ProbabilityModel;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.FiniteDiscreteDomain;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 512.
 * 
 * A Conditional Probability Table, or CPT, can be used for representing
 * conditional probabilities for discrete random variables. Each row in a CPT
 * contains the conditional probability of each node value for a conditioning
 * case. A conditioning case is just a possible combination of values for the
 * parent nodes - a miniature possible world, if you like. Each row must sum to
 * 1, because the entries represent an exhaustive set of cases for the random
 * variable.
 * 
 * @author Ciaran O'Reilly
 */
public class ConditionalProbabilityTable {

	private Distribution table = null;
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
		table = new Distribution(values, tableVars);
		varDomain.addAll(((FiniteDiscreteDomain) var.getDomain())
				.getPossibleValues());
		
		checkEachRowTotalsOne();
	}

	//
	// PRIVATE METHODS
	//
	private void checkEachRowTotalsOne() {
		Distribution.Iterator di = new Distribution.Iterator() {
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
