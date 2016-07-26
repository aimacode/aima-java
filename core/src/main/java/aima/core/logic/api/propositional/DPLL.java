package aima.core.logic.api.propositional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.logic.api.propositional.KnowledgeBase;
import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.kb.data.Model;
import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.Connective;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.core.logic.basic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.logic.basic.propositional.visitors.SymbolCollector;

/**
 * Interface describing main API of the DPLL algorithm as described in Figure
 * ?.?? but not how it should be implemented. This is to allow for
 * experimentation with different implementations to explore different
 * performance optimization strategies as described on p.g.s ???-??? of AIMA4e 
 * (i.e. 1. component analysis, 2. variable and value ordering, 
 * 3. intelligent backtracking, 4. random restarts, and 5. clever indexing).
 * 
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 * 
 */
public interface DPLL {
	
	/**
	 * DPLL-SATISFIABLE?(s)<br>
	 * Checks the satisfiability of a sentence in propositional logic.
	 * 
	 * @param s
	 *            a string representing a sentence in propositional logic.
	 * @return true if the sentence is satisfiable, false otherwise.
	 */
	default boolean dpllSatisfiable(String s, PLParser plparser) {
		PLParser parser = plparser;
		Sentence sentence = parser.parse(s);
		return dpllSatisfiable(sentence);
	}
	/**
	 * function DPLL(clauses, symbols, model) returns true or false <br>
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
	default boolean isEntailed(KnowledgeBase kb, Sentence alpha) {
		// AIMA4e p.g. ???: kb |= alpha, can be done by testing
		// unsatisfiability of kb & ~alpha.
		
		// KB & ~alpha;
		Sentence queryKbAndNotAlpha = new ComplexSentence(Connective.AND,
						kb.asSentence(), new ComplexSentence(Connective.NOT, alpha));
		
		Set<Clause> kbAndNotAlpha = new LinkedHashSet<Clause>(
						ConvertToConjunctionOfClauses.convert(queryKbAndNotAlpha).getClauses());
		Set<PropositionSymbol>  symbols       = new LinkedHashSet<PropositionSymbol>(
						SymbolCollector.getSymbolsFrom(queryKbAndNotAlpha));

		return !dpll(kbAndNotAlpha, new ArrayList<PropositionSymbol>(symbols), new Model());
	}
}
