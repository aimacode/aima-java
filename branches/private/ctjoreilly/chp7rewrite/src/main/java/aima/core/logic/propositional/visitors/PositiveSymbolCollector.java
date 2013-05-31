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
public class PositiveSymbolCollector extends BasicTraverser {
	@SuppressWarnings("unchecked")
	@Override
	public Object visitPropositionSymbol(PropositionSymbol symbol, Object arg) {
		Set<PropositionSymbol> s = (Set<PropositionSymbol>) arg;
		s.add(symbol);// add ALL symbols not discarded by the visitNotSentence
		// mathod
		return arg;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitUnarySentence(ComplexSentence ns, Object arg) {
		Set<PropositionSymbol> s = (Set<PropositionSymbol>) arg;
		if (ns.get(0) instanceof PropositionSymbol) {
			// do nothing .do NOT add a negated Symbol
		} else {
			s = SetOps
					.union(s, (Set<PropositionSymbol>) ns.get(0).accept(this, arg));
		}
		return s;
	}

	@SuppressWarnings("unchecked")
	public Set<PropositionSymbol> getPositiveSymbolsIn(Sentence sentence) {
		return (Set<PropositionSymbol>) sentence.accept(this, new HashSet<PropositionSymbol>());
	}
}