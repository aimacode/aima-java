/*
 * Created on Sep 14, 2003 by Ravi Mohan
 *  
 */
package aima.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.List;

import aima.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * 
 */

public class Function extends Term {
	private String functionName;

	private List<Term> terms;

	public String getFunctionName() {
		return functionName;
	}

	public List<Term> getTerms() {
		return terms;
	}

	public Function(String functionName, List<Term> terms) {
		this.functionName = functionName;
		this.terms = terms;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}

		Function f = (Function) o;
		boolean nameEquality = f.getFunctionName().equals(getFunctionName());
		boolean termEquality = f.getTerms().equals(getTerms());
		boolean eq = nameEquality && termEquality;

		return eq;

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + functionName.hashCode();
		for (Term t : terms) {
			result = 37 * result + t.hashCode();
		}
		return result;
	}

	public Object accept(FOLVisitor v, Object arg) {

		return v.visitFunction(this, arg);

	}

	@Override
	public String toString() {
		String pre = " " + functionName + "( ";
		String mid = "";
		for (int i = 0; i < terms.size(); i++) {
			mid += "," + (terms.get(i)).toString();
		}
		mid = mid.substring(1);
		String post = " )";
		return pre + mid + post;
	}

	@Override
	public Function copy() {
		List<Term> copyTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.size(); i++) {
			Term t = terms.get(i);
			copyTerms.add(t.copy());
		}
		return new Function(functionName, copyTerms);
	}

}