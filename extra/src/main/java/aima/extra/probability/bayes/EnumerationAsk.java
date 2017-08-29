package aima.extra.probability.bayes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import aima.extra.probability.ProbabilityComputation;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.constructs.ProbabilityUtilities;
import aima.extra.probability.domain.FiniteDomain;
import aima.extra.probability.factory.ProbabilityFactory;
import aima.extra.probability.proposition.Proposition;
import aima.extra.util.ListOps;
import aima.extra.util.MapOps;

/**
 * The enumeration algorithm for answering queries on Bayesian networks.
 * 
 * <pre>
 * function ENUMERATION-ASK(X, e, bn) returns a distribution over X
 *   inputs: X, the query variable
 *           e, observed values for variables E
 *           bn, a Bayes net with variables {X} &cup; E &cup; Y  // Y = hidden variables //
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
 * <b>Note:</b> The implementation has been extended to handle queries with
 * multiple variables. <br>
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class EnumerationAsk implements BayesInference {

	// Internal fields

	private BayesianNetwork bn;
	
	/**
	 * Class type of the ProbabilityNumber used by values.
	 */
	private Class<? extends ProbabilityNumber> clazz;

	/**
	 * ProbabilityFactory instance.
	 */
	private ProbabilityFactory<?> probFactory;
	
	// Public methods

	@Override
	public CategoricalDistribution ask(Proposition X, Proposition observedEvidence, BayesianNetwork bn) {
		// TODO Auto-generated method stub
		return null;
	}

	// function ENUMERATION-ASK(X, e, bn) returns a distribution over X
	/**
	 * The ENUMERATION-ASK algorithm evaluates expression trees using
	 * depth-first recursion.
	 * 
	 * @param scopedX
	 *            specifies query variables that are assigned specific values.
	 * @param X
	 *            the query variables (unscoped).
	 * @param observedEvidence
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y // Y = hidden
	 *            variables //
	 * 
	 * @return a distribution over the query variables.
	 */
	public CategoricalDistribution enumerationAsk(Proposition X, Proposition evidence, BayesianNetwork bn) {
		int QSize = ProbabilityUtilities.expectedSizeofProbabilityTable(X.getUnscopedTerms());
		List<ProbabilityNumber> QValues = new ArrayList<ProbabilityNumber>(
				Collections.nCopies(QSize, this.probFactory.valueOf(BigDecimal.ZERO)));
		// Q(X) <- a distribution over X, initially empty
		ProbabilityTable Q = new ProbabilityTable(X.getUnscopedTerms(), QValues, this.clazz);
		// for each value x<sub>i</sub> of X do
		// Q(x<sub>i</sub>) <- ENUMERATE-ALL(bn.VARS, e<sub>x<sub>i</sub></sub>)
		// where e<sub>x<sub>i</sub></sub> is e extended with X = x<sub>i</sub>
		Q.stream().forEach(event -> {
			Map<RandomVariable, Object> newScopedWorld = MapOps.merge(evidence.getScopedWorld(), event);
			Proposition extendedEvidence = new Proposition(evidence.getStatement().and(extendEvidence(event)),
					newScopedWorld);
			Q.modifyValue(enumerateAll(bn.getVariablesInTopologicalOrder(), extendedEvidence), event);
		});
		// return NORMALIZE(Q(X))
		return Q.normalize();
	}

	// function ENUMERATE-ALL(vars, e) returns a real number
	public ProbabilityNumber enumerateAll(List<RandomVariable> vars, Proposition e) {
		// if EMPTY?(vars) then return 1.0
		if (0 == vars.size()) {
			return this.probFactory.valueOf(1.0);
		}
		// Y <- FIRST(vars)
		RandomVariable Y = ListOps.first(vars);
		ProbabilityComputation compute = new ProbabilityComputation();
		// if Y has value y in e
		if (e.getScopedTerms().contains(Y)) {
			// then return P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e)
			return compute.mul(posteriorForParents(Y, e), enumerateAll(ListOps.rest(vars), e));
		} else {
			 //  else return &sum;<sub>y</sub> P(y | parents(Y)) * ENUMERATE-ALL(REST(vars), e<sub>y</sub>)
			 //  	where e<sub>y</sub> is e extended with Y = y
			ProbabilityNumber sum = probFactory.valueOf(0.0);
			for (Object value : ((FiniteDomain) Y.getDomain()).getPossibleValues()) {
				Map<RandomVariable, Object> event = Collections.singletonMap(Y, value);
				Map<RandomVariable, Object> newScopedWorld = MapOps.merge(e.getScopedWorld(), event);
				Proposition extendedEvidence = new Proposition(e.getStatement().and(extendEvidence(event)), newScopedWorld);
	            sum = compute.add(sum, compute.mul(posteriorForParents(Y, extendedEvidence), enumerateAll(ListOps.rest(vars), e)));
			}
			return sum;
		}
	}

	// Private methods

	/**
	 * Check if a map contains all the mappings specified in another map.
	 */
	private BiPredicate<Map<RandomVariable, Object>, Map<RandomVariable, Object>> containsAll = (superset,
			subset) -> superset.entrySet().containsAll(subset.entrySet());

	/**
	 * Extend evidence by adding random variable to value mappings.
	 * 
	 * @param event
	 *            is added to evidence.
	 * 
	 * @return check corresponding to event.
	 */
	private Predicate<Map<RandomVariable, Object>> extendEvidence(Map<RandomVariable, Object> event) {
		return mp -> containsAll.test(mp, event);
	}
	
	private ProbabilityNumber posteriorForParents(RandomVariable rv, Proposition evidence) {
		Node n = bn.getNode(rv);
		if (!(n instanceof FiniteNode)) {
			throw new IllegalArgumentException("Enumeration-Ask only works with finite Nodes.");
		}
		FiniteNode fn = (FiniteNode) n;
		Map<RandomVariable, Object> event = new HashMap<RandomVariable, Object>();
		for (Node pn : n.getParents()) {
			event.put(pn.getNodeVariable(), evidence.getScopedWorld().get(pn.getNodeVariable()));
		}
		return fn.getCPT().getValue(event);
	}
}
