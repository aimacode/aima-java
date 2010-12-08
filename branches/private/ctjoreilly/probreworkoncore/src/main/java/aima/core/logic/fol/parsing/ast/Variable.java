package aima.core.logic.fol.parsing.ast;

import java.util.List;

import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Variable implements Term {
	private String value;
	private int hashCode = 0;
	private int indexical = -1;

	public Variable(String s) {
		value = s.trim();
	}

	public Variable(String s, int idx) {
		value = s.trim();
		indexical = idx;
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
		return new Variable(value, indexical);
	}

	// END-Term
	//

	public int getIndexical() {
		return indexical;
	}

	public void setIndexical(int idx) {
		indexical = idx;
		hashCode = 0;
	}

	public String getIndexedValue() {
		return value + indexical;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof Variable)) {
			return false;
		}

		Variable v = (Variable) o;
		return v.getValue().equals(getValue())
				&& v.getIndexical() == getIndexical();
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode += indexical;
			hashCode = 37 * hashCode + value.hashCode();
		}

		return hashCode;
	}

	@Override
	public String toString() {
		return value;
	}
}