package aima.logic.fol.inference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 9.3, page 282.
 * 
 * <code>
 * function FOL-FC-ASK(KB, alpha) returns a substitution or false
 *   inputs: KB, the knowledge base, a set of first order definite clauses
 *           alpha, the query, an atomic sentence
 *   local variables: new, the new sentences inferred on each iteration
 *   
 *   repeat until new is empty
 *      new <- {}
 *      for each sentence r in KB do
 *          (p1 ^ ... ^ pn => q) <- STANDARDIZE-APART(r)
 *          for each theta such that SUBST(theta, p1 ^ ... ^ pn) = SUBST(theta, p'1 ^ ... ^ p'n)
 *                         for some p'1,...,p'n in KB
 *              q' <- SUBST(theta, q)
 *              if q' is not a renaming of some sentence already in KB or new then do
 *                   add q' to new
 *                   theta <- UNIFY(q', alpha)
 *                   if theta is not fail then return theta
 *      add new to KB
 *   return false
 * </code>
 * 
 * Figure 9.3 A conceptually straightforward, but very inefficient forward-chaining algo-
 * rithm. On each iteration, it adds to KB all the atomic sentences that can be inferred in one
 * step from the implication sentences and the atomic sentences already in KB.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLFCAsk implements InferenceProcedure {

	public FOLFCAsk() {
	}

	//
	// START-InferenceProcedure

	/**
	 * <code>
	 * function FOL-FC-ASK(KB, alpha) returns a substitution or false
	 *   inputs: KB, the knowledge base, a set of first order definite clauses
	 *           alpha, the query, an atomic sentence
	 * </code>
	 */
	public Set<Map<Variable, Term>> ask(FOLKnowledgeBase KB, Sentence query) {
		// Assertions on the type of queries this Inference procedure
		// supports
		if (!Predicate.class.isInstance(query)) {
			throw new IllegalArgumentException(
					"Only Predicate Queries are supported.");
		}

		Predicate alpha = (Predicate) query;

		// local variables: new, the new sentences inferred on each iteration
		List<Predicate> newSentences = new ArrayList<Predicate>();

		// Ensure query is not already a know fact before
		// attempting forward chaining.
		Set<Map<Variable, Term>> answer = KB.fetch(alpha);
		if (answer.size() > 0) {
			return answer;
		}

		// repeat until new is empty
		do {

			// new <- {}
			newSentences.clear();
			// for each sentence r in KB do
			// (p1 ^ ... ^ pn => q) <-STANDARDIZE-APART(r)
			for (Clause impl : KB.getAllDefiniteClauseImplications()) {
				// for each theta such that SUBST(theta, p1 ^ ... ^ pn) =
				// SUBST(theta, p'1 ^ ... ^ p'n)
				// --- for some p'1,...,p'n in KB
				for (Map<Variable, Term> theta : KB.fetch(impl.getNegativeLiterals())) {
					// q' <- SUBST(theta, q)
					Predicate qDelta = (Predicate) KB.subst(theta, impl
							.getPositiveLiterals().get(0));
					// if q' is not a renaming of some sentence already in KB or
					// new then do
					if (!KB.isRenaming(qDelta)
							&& !KB.isRenaming(qDelta, newSentences)) {
						// add q' to new
						newSentences.add(qDelta);
						// theta <- UNIFY(q', alpha)
						theta = KB.unify(qDelta, alpha);
						// if theta is not fail then return theta
						if (null != theta) {
							KB.tell(newSentences);
							return KB.fetch(alpha);
						}
					}
				}
			}
			// add new to KB
			KB.tell(newSentences);
		} while (newSentences.size() > 0);

		// return false
		return new HashSet<Map<Variable, Term>>();
	}

	// END-InferenceProcedure
	//
}
