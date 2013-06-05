package aima.core.logic.propositional.inference;

import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.Model;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;
import aima.core.util.SetOps;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 7.10, page
 * 248.<br>
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
 *     if PL-TRUE>(KB, model) then return PL-TRUE?(&alpha;, model)
 *     else return true // when KB is false, always return true
 *   else do
 *     P <- FIRST(symbols)
 *     rest <- REST(symbols)
 *     return (TT-CHECK-ALL(KB, &alpha;, rest, model &cup; { P = true })
 *            and
 *            TT-CHECK-ALL(KB, &alpha;, rest, model &cup; { P = false }))
 * </pre>
 * 
 * Figure 7.10 A truth-table enumeration algorithm for deciding propositional
 * entailment. (TT stands for truth table.) PL-TRUE? returns true if a sentence
 * holds within a model. The variable model represents a partional model - an
 * assignment to some of the symbols. The keyword <b>"and"</b> is used here as a
 * logical operation on its two arguments, returning true or false.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class TTEntails {

	/**
	 * Returns the answer to the specified question using the TT-Entails
	 * algorithm.
	 * 
	 * @param kb
	 *            a knowledge base to ASK
	 * @param alpha
	 *            a question to ASK the knowledge base
	 * 
	 * @return the answer to the specified question using the TT-Entails
	 *         algorithm.
	 */
	public boolean ttEntails(KnowledgeBase kb, String alpha) {
		Sentence kbSentence = kb.asSentence();
		Sentence querySentence = (Sentence) new PLParser().parse(alpha);
		SymbolCollector collector = new SymbolCollector();
		Set<PropositionSymbol> kbSymbols = collector.getSymbolsIn(kbSentence);
		Set<PropositionSymbol> querySymbols = collector
				.getSymbolsIn(querySentence);
		Set<PropositionSymbol> symbols = SetOps.union(kbSymbols, querySymbols);
		List<PropositionSymbol> symbolList = new Converter<PropositionSymbol>()
				.setToList(symbols);
		return ttCheckAll(kbSentence, querySentence, symbolList, new Model());
	}

	public boolean ttCheckAll(Sentence kbSentence, Sentence querySentence,
			List<PropositionSymbol> symbols, Model model) {
		if (symbols.isEmpty()) {
			if (model.isTrue(kbSentence)) {
				// System.out.println("#");
				return model.isTrue(querySentence);
			} else {
				// System.out.println("0");
				return true;
			}
		} else {
			PropositionSymbol symbol = Util.first(symbols);
			List<PropositionSymbol> rest = Util.rest(symbols);

			Model trueModel = model.extend(
					new PropositionSymbol(symbol.getSymbol()), true);
			Model falseModel = model.extend(
					new PropositionSymbol(symbol.getSymbol()), false);
			return (ttCheckAll(kbSentence, querySentence, rest, trueModel) && (ttCheckAll(
					kbSentence, querySentence, rest, falseModel)));
		}
	}
}