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

	@Override
	public Set<PropositionSymbol> visitPropositionSymbol(PropositionSymbol s, Set<PropositionSymbol> arg) {
		// Do not add the always true or false symbols
		if (!s.isAlwaysTrue() && !s.isAlwaysFalse()) {
			arg.add(s);
		}
		return arg;
	}

	public Set<PropositionSymbol> getSymbolsIn(Sentence s) {
		Set<PropositionSymbol> result = new LinkedHashSet<PropositionSymbol>();
		return s.accept(this, result);
	}
}
