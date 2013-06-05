package aima.core.logic.propositional.visitors;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */
public class PositiveSymbolCollector extends BasicGatherer<PropositionSymbol> {

	@Override
	public Set<PropositionSymbol> visitPropositionSymbol(PropositionSymbol symbol, Set<PropositionSymbol> arg) {
		arg.add(symbol);// add ALL symbols not discarded by the visitNotSentence
		return arg;
	}

	@Override
	public Set<PropositionSymbol> visitUnarySentence(ComplexSentence ns, Set<PropositionSymbol> arg) {
		if (ns.getSimplerSentence(0).isPropositionSymbol()) {
			// do nothing .do NOT add a negated Symbol
		} else {
			arg = SetOps.union(arg, ns.getSimplerSentence(0).accept(this, arg));
		}
		return arg;
	}

	@SuppressWarnings("unchecked")
	public Set<PropositionSymbol> getPositiveSymbolsIn(Sentence sentence) {
		Set<PropositionSymbol> result = new LinkedHashSet<PropositionSymbol>();
		return sentence.accept(this, result);
	}
}