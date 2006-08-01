/*
 * Created on Sep 14, 2003 by Ravi Mohan
 *
 */
package aima.logic.fol.parsing.ast;

import aima.logic.fol.parsing.FOLVisitor;

public class NotSentence implements Sentence {
	private Sentence negated;

	public NotSentence(Sentence negated) {
		this.negated = negated;
	}

	public Sentence getNegated() {
		return negated;
	}

	public FOLNode copy() {
		return new NotSentence((Sentence) negated.copy());
	}

	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		NotSentence ns = (NotSentence) o;
		return (ns.negated.equals(negated));

	}
	
	public int hashCode(){
		int result = 17;
		result = 37 * result + negated.hashCode();
		return result;
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitNotSentence(this, arg);
	}

	public String toString() {
		return " NOT " + negated.toString();
	}

}
