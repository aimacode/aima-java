/*
 * Created on Dec 4, 2004
 *
 */
package aima.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.parsing.ast.UnarySentence;
import aima.util.SetOps;

/**
 * @author Ravi Mohan
 * 
 */

public class PositiveSymbolCollector extends BasicTraverser {
	@Override
	public Object visitSymbol(Symbol symbol, Object arg) {
		Set<Symbol> s = (Set<Symbol>) arg;
		s.add(symbol);// add ALL symbols not discarded by the visitNotSentence
		// mathod
		return arg;
	}

	@Override
	public Object visitNotSentence(UnarySentence ns, Object arg) {
		Set<Symbol> s = (Set<Symbol>) arg;
		if (ns.getNegated() instanceof Symbol) {
			// do nothing .do NOT add a negated Symbol
		} else {
			s = new SetOps<Symbol>().union(s, (Set<Symbol>) ns.getNegated()
					.accept(this, arg));
		}
		return s;
	}

	public Set<Symbol> getPositiveSymbolsIn(Sentence sentence) {
		return (Set<Symbol>) sentence.accept(this, new HashSet<Symbol>());
	}
}