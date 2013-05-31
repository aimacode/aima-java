package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolCollector extends BasicTraverser {

	@SuppressWarnings("unchecked")
	@Override
	public Object visitPropositionSymbol(PropositionSymbol s, Object arg) {
		Set<PropositionSymbol> symbolsCollectedSoFar = (Set<PropositionSymbol>) arg;
		symbolsCollectedSoFar.add(new PropositionSymbol(s.getSymbol()));
		return symbolsCollectedSoFar;
	}

	@SuppressWarnings("unchecked")
	public Set<PropositionSymbol> getSymbolsIn(Sentence s) {
		if (s == null) {// empty knowledge bases == null fix this later
			return new HashSet<PropositionSymbol>();
		}
		return (Set<PropositionSymbol>) s.accept(this, new HashSet<PropositionSymbol>());
	}
}
