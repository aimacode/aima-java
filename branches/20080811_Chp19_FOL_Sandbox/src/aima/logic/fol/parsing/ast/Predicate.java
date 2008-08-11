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

public class Predicate implements Sentence {
	private String predicateName;

	private List<Term> terms;

	public Predicate(String predicateName, List<Term> terms) {
		this.predicateName = predicateName;
		this.terms = terms;
	}

	public String getPredicateName() {
		return predicateName;
	}

	public List getTerms() {
		return terms;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		Predicate p = (Predicate) o;
		boolean nameEquality = p.getPredicateName().equals(getPredicateName());
		boolean termEquality = p.getTerms().equals(getTerms());
		return ((nameEquality)) && (termEquality);

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
		String pre = " " + predicateName + "( ";
		String mid = "";
		for (int i = 0; i < terms.size(); i++) {

			mid += "," + ((FOLNode) terms.get(i)).toString();
		}
		mid = mid.substring(1);
		String post = " ) ";
		return pre + mid + post;
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