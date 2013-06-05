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
public class NegativeSymbolCollector extends BasicGatherer<PropositionSymbol> {

	@Override
	public Set<PropositionSymbol> visitUnarySentence(ComplexSentence ns, Set<PropositionSymbol> arg) {
		if (ns.getSimplerSentence(0).isPropositionSymbol()) {
			arg.add((PropositionSymbol) ns.getSimplerSentence(0));
		} else {
			arg = SetOps.union(arg, ns.getSimplerSentence(0).accept(this, arg));
		}
		return arg;
	}

	public Set<PropositionSymbol> getNegativeSymbolsIn(Sentence s) {
		Set<PropositionSymbol> result = new LinkedHashSet<PropositionSymbol>();
		return s.accept(this, result);
	}
}
