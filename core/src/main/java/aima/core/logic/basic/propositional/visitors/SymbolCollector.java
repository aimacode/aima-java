package aima.core.logic.basic.propositional.visitors;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;

/**
 * Utility class for collecting propositional symbols from sentences. Will
 * exclude the always false and true symbols.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class SymbolCollector extends BasicGatherer<PropositionSymbol> {

	/**
	 * Collect a set of propositional symbols from a list of given sentences.
	 * 
	 * @param sentences
	 *            a list of sentences from which to collect symbols.
	 * @return a set of all the proposition symbols that are not always true or
	 *         false contained within the input sentences.
	 */
	public static Set<PropositionSymbol> getSymbolsFrom(Sentence... sentences) {
		Set<PropositionSymbol> result = new LinkedHashSet<PropositionSymbol>();

		SymbolCollector symbolCollector = new SymbolCollector();
		for (Sentence s : sentences) {
			result = s.accept(symbolCollector, result);
		}

		return result;
	}

	@Override
	public Set<PropositionSymbol> visitPropositionSymbol(PropositionSymbol s,
			Set<PropositionSymbol> arg) {
		// Do not add the always true or false symbols
		if (!s.isAlwaysTrue() && !s.isAlwaysFalse()) {
			arg.add(s);
		}
		return arg;
	}
}
