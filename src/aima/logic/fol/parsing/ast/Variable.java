/*
 * Created on Sep 14, 2003 by Ravi Mohan
 *  
 */
package aima.logic.fol.parsing.ast;

import aima.logic.fol.parsing.FOLVisitor;

public class Variable extends Term {
	private String value;

	public Variable(String s) {
		value = s.trim();
	}

	public String getValue() {
		return value;
	}

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

	public int hashCode() {
		int result = 17;
	    result = 37 * result + value.hashCode();
		
		return result;
	}

	public String toString() {
		return value;
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitVariable(this, arg);
	}

	public Object clone() {
		return new Variable(value);
	}

	public Variable copy() {
		return new Variable(value);
	}


}