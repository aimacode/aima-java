/*
 * Created on Sep 2, 2003 by Ravi Mohan
 *  
 */
package aima.logic.propositional.algorithms;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import aima.logic.propositional.parsing.PLVisitor;
import aima.logic.propositional.parsing.ast.BinarySentence;
import aima.logic.propositional.parsing.ast.FalseSentence;
import aima.logic.propositional.parsing.ast.MultiSentence;
import aima.logic.propositional.parsing.ast.Sentence;
import aima.logic.propositional.parsing.ast.Symbol;
import aima.logic.propositional.parsing.ast.TrueSentence;
import aima.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */

public class Model implements PLVisitor {

	Hashtable<String, Boolean> h = new Hashtable<String, Boolean>();

	public Boolean getStatus(Symbol symbol) {
		Object status = h.get(symbol.getValue());
		if (status != null) {
			return (Boolean) status;
		}
		return null;
	}

	public boolean isTrue(Symbol symbol) {
		Object status = h.get(symbol.getValue());
		if (status != null) {
			return ((Boolean) status).booleanValue();
		}
		return false;
	}

	public boolean isFalse(Symbol symbol) {
		Object status = h.get(symbol.getValue());
		if (status != null) {
			return !((Boolean) status).booleanValue();
		}
		return false;
	}

	private boolean isUnknown(Symbol s) {
		Object o = h.get(s.getValue());
		return (o == null);

	}

	public Model extend(Symbol symbol, boolean b) {
		Model m = new Model();
		return extend(symbol.getValue(), b);
	}

	public Model extend(String s, boolean b) {
		Model m = new Model();
		Iterator<String> i = this.h.keySet().iterator();
		while (i.hasNext()) {
			String key = i.next();
			Boolean value = h.get(key);
			String newKey = new String((key).toCharArray());
			if (value == null) {
				throw new RuntimeException();
			}
			m.h.put(key, value);
		}
		m.h.put(s, new Boolean(b));
		return m;
	}

	public void print() {
		Iterator i = h.keySet().iterator();
		while (i.hasNext()) {
			Object key = i.next();
			Object value = h.get(key);
			System.out.print(key + " = " + value + " ");
			// System.out.print (key +" = " +((Boolean)value).booleanValue());
		}
		System.out.println();
	}

	public boolean isTrue(Sentence clause) {
		Object result = clause.accept(this, null);
		return (result == null) ? false
				: ((Boolean) result).booleanValue() == true;
	}

	public boolean isFalse(Sentence clause) {
		Object o = clause.accept(this, null);
		return (o != null) ? ((Boolean) o).booleanValue() == false : false;
	}

	public boolean isUnknown(Sentence clause) { // TODO TEST WELL
		Object o = clause.accept(this, null);
		return (o == null);
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

	@Override
	public String toString() {
		return h.toString();
	}

	// VISITOR METHODS
	public Object visitSymbol(Symbol s, Object arg) {
		return getStatus(s);
	}

	public Object visitTrueSentence(TrueSentence ts, Object arg) {
		return Boolean.TRUE;
	}

	public Object visitFalseSentence(FalseSentence fs, Object arg) {
		return Boolean.FALSE;
	}

	public Object visitNotSentence(UnarySentence fs, Object arg) {
		Object negatedValue = fs.getNegated().accept(this, null);
		if (negatedValue != null) {
			return new Boolean(!((Boolean) negatedValue).booleanValue());
		} else {
			return null;
		}
	}

	public Object visitBinarySentence(BinarySentence bs, Object arg) {
		Object firstValue = bs.getFirst().accept(this, null);
		Object secondValue = bs.getSecond().accept(this, null);
		if ((firstValue == null) || (secondValue == null)) { // strictly not
			// true for or/and
			// -FIX later
			return null;
		} else {
			String operator = bs.getOperator();
			if (operator.equals("AND")) {
				return evaluateAnd((Boolean) firstValue, (Boolean) secondValue);
			}
			if (operator.equals("OR")) {
				return evaluateOr((Boolean) firstValue, (Boolean) secondValue);
			}
			if (operator.equals("=>")) {
				return evaluateImplied((Boolean) firstValue,
						(Boolean) secondValue);
			}
			if (operator.equals("<=>")) {
				return evaluateBiConditional((Boolean) firstValue,
						(Boolean) secondValue);
			}
			return null;
		}
	}

	public Object visitMultiSentence(MultiSentence fs, Object argd) {
		// TODO remove this?
		return null;
	}

	private Boolean evaluateAnd(Boolean firstValue, Boolean secondValue) {
		if ((firstValue.equals(Boolean.TRUE))
				&& (secondValue.equals(Boolean.TRUE))) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	private Boolean evaluateOr(Boolean firstValue, Boolean secondValue) {
		if ((firstValue.equals(Boolean.TRUE))
				|| (secondValue.equals(Boolean.TRUE))) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	private Boolean evaluateImplied(Boolean firstValue, Boolean secondValue) {
		if ((firstValue.equals(Boolean.TRUE))
				&& (secondValue.equals(Boolean.FALSE))) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	private Boolean evaluateBiConditional(Boolean firstValue,
			Boolean secondValue) {
		if (firstValue.equals(secondValue)) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public Set<Symbol> getAssignedSymbols() {
		Set<Symbol> set = new HashSet<Symbol>();
		Iterator i = this.h.keySet().iterator();
		while (i.hasNext()) {
			Symbol key = new Symbol((String) i.next());
			if (!(isUnknown(key))) {
				set.add(key);
			}
		}
		return set;
	}

	public boolean matches(String variable, boolean value) {
		if (value) {
			return isTrue(new Symbol(variable));
		} else if (!(value)) {
			return isFalse(new Symbol(variable));
		}
		return false;
	}

}