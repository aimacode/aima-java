package aima.core.logic.propositional.inference;

import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Model;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * Interface describing main API of the DPLL algorithm as described in Figure
 * 7.17 but not how it should be implemented. This is to allow for
 * experimentation with different implementations to explore different
 * performance optimization strategies as described on p.g.s 261-262 of AIMA3e 
 * (i.e. 1. component analysis, 2. variable and value ordering, 
 * 3. intelligent backtracking, 4. random restarts, and 5. clever indexing).
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface DPLL {

	/**
	 * DPLL-SATISFIABLE?(s)<br>
	 * Checks the satisfiability of a sentence in propositional logic.
	 * 
	 * @param s
	 *            a sentence in propositional logic.
	 * @return true if the sentence is satisfiable, false otherwise.
	 */
	boolean dpllSatisfiable(Sentence s);

	/**
	 * DPLL(clauses, symbols, model)<br>
	 * 
	 * @param clauses
	 *            the set of clauses.
	 * @param symbols
	 *            a list of unassigned symbols.
	 * @param model
	 *            contains the values for assigned symbols.
	 * @return true if the model is satisfiable under current assignments, false
	 *         otherwise.
	 */
	boolean dpll(Set<Clause> clauses, List<PropositionSymbol> symbols,
			Model model);

	/**
	 * Determine if KB |= &alpha;, i.e. alpha is entailed by KB.
	 * 
	 * @param kb
	 *            a Knowledge Base in propositional logic.
	 * @param alpha
	 *            a propositional sentence.
	 * @return true, if &alpha; is entailed by KB, false otherwise.
	 */
	boolean isEntailed(KnowledgeBase kb, Sentence alpha);
}
