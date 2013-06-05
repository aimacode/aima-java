package aima.core.logic.propositional.visitors;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolCollector extends BasicGatherer<PropositionSymbol> {
	
	public static Set<PropositionSymbol> getSymbolsFrom(Sentence... sentences) {
		Set<PropositionSymbol> result = new LinkedHashSet<PropositionSymbol>();
		
		SymbolCollector symbolCollector = new SymbolCollector();
		for (Sentence s : sentences) {
			result = s.accept(symbolCollector, result);
		}
		
		return result;
	}

	@Override
	public Set<PropositionSymbol> visitPropositionSymbol(PropositionSymbol s, Set<PropositionSymbol> arg) {
		// Do not add the always true or false symbols
		if (!s.isAlwaysTrue() && !s.isAlwaysFalse()) {
			arg.add(s);
		}
		return arg;
	}
}
