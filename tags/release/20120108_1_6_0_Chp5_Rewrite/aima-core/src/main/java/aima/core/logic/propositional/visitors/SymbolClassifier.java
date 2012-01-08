package aima.core.logic.propositional.visitors;

import java.util.Set;

import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolClassifier {

	public Set<Symbol> getPositiveSymbolsIn(Sentence sentence) {
		return new PositiveSymbolCollector().getPositiveSymbolsIn(sentence);
	}

	public Set<Symbol> getNegativeSymbolsIn(Sentence sentence) {
		return new NegativeSymbolCollector().getNegativeSymbolsIn(sentence);
	}

	public Set<Symbol> getPureNegativeSymbolsIn(Sentence sentence) {
		Set<Symbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<Symbol> allPositives = getPositiveSymbolsIn(sentence);
		return SetOps.difference(allNegatives, allPositives);
	}

	public Set<Symbol> getPurePositiveSymbolsIn(Sentence sentence) {
		Set<Symbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<Symbol> allPositives = getPositiveSymbolsIn(sentence);
		return SetOps.difference(allPositives, allNegatives);
	}

	public Set<Symbol> getPureSymbolsIn(Sentence sentence) {
		Set<Symbol> allPureNegatives = getPureNegativeSymbolsIn(sentence);
		Set<Symbol> allPurePositives = getPurePositiveSymbolsIn(sentence);
		return SetOps.union(allPurePositives, allPureNegatives);
	}

	public Set<Symbol> getImpureSymbolsIn(Sentence sentence) {
		Set<Symbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<Symbol> allPositives = getPositiveSymbolsIn(sentence);
		return SetOps.intersection(allPositives, allNegatives);
	}

	public Set<Symbol> getSymbolsIn(Sentence sentence) {
		return new SymbolCollector().getSymbolsIn(sentence);
	}
}