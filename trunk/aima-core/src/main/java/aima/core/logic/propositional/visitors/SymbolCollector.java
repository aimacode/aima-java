package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;

/**
 * @author Ravi Mohan
 * 
 */
public class SymbolCollector extends BasicTraverser {

	@Override
	public Object visitSymbol(Symbol s, Object arg) {
		Set<Symbol> symbolsCollectedSoFar = (Set) arg;
		symbolsCollectedSoFar.add(new Symbol(s.getValue()));
		return symbolsCollectedSoFar;
	}

	public Set<Symbol> getSymbolsIn(Sentence s) {
		if (s == null) {// empty knowledge bases == null fix this later
			return new HashSet<Symbol>();
		}
		return (Set<Symbol>) s.accept(this, new HashSet());
	}
}
