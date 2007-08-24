package aima.logic.fol.parsing.ast;

import aima.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * 
 */

public class Variable extends Term {
	private String value;

	public Variable(String s) {
		value = s.trim();
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}

		Variable v = (Variable) o;
		return (v.getValue().equals(getValue()));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + value.hashCode();

		return result;
	}

	@Override
	public String toString() {
		return value;
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitVariable(this, arg);
	}

	@Override
	public Object clone() {
		return new Variable(value);
	}

	@Override
	public Variable copy() {
		return new Variable(value);
	}

}