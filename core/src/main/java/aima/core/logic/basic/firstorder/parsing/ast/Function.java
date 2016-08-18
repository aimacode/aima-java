package aima.core.logic.basic.firstorder.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

import aima.core.logic.basic.firstorder.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class Function implements Term {
	private String functionName;
	private List<Term> terms = new ArrayList<Term>();
	private String stringRep = null;
	private int hashCode = 0;

	public Function(String functionName, List<Term> terms) {
		this.functionName = functionName;
		this.terms.addAll(terms);
	}

	public String getFunctionName() {
		return functionName;
	}

	public List<Term> getTerms() {
		return Collections.unmodifiableList(terms);
	}

	//
	// START-Term
	public String getSymbolicName() {
		return getFunctionName();
	}

	public boolean isCompound() {
		return true;
	}

	public List<Term> getArgs() {
		return getTerms();
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitFunction(this, arg);
	}

	public Function copy() {
		List<Term> copyTerms = new ArrayList<Term>();
		for (Term t : terms) {
			copyTerms.add(t.copy());
		}
		return new Function(functionName, copyTerms);
	}

	// END-Term
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof Function)) {
			return false;
		}

		Function f = (Function) o;

		return f.getFunctionName().equals(getFunctionName())
				&& f.getTerms().equals(getTerms());
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + functionName.hashCode();
			for (Term t : terms) {
				hashCode = 37 * hashCode + t.hashCode();
			}
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append(functionName);
			StringJoiner sj = new StringJoiner(",", "(", ")");
			for (Term t : terms) {
				sj.add(t.toString());
			}
			stringRep = sb.toString() + sj.toString();
			return stringRep;
		}
		return stringRep;
	}
}