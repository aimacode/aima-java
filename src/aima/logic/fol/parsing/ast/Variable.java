package aima.logic.fol.parsing.ast;

import java.util.List;

import aima.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Variable implements Term {
	private String value;
	private int hashCode = 0;

	public Variable(String s) {
		value = s.trim();
	}

	public String getValue() {
		return value;
	}
	
	//
	// START-Term
	public String getSymbolicName() {
		return getValue();
	}

	public boolean isCompound() {
		return false;
	}

	public List<Term> getArgs() {
		// Is not Compound, therefore should
		// return null for its arguments
		return null;
	}
	
	public Object accept(FOLVisitor v, Object arg) {
		return v.visitVariable(this, arg);
	}

	public Variable copy() {
		return new Variable(value);
	}
	
	// END-Term
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof Variable)) {
			return false;
		}

		Variable v = (Variable) o;
		return v.getValue().equals(getValue());
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + value.hashCode();
		}

		return hashCode;
	}

	@Override
	public String toString() {
		return value;
	}
}