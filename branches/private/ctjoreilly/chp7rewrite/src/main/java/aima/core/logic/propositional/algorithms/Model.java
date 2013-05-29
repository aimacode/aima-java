package aima.core.logic.propositional.algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import aima.core.logic.propositional.parsing.PLVisitor;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */
public class Model implements PLVisitor {

	private HashMap<Symbol, Boolean> h = new HashMap<Symbol, Boolean>();

	public Model() {

	}

	public Boolean getStatus(Symbol symbol) {
		return h.get(symbol);
	}

	public boolean isTrue(Symbol symbol) {
		return Boolean.TRUE.equals(h.get(symbol));
	}

	public boolean isFalse(Symbol symbol) {
		return Boolean.FALSE.equals(h.get(symbol));
	}

	public Model extend(Symbol symbol, boolean b) {
		Model m = new Model();
		m.h.putAll(this.h);
		m.h.put(symbol, b);
		return m;
	}

	public boolean isTrue(Sentence clause) {
		return Boolean.TRUE.equals(clause.accept(this, null));
	}

	public boolean isFalse(Sentence clause) {
		return Boolean.FALSE.equals(clause.accept(this, null));
	}

	public boolean isUnknown(Sentence clause) {
		return null == clause.accept(this, null);
	}

	public Model flip(Symbol s) {
		if (isTrue(s)) {
			return extend(s, false);
		}
		if (isFalse(s)) {
			return extend(s, true);
		}
		return this;
	}

	public Set<Symbol> getAssignedSymbols() {
		return Collections.unmodifiableSet(h.keySet());
	}

	public void print() {
		for (Map.Entry<Symbol, Boolean> e : h.entrySet()) {
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
	public Object visitSymbol(Symbol s, Object arg) {
		return getStatus(s);
	}

	@Override
	public Object visitTrueSentence(TrueSentence ts, Object arg) {
		return Boolean.TRUE;
	}

	@Override
	public Object visitFalseSentence(FalseSentence fs, Object arg) {
		return Boolean.FALSE;
	}

	@Override
	public Object visitNotSentence(UnarySentence fs, Object arg) {
		Object negatedValue = fs.getNegated().accept(this, null);
		if (negatedValue != null) {
			return new Boolean(!((Boolean) negatedValue).booleanValue());
		} else {
			return null;
		}
	}

	@Override
	public Object visitBinarySentence(BinarySentence bs, Object arg) {
		Boolean firstValue = (Boolean) bs.getFirst().accept(this, null);
		Boolean secondValue = (Boolean) bs.getSecond().accept(this, null);
		if ((firstValue == null) || (secondValue == null)) {
			// strictly not true for or/and
			// -FIX later
			return null;
		} else {
			String operator = bs.getOperator();
			if (operator.equals("AND")) {
				return firstValue && secondValue;
			} else if (operator.equals("OR")) {
				return firstValue || secondValue;
			} else if (operator.equals("=>")) {
				return !(firstValue && !secondValue);
			} else if (operator.equals("<=>")) {
				return firstValue.equals(secondValue);
			}
			return null;
		}
	}

	@Override
	public Object visitMultiSentence(MultiSentence fs, Object argd) {
		// TODO remove this?
		return null;
	}
	// END-PLVisitor
	//
}