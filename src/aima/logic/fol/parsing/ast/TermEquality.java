/*
 * Created on Sep 14, 2003 by Ravi Mohan
 *  
 */
package aima.logic.fol.parsing.ast;

import aima.logic.fol.parsing.FOLVisitor;

public class TermEquality implements Sentence {
	private Term term1, term2;

	public void setTerm1(Sentence term1) {
		this.term1 = (Term) term1;
	}

	public void setTerm2(Sentence term2) {
		this.term2 = (Term) term2;
	}

	public Term getTerm1() {
		return term1;
	}

	public Term getTerm2() {
		return term2;
	}

	public TermEquality(Term term1, Term term2) {
		this.term1 = term1;
		this.term2 = term2;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		TermEquality te = (TermEquality) o;
		boolean term1eq = (te.getTerm1().equals(term1));

		boolean eq = (term1eq && (te.getTerm2().equals(term2)));

		return eq;

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + getTerm1().hashCode();
		result = 37 * result + getTerm2().hashCode();
		return result;
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitTermEquality(this, arg);
	}

	@Override
	public String toString() {
		String pre = term1.toString();
		String mid = " = ";
		String post = term2.toString();
		return pre + mid + post;
	}

	public FOLNode copy() {
		return new TermEquality(term1.copy(), term2.copy());
	}
}