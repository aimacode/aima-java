package aima.core.logic.basic.propositional.inference;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.api.propositional.KnowledgeBase;
import aima.core.logic.basic.propositional.kb.data.Model;
import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.core.logic.basic.propositional.visitors.SymbolCollector;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ?.??, page
 * ???.<br>
 * <br>
 * 
 * <pre>
 * function TT-ENTAILS?(KB, &alpha;) returns true or false
 *   inputs: KB, the knowledge base, a sentence in propositional logic
 *           &alpha;, the query, a sentence in propositional logic
 *           
 *   symbols <- a list of proposition symbols in KB and &alpha
 *   return TT-CHECK-ALL(KB, &alpha; symbols, {})
 *   
 * --------------------------------------------------------------------------------
 * 
 * function TT-CHECK-ALL(KB, &alpha; symbols, model) returns true or false
 *   if EMPTY?(symbols) then
 *     if PL-TRUE?(KB, model) then return PL-TRUE?(&alpha;, model)
 *     else return true // when KB is false, always return true
 *   else do
 *     P <- FIRST(symbols)
 *     rest <- REST(symbols)
 *     return (TT-CHECK-ALL(KB, &alpha;, rest, model &cup; { P = true })
 *            and
 *            TT-CHECK-ALL(KB, &alpha;, rest, model &cup; { P = false }))
 * </pre>
 * 
 * Figure ?.?? A truth-table enumeration algorithm for deciding propositional
 * entailment. (TT stands for truth table.) PL-TRUE? returns true if a sentence
 * holds within a model. The variable model represents a partional model - an
 * assignment to some of the symbols. The keyword <b>"and"</b> is used here as a
 * logical operation on its two arguments, returning true or false.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Anurag Rai
 */
public class TTEntails {
	
	/**
	 * Returns the answer to the specified question using the TT-Entails
	 * algorithm.
	 * 
	 * @param kb
	 *            KB, the knowledge base, a sentence in propositional logic
	 * 
	 * @param queryString
	 *            a question to ASK the knowledge base
	 *            
	 * @return the answer to the specified question using the TT-Entails
	 *         algorithm.
	 */
	public boolean ttEntails(KnowledgeBase kb, String queryString, PLParser plparser) {
		
		PLParser parser = plparser;
		Sentence alpha = parser.parse(queryString);
		return ttEntails(kb, alpha);
	}
	
	/**
	 * function TT-ENTAILS?(KB, &alpha;) returns true or false.
	 * 
	 * @param kb
	 *            KB, the knowledge base, a sentence in propositional logic
	 * @param alpha
	 *            &alpha;, the query, a sentence in propositional logic
	 * 
	 * @return true if KB entails &alpha;, false otherwise.
	 */
	public boolean ttEntails(KnowledgeBase kb, Sentence alpha) {
		// symbols <- a list of proposition symbols in KB and &alpha
		List<PropositionSymbol> symbols = new ArrayList<PropositionSymbol>(
				SymbolCollector.getSymbolsFrom(kb.asSentence(), alpha));

		// return TT-CHECK-ALL(KB, &alpha; symbols, {})
		return ttCheckAll(kb, alpha, symbols, new Model());
	}

	//
	/**
	 * function TT-CHECK-ALL(KB, &alpha; symbols, model) returns true or false
	 * 
	 * @param kb
	 *            KB, the knowledge base, a sentence in propositional logic
	 * @param alpha
	 *            &alpha;, the query, a sentence in propositional logic
	 * @param symbols
	 *            a list of currently unassigned propositional symbols in the
	 *            model.
	 * @param model
	 *            a partially or fully assigned model for the given KB and
	 *            query.
	 * @return true if KB entails &alpha;, false otherwise.
	 */
	public boolean ttCheckAll(KnowledgeBase kb, Sentence alpha,
			List<PropositionSymbol> symbols, Model model) {
		// if EMPTY?(symbols) then
		if (symbols.isEmpty()) {
			// if PL-TRUE?(KB, model) then return PL-TRUE?(&alpha;, model)
			if (model.isTrue(kb.asSentence())) {
				return model.isTrue(alpha);
			} else {
				// else return true // when KB is false, always return true
				return true;
			}
		}
		// else do
		else {
		// P <- FIRST(symbols)
		PropositionSymbol p = Util.first(symbols);
		// rest <- REST(symbols)
		List<PropositionSymbol> rest = Util.rest(symbols);
		// return (TT-CHECK-ALL(KB, &alpha;, rest, model &cup; { P = true })
		// and
		// TT-CHECK-ALL(KB, &alpha;, rest, model U { P = false }))
		return ttCheckAll(kb, alpha, rest, model.union(p, true))
				&& ttCheckAll(kb, alpha, rest, model.union(p, false));
		}
	}
}