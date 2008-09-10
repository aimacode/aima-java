package aima.logic.fol.algorithms;

import java.util.List;
import java.util.Map;

import aima.logic.fol.kb.FOLKnowledgeBase;
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
public class FOLFowardChainAsk {

	/**
	 * <code>
	 * function FOL-FC-ASK(KB, alpha) returns a substitution or false
	 *   inputs: KB, the knowledge base, a set of first order definite clauses
	 *           alpha, the query, an atomic sentence
	 * </code>
	 */
	public List<Map<Variable, Term>> ask(FOLKnowledgeBase kb, Sentence query) {
		// local variables: new, the new sentences inferred on each iteration
		return null; // TODO
	}
}
