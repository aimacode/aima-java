package aima.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.parsing.ast.AtomicSentence;
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
		if (!(query instanceof AtomicSentence)) {
			throw new IllegalArgumentException(
					"Only Atomic Queries are supported.");
		}

		List<Literal> goals = new ArrayList<Literal>();
		goals.add(new Literal((AtomicSentence) query));

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
			List<Literal> goals, Map<Variable, Term> theta) {
		// local variables: answers, a set of substitutions, initially empty
		Set<Map<Variable, Term>> answers = new LinkedHashSet<Map<Variable, Term>>();

		// if goals is empty then return {theta}
		if (goals.isEmpty()) {
			answers.add(theta);
			return answers;
		}

		// qDelta <- SUBST(theta, FIRST(goals))
		Literal qDelta = (Literal) KB.subst(theta, goals.get(0));
		if (null == qDelta) {
			return answers;
		}

		// for each sentence r in KB where
		// STANDARDIZE-APART(r) = (p1 ^ ... ^ pn => q)
		for (Clause r : KB.getAllDefiniteClauses()) {
			r = KB.standardizeApart(r);
			// and thetaDelta <- UNIFY(q, qDelta) succeeds
			Map<Variable, Term> thetaDelta = KB
					.unify(r.getPositiveLiterals()
					.get(0).getAtomicSentence(), qDelta.getAtomicSentence());
			if (null != thetaDelta) {
				// new_goals <- [p1,...,pn|REST(goals)]
				List<Literal> newGoals = new ArrayList<Literal>(r
						.getNegativeLiterals());
				newGoals.addAll(goals.subList(1, goals.size()));
				// answers <- FOL-BC-ASK(KB, new_goals, COMPOSE(thetaDelta,
				// theta)) U answers
				answers.addAll(folbcask(KB, newGoals,
						compose(KB, thetaDelta,
						theta)));
			}
		}

		// return answers
		return answers;
	}

	// Artificial Intelligence A Modern Approach (2nd Edition): page 288.
	// COMPOSE(delta, tau) is the substitution whose effect is identical to
	// the effect of applying each substitution in turn. That is,
	// SUBST(COMPOSE(theta1, theta2), p) = SUBST(theta2, SUBST(theta1, p))
	private Map<Variable, Term> compose(FOLKnowledgeBase KB,
			Map<Variable, Term> theta1,
			Map<Variable, Term> theta2) {
		Map<Variable, Term> composed = new HashMap<Variable, Term>();

		// So that it behaves like:
		// SUBST(theta2, SUBST(theta1, p))
		// There are two steps involved here.
		// See: http://logic.stanford.edu/classes/cs157/2008/notes/chap09.pdf
		// for a detailed discussion:

		// 1. Apply theta2 to the range of theta1.
		for (Variable v : theta1.keySet()) {
			composed.put(v, KB.subst(theta2, theta1.get(v)));
		}

		// 2. Adjoin to delta all pairs from tau with different
		// domain variables.
		for (Variable v : theta2.keySet()) {
			if (!theta1.containsKey(v)) {
				composed.put(v, theta2.get(v));
			}
		}

		return cascadeSubstitutions(KB, composed);
	}

	// See:
	// http://logic.stanford.edu/classes/cs157/2008/miscellaneous/faq.html#jump165
	// for need for this.
	private Map<Variable, Term> cascadeSubstitutions(FOLKnowledgeBase KB,
			Map<Variable, Term> theta) {
		for (Variable v : theta.keySet()) {
			Term t = theta.get(v);
			theta.put(v, KB.subst(theta, t));
		}

		return theta;
	}
}
