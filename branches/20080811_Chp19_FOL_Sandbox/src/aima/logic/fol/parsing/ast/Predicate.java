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

public class Predicate implements AtomicSentence {
	private String predicateName;

	private List<Term> terms;

	public Predicate(String predicateName, List<Term> terms) {
		this.predicateName = predicateName;
		this.terms = terms;
	}
	
	//
	// START-AtomicSentence
	public String getSymbolicName() {
		return getPredicateName();
	}

	public List<Term> getTerms() {
		return terms;
	}
	
	// END-AtomicSentence
	//

	public String getPredicateName() {
		return predicateName;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof Predicate)) {
			return false;
		}
		Predicate p = (Predicate) o;
		return p.getPredicateName().equals(getPredicateName())
				&& p.getTerms().equals(getTerms());
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + predicateName.hashCode();
		for (Term t : terms) {
			result = 37 * result + t.hashCode();
		}
		return result;
	}

	boolean checkTerms(List l1, List l2) {
		boolean ret = true;
		for (int i = 0; i < l1.size(); i++) {
			Term t1 = (Term) l1.get(i);
			Term t2 = (Term) l2.get(i);
			System.out.println("comparing " + t1 + " and " + t2);
			if (!(t1.equals(t2))) {
				System.out.println(t1.getClass().getName() + " !=  "
						+ t2.getClass().getName());
				System.out.println(t1 + " !=  " + t2);
				ret = false;
			}
		}
		return ret;
	}

	public Object accept(FOLVisitor v, Object arg) {

		return v.visitPredicate(this, arg);

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(predicateName);
		sb.append("(");

		boolean first = true;
		for (Term t : terms) {
			if (first) {
				first = false;
			} else {
				sb.append(",");
			}
			sb.append(t.toString());
		}
		
		sb.append(")");
		return sb.toString();
	}

	public Predicate copy() {
		List<Term> copyTerms = new ArrayList<Term>();
		for (int i = 0; i < terms.size(); i++) {
			Term t = terms.get(i);
			copyTerms.add(t.copy());
		}
		return new Predicate(predicateName, copyTerms);
	}

}