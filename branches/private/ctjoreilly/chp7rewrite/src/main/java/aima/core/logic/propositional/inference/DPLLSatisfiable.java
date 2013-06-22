package aima.core.logic.propositional.inference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.Model;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Util;
import aima.core.util.datastructure.Pair;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 261.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function DPLL-SATISFIABLE?(s) returns true or false
 *   inputs: s, a sentence in propositional logic.
 *   
 *   clauses &larr; the set of clauses in the CNF representation of s
 *   symbols &larr; a list of the proposition symbols in s
 *   return DPLL(clauses, symbols, {})
 * 
 * --------------------------------------------------------------------------------
 * 
 * function DPLL(clauses, symbols, model) returns true or false
 *   
 *   if every clause in clauses is true in model then return true
 *   if some clause in clauses is false in model then return false
 *   P, value &larr; FIND-PURE-SYMBOL(symbols, clauses, model)
 *   if P is non-null then return DPLL(clauses, symbols - P, model &cup; {P = value})
 *   P, value &larr; FIND-UNIT-CLAUSE(clauses, model)
 *   if P is non-null then return DPLL(clauses, symbols - P, model &cup; {P = value})
 *   P &larr; FIRST(symbols); rest &larr; REST(symbols)
 *   return DPLL(clauses, rest, model &cup; {P = true}) or
 *          DPLL(clauses, rest, model &cup; {P = false})
 * </code>
 * </pre>
 * 
 * Figure 7.17 The DPLL algorithm for checking satisfiability of a sentence in
 * propositional logic. The ideas behind FIND-PURE-SYMBOL and FIND-UNIT-CLAUSE
 * are described in the test; each returns a symbol (or null) and the truth
 * value to assign to that symbol. Like TT-ENTAILS?, DPLL operates over partial
 * models.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class DPLLSatisfiable {

	/**
	 * DPLL-SATISFIABLE?(s)<br>
	 * Checks the satisfiability of a sentence in propositional logic.
	 * 
	 * @param s
	 *            a sentence in propositional logic.
	 * @return true if the sentence is satisfiable, false otherwise.
	 */
	public boolean dpllSatisfiable(Sentence s) {
		// clauses <- the set of clauses in the CNF representation of s
		Set<Clause> clauses = ConvertToConjunctionOfClauses.convert(s)
				.getClauses();
		// symbols <- a list of the proposition symbols in s
		List<PropositionSymbol> symbols = getPropositionSymbolsInSentence(s);

		// return DPLL(clauses, symbols, {})
		return dpll(clauses, symbols, new Model());
	}

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
	public boolean dpll(Set<Clause> clauses, List<PropositionSymbol> symbols,
			Model model) {
		// if every clause in clauses is true in model then return true
		if (everyClauseTrue(clauses, model)) {
			return true;
		}
		// if some clause in clauses is false in model then return false
		if (someClauseFalse(clauses, model)) {
			return false;
		}

		// P, value <- FIND-PURE-SYMBOL(symbols, clauses, model)
		// if P is non-null then
		// .. return DPLL(clauses, symbols - P, model U {P = value})
// TODO

		// P, value <- FIND-UNIT-CLAUSE(clauses, model)
		// if P is non-null then
		// .. return DPLL(clauses, symbols - P, model U {P = value})
// TODO

		// P <- FIRST(symbols); rest <- REST(symbols)
		PropositionSymbol p = Util.first(symbols);
		List<PropositionSymbol> rest = Util.rest(symbols);
		// return DPLL(clauses, rest, model &cup; {P = true}) or
		// ...... DPLL(clauses, rest, model &cup; {P = false})
		return dpll(clauses, rest, model.union(p, true))
				|| dpll(clauses, rest, model.union(p, false));
	}

	//
	// SUPPORTING CODE
	//

	public boolean isEntailed(KnowledgeBase kb, Sentence alpha) {
		Sentence isContradiction = new ComplexSentence(Connective.AND,
				kb.asSentence(), new ComplexSentence(Connective.NOT, alpha));

		return !dpllSatisfiable(isContradiction);
	}

	//
	// PROTECTED
	//
	protected List<PropositionSymbol> getPropositionSymbolsInSentence(Sentence s) {
		List<PropositionSymbol> result = new ArrayList<PropositionSymbol>(
				SymbolCollector.getSymbolsFrom(s));

		return result;
	}
	
	protected Pair<PropositionSymbol, Boolean> findPureSymbol(
			List<PropositionSymbol> symbols, Set<Clause> clauses, Model model) {
// TODO		
		return null;
	}

	protected Pair<PropositionSymbol, Boolean> findUnitClause(
			Set<Clause> clauses, Model model) {
// TODO
		return null;
	}

	protected boolean everyClauseTrue(Set<Clause> clauses, Model model) {
		Set<PropositionSymbol> assignedSymbols = model.getAssignedSymbols();
		for (Clause c : clauses) {
			if (c.isFalse()) {
				return false;
			}
			if (c.isTautology()) {
				continue;
			}
			boolean isTrue = false;
			for (PropositionSymbol positive : c.getPositiveSymbols()) {
				if (assignedSymbols.contains(positive)) {
					if (model.isTrue(positive)) {
						isTrue = true;
						break;
					}
				}
			}
			if (isTrue) {
				continue;
			}
			for (PropositionSymbol negative : c.getNegativeSymbols()) {
				if (assignedSymbols.contains(negative)) {
					if (model.isFalse(negative)) {
						isTrue = true;
						break;
					}
				}
			}
			
			if (!isTrue) {
				return false;
			}
		}
		return true;
	}

	protected boolean someClauseFalse(Set<Clause> clauses, Model model) {
		Set<PropositionSymbol> assignedSymbols = model.getAssignedSymbols();
		for (Clause c : clauses) {
			if (c.isFalse()) {
				return true;
			}
			if (c.isTautology()) {
				continue;
			}
			boolean isFalse = true;
			for (PropositionSymbol positive : c.getPositiveSymbols()) {
				if (assignedSymbols.contains(positive)) {
					if (model.isTrue(positive)) {
						isFalse = false;
						break;
					}
				} else {
					isFalse = false;
					break;
				}
			}
			if (isFalse) {
				for (PropositionSymbol negative : c.getNegativeSymbols()) {
					if (assignedSymbols.contains(negative)) {
						if (model.isFalse(negative)) {
							isFalse = false;
							break;
						}
					} else {
						isFalse = false;
						break;
					}
				}

				if (isFalse) {
					return true;
				}
			}
		}
		return false;
	}
}