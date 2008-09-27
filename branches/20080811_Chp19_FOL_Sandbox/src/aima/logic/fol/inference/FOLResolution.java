package aima.logic.fol.inference;

import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 297.
 * 
 * The algorithmic approach is identical to the propositional case, described
 * in Figure 7.12:
 * <code>
 * function FOL-RESOLUTION(KB, alpha) returns true or false
 *   inputs: KB, the knowledge base, a sentence in first order logic
 *           alpha, the query, a sentence in first order logic
 *           
 *   clauses <- the set of clauses in CNF representation of KB ^ ~alpha
 *   new <- {}
 *   loop do
 *      for each Ci, Cj in clauses do
 *          resolvents <- FOL-RESOLVE(Ci, Cj)
 *          if resolvents contains the empty clause then return true
 *          new <- new <UNION> resolvents
 *      if new <SUBSET> clauses then return false
 *      clauses <- clauses <UNION> new
 * </code>
 * 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLResolution implements InferenceProcedure {

	//
	// START-InferenceProcedure
	/**
	 * <code>
	 * function FOL-RESOLUTION(KB, alpha) returns true or false 
	 *   inputs: KB, the knowledge base, a sentence in first order logic 
	 *           alpha, the query, a sentence in first order logic
	 * </code>
	 */
	public Set<Map<Variable, Term>> ask(FOLKnowledgeBase KB, Sentence alpha) {
		// clauses <- the set of clauses in CNF representation of KB ^ ~alpha

		return null; // TODO
	}
	// END-InferenceProcedure
	// 

	//
	// PRIVATE METHODS
	//
}
