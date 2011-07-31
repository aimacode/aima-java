package aima.core.logic.fol.parsing.ast;

import java.util.List;

import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Constant implements Term {
	private String value;
	private int hashCode = 0;

	public Constant(String s) {
		value = s;
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
		return v.visitConstant(this, arg);
	}

	public Constant copy() {
		return new Constant(value);
	}

	// END-Term
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof Constant)) {
			return false;
		}
		Constant c = (Constant) o;
		return c.getValue().equals(getValue());

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
