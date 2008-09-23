package aima.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.DefiniteClause;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 9.6, page 288.
 * 
 * <code>
 * function FOL-BC-ASK(KB, goals, theta) returns a set of substitutions
 *   input: KB, a knowledge base
 *          goals, a list of conjuncts forming a query (theta already applied)
 *          theta, the current substitution, initially the empty substitution {}
 *   local variables: answers, a set of substitutions, initially empty
 *   
 *   if goals is empty then return {theta}
 *   qDelta <- SUBST(theta, FIRST(goals))
 *   for each sentence r in KB where STANDARDIZE-APART(r) = (p1 ^ ... ^ pn => q)
 *          and thetaDelta <- UNIFY(q, qDelta) succeeds
 *       new_goals <- [p1,...,pn|REST(goals)]
 *       answers <- FOL-BC-ASK(KB, new_goals, COMPOSE(thetaDelta, theta)) U answers
 *   return answers
 * </code>
 * 
 * Figure 9.6 A simple backward-chaining algorithm.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLBCAsk implements InferenceProcedure {

	public FOLBCAsk() {

	}

	//
	// START-InferenceProcedure
	public Set<Map<Variable, Term>> ask(FOLKnowledgeBase KB, Sentence query) {
		// Assertions on the type queries this Inference procedure
		// supports
		if (!Predicate.class.isInstance(query)) {
			throw new IllegalArgumentException(
					"Only Predicate Queries are supported.");
		}

		List<Predicate> goals = new ArrayList<Predicate>();
		goals.add((Predicate) query);

		return folbcask(KB, goals, new HashMap<Variable, Term>());
	}

	// END-InferenceProcedure
	//

	//
	// PRIVATE METHODS
	//

	/**
	 * <code>
	 * function FOL-BC-ASK(KB, goals, theta) returns a set of substitutions
	 *   input: KB, a knowledge base
	 *          goals, a list of conjuncts forming a query (theta already applied)
	 *          theta, the current substitution, initially the empty substitution {}
	 * </code>
	 */
	private Set<Map<Variable, Term>> folbcask(FOLKnowledgeBase KB,
			List<Predicate> goals, Map<Variable, Term> theta) {
		// local variables: answers, a set of substitutions, initially empty
		Set<Map<Variable, Term>> answers = new LinkedHashSet<Map<Variable, Term>>();

		// if goals is empty then return {theta}
		if (goals.isEmpty()) {
			answers.add(theta);
			return answers;
		}

		// qDelta <- SUBST(theta, FIRST(goals))
		Predicate qDelta = (Predicate) KB.subst(theta, goals.get(0));
		if (null == qDelta) {
			return answers;
		}

		// for each sentence r in KB where
		// STANDARDIZE-APART(r) = (p1 ^ ... ^ pn => q)
		for (DefiniteClause r : KB.getAllDefiniteClauses()) {
			// and thetaDelta <- UNIFY(q, qDelta) succeeds
			Map<Variable, Term> thetaDelta = KB
					.unify(r.getConclusion(), qDelta);
			if (null != thetaDelta) {
				// new_goals <- [p1,...,pn|REST(goals)]
				List<Predicate> newGoals = new ArrayList<Predicate>(r
						.getPremises());
				newGoals.addAll(goals.subList(1, goals.size()));
				// answers <- FOL-BC-ASK(KB, new_goals, COMPOSE(thetaDelta,
				// theta)) U answers
				answers.addAll(folbcask(KB, newGoals,
						compose(thetaDelta, theta)));
			}
		}

		// return answers
		return answers;
	}

	// Artificial Intelligence A Modern Approach (2nd Edition): page 288.
	// COMPOSE(theta1, theta2) is the substitution whose effect is identical to
	// the effect of applying each subsitution in turn. That is,
	// SUBST(COMPOSE(theta1, theta2), p) = SUBST(theta2, SUBST(theta1, p))
	private Map<Variable, Term> compose(Map<Variable, Term> theta1,
			Map<Variable, Term> theta2) {
		Map<Variable, Term> composed = new HashMap<Variable, Term>(theta1);
		composed.putAll(theta2);

		// So that it behaves like:
		// SUBST(theta2, SUBST(theta1, p))
		// Need to handle a situation like this:
		// {x=John, v1=x}
		// in this case want v1=x to be:
		// v1=John.
		for (Variable v : composed.keySet()) {
			Term t = composed.get(v);
			if (composed.keySet().contains(t)) {
				composed.put(v, composed.get(t));
			}
		}

		return composed;
	}
}
