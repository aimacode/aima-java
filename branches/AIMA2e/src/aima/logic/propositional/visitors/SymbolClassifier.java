/*
 * Created on Dec 4, 2004
 *
 */
package aima.logic.propositional.visitors;

import java.util.Set;

import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.util.SetOps;

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
		return new SetOps<Symbol>().difference(allNegatives, allPositives);
	}

	public Set<Symbol> getPurePositiveSymbolsIn(Sentence sentence) {
		Set<Symbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<Symbol> allPositives = getPositiveSymbolsIn(sentence);
		return new SetOps<Symbol>().difference(allPositives, allNegatives);
	}

	public Set<Symbol> getPureSymbolsIn(Sentence sentence) {
		Set<Symbol> allPureNegatives = getPureNegativeSymbolsIn(sentence);
		Set<Symbol> allPurePositives = getPurePositiveSymbolsIn(sentence);
		return new SetOps<Symbol>().union(allPurePositives, allPureNegatives);
	}

	public Set<Symbol> getImpureSymbolsIn(Sentence sentence) {
		Set<Symbol> allNegatives = getNegativeSymbolsIn(sentence);
		Set<Symbol> allPositives = getPositiveSymbolsIn(sentence);
		return new SetOps<Symbol>().intersection(allPositives, allNegatives);
	}

	public Set<Symbol> getSymbolsIn(Sentence sentence) {
		return new SymbolCollector().getSymbolsIn(sentence);
	}

}