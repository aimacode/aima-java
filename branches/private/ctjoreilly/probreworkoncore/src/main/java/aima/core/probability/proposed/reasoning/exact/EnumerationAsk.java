package aima.core.probability.proposed.reasoning.exact;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.probability.proposed.model.Distribution;
import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.impl.bayes.BayesianNetwork;
import aima.core.probability.proposed.model.proposition.AssignmentProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 14.9, page
 * 525.
 * 
 * <pre>
 * function ENUMERATION-ASK(X, e, bn) returns a distribution over X
 *   inputs: X, the query variable
 *           e, observed values for variables E
 *           bn, a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden variables //
 *           
 *   Q(X) <- a distribution over X, initially empty
 *   for each value x<sub>i</sub> of X do
 *       Q(x<sub>i</sub>) <- ENUMERATE-ALL(bn.VARS, e<sub>x<sub>i</sub></sub>)
 *          where e<sub>x<sub>i</sub></sub> is e extended with X = x<sub>i</sub>
 *   return NORMALIZE(Q(X))
 *   
 * ---------------------------------------------------------------------------------------------------
 * 
 * function ENUMERATE-ALL(vars, e) returns a real number
 *   if EMPTY?(vars) then return 1.0
 *   Y <- FIRST(vars)
 *   if Y has value y in e
 *       then return P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e)
 *       else return &sum;<sub>y</sub> P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e<sub>y</sub>)
 *           where e<sub>y</sub> is e extended with Y = y
 * </pre>
 * 
 * Figure 14.9 The enumeration algorithm for answering queries on Bayesian
 * networks. <br>
 * <br>
 * Note: The implementation has been extended to handle queries with multiple
 * variables. <br>
 * 
 * @author Ciaran O'Reilly
 */
public class EnumerationAsk {

	public EnumerationAsk() {

	}

	// function ENUMERATION-ASK(X, e, bn) returns a distribution over X
	/**
	 * The ENUMERATION-ASK algorithm in Figure 14.9 evaluates expression trees
	 * (Figure 14.8) using depth-first recursion.
	 * 
	 * @param queryVariables
	 *            the query variables.
	 * @param observedEvidence
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables //
	 * @return a distribution over the query variables.
	 */
	public Distribution enumerationAsk(final RandomVariable[] queryVariables,
			final AssignmentProposition[] observedEvidence,
			final BayesianNetwork bn) {

		// Q(X) <- a distribution over X, initially empty
		final Distribution Q = new Distribution(queryVariables);
		final ObservedEvidence e = new ObservedEvidence(queryVariables,
				observedEvidence, bn);
		// for each value x<sub>i</sub> of X do
		Distribution.Iterator di = new Distribution.Iterator() {
			int cnt = 0;

			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				/**
				 * <pre>
				 * Q(x<sub>i</sub>) <- ENUMERATE-ALL(bn.VARS, e<sub>x<sub>i</sub></sub>)
				 *   where e<sub>x<sub>i</sub></sub> is e extended with X = x<sub>i</sub>
				 * </pre>
				 */
				for (int i = 0; i < queryVariables.length; i++) {
					e.value[i] = possibleWorld.get(queryVariables[i]);
				}
				Q.getValues()[cnt] = enumerateAll(bn.getVariables(), e);
				cnt++;
			}

			public Object getPostIterateValue() {
				return null; // N/A
			}
		};
		Q.iterateDistribution(di);

		// return NORMALIZE(Q(X))
		return Q.normalize();
	}

	//
	// PROTECTED METHODS
	//
	protected class ObservedEvidence {
		public Object[] value = null;
		public RandomVariable[] var = null;
		public int hiddenIdx = 0;
		public Map<RandomVariable, Integer> varIdxs = new HashMap<RandomVariable, Integer>();

		public ObservedEvidence(RandomVariable[] queryVariables,
				AssignmentProposition[] e, BayesianNetwork bn) {
			int maxSize = bn.getVariables().size();
			value = new Object[maxSize];
			var = new RandomVariable[maxSize];
			// query variables go first
			int idx = 0;
			for (int i = 0; i < queryVariables.length; i++) {
				var[idx] = queryVariables[i];
				varIdxs.put(var[idx], idx);
				idx++;
			}
			// initial evidence variables go next
			for (int i = 0; i < e.length; i++) {
				var[idx] = e[i].getRandomVariable();
				varIdxs.put(var[idx], idx);
				value[idx] = e[i].getValue();
				idx++;
			}
			// the remaining slots are left open for the hidden variables
			hiddenIdx = idx;
			for (RandomVariable rv : bn.getVariables()) {
				if (!varIdxs.containsKey(rv)) {
					var[idx] = rv;
					varIdxs.put(var[idx], idx);
					idx++;
				}
			}
		}

		public boolean containsValue(RandomVariable rv) {
			return varIdxs.get(rv) < hiddenIdx;
		}
	}

	// function ENUMERATE-ALL(vars, e) returns a real number
	protected double enumerateAll(List<RandomVariable> vars, ObservedEvidence e) {
		// if EMPTY?(vars) then return 1.0
		if (0 == vars.size()) {
			e.hiddenIdx--;
			return 1;
		}
		// Y <- FIRST(vars)
		RandomVariable Y = vars.get(0);
		// if Y has value y in e
		if (e.containsValue(Y)) {
			// then return P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e)
			e.hiddenIdx++;
			e.hiddenIdx--;
			return 0; // TODO
		}
		/**
		 * <pre>
		 *  else return &sum;<sub>y</sub> P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e<sub>y</sub>)
		 *       where e<sub>y</sub> is e extended with Y = y
		 * </pre>
		 */
		e.hiddenIdx--;
		return 0;
	}

	//
	// PRIVATE METHODS
	//
}