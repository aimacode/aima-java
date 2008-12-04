package aima.logic.fol.inference;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 9.14, page 307.
 * 
 * <code>
 * procedure OTTER(sos, usable)
 *   inputs: sos, a set of support-clauses defining the problem (a global variable)
 *   usable, background knowledge potentially relevant to the problem
 *   
 *   repeat
 *      clause <- the lightest member of sos
 *      move clause from sos to usable
 *      PROCESS(INFER(clause, usable), sos)
 *   until sos = [] or a refutation has been found
 * 
 * --------------------------------------------------------------------------------
 * 
 * function INFER(clause, usable) returns clauses
 *   
 *   resolve clause with each member of usable
 *   return the resulting clauses after applying filter
 *   
 * --------------------------------------------------------------------------------
 * 
 * procedure PROCESS(clauses, sos)
 * 
 *   for each clause in clauses do
 *       clause <- SIMPLIFY(clause)
 *       merge identical literals
 *       discard clause if it is a tautology
 *       sos <- [clause | sos]
 *       if clause has no literals then a refutation has been found
 *       if clause has one literal then look for unit refutation
 * </code>
 * 
 * Figure 9.14 Sketch of the OTTER theorem prover. Heuristic control is applied in the
 * selection of the "lightest" clause and in the FILTER function that eliminates uninteresting
 * clauses from consideration.
 */

// Note: The original implementation of OTTER has been retired 
// but its successor, Prover9, can be found at:
// http://www.prover9.org/
// or
// http://www.cs.unm.edu/~mccune/mace4/
// Should you wish to play with a mature implementation of a theorem prover :-)
// For lots of interesting problems to play with, see
// The TPTP Problem Library for Automated Theorem Proving:
// http://www.cs.miami.edu/~tptp/

/**
 * @author Ciaran O'Reilly
 * 
 */

// TODO: Remember to add reflexivity axiom if using paramodulation.
public class FOLOTTERLikeTheoremProver implements InferenceProcedure {
	// Ten seconds is default maximum query time permitted
	private long maxQueryTime = 10 * 1000;

	public FOLOTTERLikeTheoremProver() {

	}

	public FOLOTTERLikeTheoremProver(long maxQueryTime) {
		setMaxQueryTime(maxQueryTime);
	}

	public long getMaxQueryTime() {
		return maxQueryTime;
	}

	public void setMaxQueryTime(long maxQueryTime) {
		this.maxQueryTime = maxQueryTime;
	}
	
	//
	// START-InferenceProcedure
	public Set<Map<Variable, Term>> ask(FOLKnowledgeBase KB, Sentence alpha) {
		Set<Map<Variable, Term>> result = new LinkedHashSet<Map<Variable, Term>>();
		return result;
	}

	// END-InferenceProcedure
	// 
}
