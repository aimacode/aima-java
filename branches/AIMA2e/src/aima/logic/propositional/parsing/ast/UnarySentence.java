/*
 * Created on Sep 15, 2003 by Ravi Mohan
 *  
 */
package aima.logic.propositional.parsing.ast;

import aima.logic.propositional.parsing.PLVisitor;
import aima.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */

public class UnarySentence extends ComplexSentence {
	private Sentence negated;

	public Sentence getNegated() {
		return negated;
	}

	public UnarySentence(Sentence negated) {
		this.negated = negated;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		UnarySentence ns = (UnarySentence) o;
		return (ns.negated.equals(negated));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + negated.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return " ( NOT " + negated.toString() + " ) ";
	}

	@Override
	public Object accept(PLVisitor plv, Object arg) {
		return plv.visitNotSentence(this, arg);
	}
}