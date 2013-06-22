package aima.core.logic.propositional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * @author Ravi Mohan
 * 
 */
public class Model implements PLVisitor<Boolean, Boolean> {

	private HashMap<PropositionSymbol, Boolean> h = new HashMap<PropositionSymbol, Boolean>();

	public Model() {

	}

	public Boolean getStatus(PropositionSymbol symbol) {
		return h.get(symbol);
	}

	public boolean isTrue(PropositionSymbol symbol) {
		return Boolean.TRUE.equals(h.get(symbol));
	}

	public boolean isFalse(PropositionSymbol symbol) {
		return Boolean.FALSE.equals(h.get(symbol));
	}

	public Model union(PropositionSymbol symbol, boolean b) {
		Model m = new Model();
		m.h.putAll(this.h);
		m.h.put(symbol, b);
		return m;
	}

	public boolean isTrue(Sentence s) {
		return Boolean.TRUE.equals(s.accept(this, null));
	}

	public boolean isFalse(Sentence s) {
		return Boolean.FALSE.equals(s.accept(this, null));
	}

	public boolean isUnknown(Sentence s) {
		return null == s.accept(this, null);
	}

	public Model flip(PropositionSymbol s) {
		if (isTrue(s)) {
			return union(s, false);
		}
		if (isFalse(s)) {
			return union(s, true);
		}
		return this;
	}

	public Set<PropositionSymbol> getAssignedSymbols() {
		return Collections.unmodifiableSet(h.keySet());
	}

	public void print() {
		for (Map.Entry<PropositionSymbol, Boolean> e : h.entrySet()) {
			System.out.print(e.getKey() + " = " + e.getValue() + " ");
		}
		System.out.println();
	}

	@Override
	public String toString() {
		return h.toString();
	}

	//
	// START-PLVisitor
	@Override
	public Boolean visitPropositionSymbol(PropositionSymbol s, Boolean arg) {
		if (s.isAlwaysTrue()) {
			return Boolean.TRUE;
		}
		if (s.isAlwaysFalse()) {
			return Boolean.FALSE;
		}
		return getStatus(s);
	}

	@Override
	public Boolean visitUnarySentence(ComplexSentence fs, Boolean arg) {
		Object negatedValue = fs.getSimplerSentence(0).accept(this, null);
		if (negatedValue != null) {
			return new Boolean(!((Boolean) negatedValue).booleanValue());
		} else {
			return null;
		}
	}

	@Override
	public Boolean visitBinarySentence(ComplexSentence bs, Boolean arg) {
		Boolean firstValue = (Boolean) bs.getSimplerSentence(0).accept(this, null);
		Boolean secondValue = (Boolean) bs.getSimplerSentence(1).accept(this, null);
		if ((firstValue == null) || (secondValue == null)) {
			// strictly not true for or/and
			// -FIX later
			return null;
		} else {
			Connective connective = bs.getConnective();
			if (connective.equals(Connective.AND)) {
				return firstValue && secondValue;
			} else if (connective.equals(Connective.OR)) {
				return firstValue || secondValue;
			} else if (connective.equals(Connective.IMPLICATION)) {
				return !(firstValue && !secondValue);
			} else if (connective.equals(Connective.BICONDITIONAL)) {
				return firstValue.equals(secondValue);
			}
			return null;
		}
	}
	// END-PLVisitor
	//
}