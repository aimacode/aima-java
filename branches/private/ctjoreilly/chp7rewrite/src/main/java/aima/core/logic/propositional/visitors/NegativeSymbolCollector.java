package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */
public class NegativeSymbolCollector extends BasicTraverser {
	@SuppressWarnings("unchecked")
	@Override
	public Object visitUnarySentence(ComplexSentence ns, Object arg) {
		Set<PropositionSymbol> s = (Set<PropositionSymbol>) arg;
		if (ns.get(0) instanceof PropositionSymbol) {
			s.add((PropositionSymbol) ns.get(0));
		} else {
			s = SetOps.union(s, (Set<PropositionSymbol>) ns.get(0).accept(this, arg));
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	public Set<PropositionSymbol> getNegativeSymbolsIn(Sentence s) {
		return (Set<PropositionSymbol>) s.accept(this, new HashSet<PropositionSymbol>());
	}
}
