package aima.core.logic.propositional;

import java.util.Set;

import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.visitors.NegativeSymbolCollector;
import aima.core.logic.propositional.visitors.PositiveSymbolCollector;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolClassifier {

	public Set<PropositionSymbol> getPositiveSymbolsIn(Sentence sentence) {
		return new PositiveSymbolCollector().getPositiveSymbolsIn(sentence);
	}

	public Set<PropositionSymbol> getNegativeSymbolsIn(Sentence sentence) {
		return new NegativeSymbolCollector().getNegativeSymbolsIn(sentence);
	}

	public Set<PropositionSymbol> getPureNegativeSymbolsIn(Sentence sentence) {
		Set<PropositionSymbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> allPositives = getPositiveSymbolsIn(sentence);
		return SetOps.difference(allNegatives, allPositives);
	}

	public Set<PropositionSymbol> getPurePositiveSymbolsIn(Sentence sentence) {
		Set<PropositionSymbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> allPositives = getPositiveSymbolsIn(sentence);
		return SetOps.difference(allPositives, allNegatives);
	}

	public Set<PropositionSymbol> getPureSymbolsIn(Sentence sentence) {
		Set<PropositionSymbol> allPureNegatives = getPureNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> allPurePositives = getPurePositiveSymbolsIn(sentence);
		return SetOps.union(allPurePositives, allPureNegatives);
	}

	public Set<PropositionSymbol> getImpureSymbolsIn(Sentence sentence) {
		Set<PropositionSymbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<PropositionSymbol> allPositives = getPositiveSymbolsIn(sentence);
		return SetOps.intersection(allPositives, allNegatives);
	}

	public Set<PropositionSymbol> getSymbolsIn(Sentence sentence) {
		return new SymbolCollector().getSymbolsIn(sentence);
	}
}