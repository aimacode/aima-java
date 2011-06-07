package aima.core.probability.proposed.bayes.exact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aima.core.probability.proposed.CategoricalDistribution;
import aima.core.probability.proposed.Factor;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.bayes.BayesInference;
import aima.core.probability.proposed.bayes.BayesianNetwork;
import aima.core.probability.proposed.bayes.FiniteNode;
import aima.core.probability.proposed.bayes.Node;
import aima.core.probability.proposed.proposition.AssignmentProposition;
import aima.core.probability.proposed.util.ProbabilityTable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 14.11, page
 * 528.<br>
 * <br>
 * 
 * <pre>
 * function ELIMINATION-ASK(X, e, bn) returns a distribution over X
 *   inputs: X, the query variable
 *           e, observed values for variables E
 *           bn, a Bayesian network specifying joint distribution P(X<sub>1</sub>, ..., X<sub>n</sub>)
 *   
 *   factors <- []
 *   for each var in ORDER(bn.VARS) do
 *       factors <- [MAKE-FACTOR(var, e) | factors]
 *       if var is hidden variable the factors <- SUM-OUT(var, factors)
 *   return NORMALIZE(POINTWISE-PRODUCT(factors))
 * </pre>
 * 
 * Figure 14.11 The variable elimination algorithm for inference in Bayesian
 * networks. <br>
 * <br>
 * <b>Note:</b> The implementation has been extended to handle queries with
 * multiple variables. <br>
 * 
 * @author Ciaran O'Reilly
 */
public class EliminationAsk implements BayesInference {
	//
	private static final ProbabilityTable _identity = new ProbabilityTable(
			new double[] { 1.0 });

	public EliminationAsk() {

	}

	// function ELIMINATION-ASK(X, e, bn) returns a distribution over X
	/**
	 * The ELIMINATION-ASK algorithm in Figure 14.11.
	 * 
	 * @param X
	 *            the query variables.
	 * @param e
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables //
	 * @return a distribution over the query variables.
	 */
	public CategoricalDistribution eliminationAsk(final RandomVariable[] X,
			final AssignmentProposition[] e, final BayesianNetwork bn) {

		Set<RandomVariable> hidden = calculateHiddenVariables(X, e, bn);

		// factors <- []
		List<Factor> factors = new ArrayList<Factor>();
		// for each var in ORDER(bn.VARS) do
		for (RandomVariable var : order(bn.getVariablesInTopologicalOrder())) {
			// factors <- [MAKE-FACTOR(var, e) | factors]
			factors.add(0, makeFactor(var, e, bn));
			// if var is hidden variable then factors <- SUM-OUT(var, factors)
			if (hidden.contains(var)) {
				factors = sumOut(var, factors, bn);
			}
		}
		// return NORMALIZE(POINTWISE-PRODUCT(factors))
		Factor product = pointwiseProduct(factors);
		// Note: Want to ensure the order of the product matches the
		// query variables
		return ((ProbabilityTable) product.pointwiseProductPOS(_identity, X))
				.normalize();
	}

	//
	// START-BayesInference
	public CategoricalDistribution ask(final RandomVariable[] X,
			final AssignmentProposition[] observedEvidence,
			final BayesianNetwork bn) {
		return this.eliminationAsk(X, observedEvidence, bn);
	}

	// END-BayesInference
	//

	//
	// PRIVATE METHODS
	//
	private Set<RandomVariable> calculateHiddenVariables(
			final RandomVariable[] X, final AssignmentProposition[] e,
			final BayesianNetwork bn) {
		Set<RandomVariable> hidden = new HashSet<RandomVariable>(bn
				.getVariablesInTopologicalOrder());
		for (RandomVariable x : X) {
			hidden.remove(x);
		}
		for (AssignmentProposition ap : e) {
			hidden.removeAll(ap.getScope());
		}

		return hidden;
	}

	private List<RandomVariable> order(Collection<RandomVariable> vars) {
		// For simplicity just return in the reverse order received,
		// i.e. received will be the default topological order for
		// the Bayesian Network and we want to ensure the network
		// is iterated from bottom up to ensure when hidden variables
		// are come across all the factors dependent on them have
		// been seen so far.
		List<RandomVariable> order = new ArrayList<RandomVariable>(vars);
		Collections.reverse(order);

		return order;
	}

	private Factor makeFactor(RandomVariable var, AssignmentProposition[] e,
			BayesianNetwork bn) {

		Node n = bn.getNode(var);
		if (!(n instanceof FiniteNode)) {
			throw new IllegalArgumentException(
					"Elimination-Ask only works with finite Nodes.");
		}
		FiniteNode fn = (FiniteNode) n;
		List<AssignmentProposition> evidence = new ArrayList<AssignmentProposition>();
		for (AssignmentProposition ap : e) {
			if (fn.getCPT().contains(ap.getTermVariable())) {
				evidence.add(ap);
			}
		}

		return fn.getCPT().getFactorFor(
				evidence.toArray(new AssignmentProposition[evidence.size()]));
	}

	private List<Factor> sumOut(RandomVariable var, List<Factor> factors,
			BayesianNetwork bn) {
		List<Factor> summedOutFactors = new ArrayList<Factor>();
		List<Factor> toMultiply = new ArrayList<Factor>();
		for (Factor f : factors) {
			if (f.contains(var)) {
				toMultiply.add(f);
			} else {
				// This factor does not contain the variable
				// so no need to sum out - see AIMA3e pg. 527.
				summedOutFactors.add(f);
			}
		}

		summedOutFactors.add(pointwiseProduct(toMultiply).sumOut(var));

		return summedOutFactors;
	}

	public Factor pointwiseProduct(List<Factor> factors) {

		Factor product = factors.get(0);
		for (int i = 1; i < factors.size(); i++) {
			product = product.pointwiseProduct(factors.get(i));
		}

		return product;
	}
}