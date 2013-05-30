package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.propositional.Connective;
import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class UnarySentence extends ComplexSentence {
	private Connective connective;

	private Sentence first;

	public UnarySentence(Connective connective, Sentence first) {
		this.connective = connective;
		this.first = first;
	}

	public Sentence getFirst() {
		return first;
	}

	public Connective getConnective() {
		return connective;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		UnarySentence us = (UnarySentence) o;
		return (us.getConnective().equals(getConnective()))
				&& (us.getFirst().equals(first));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + first.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return  getConnective() + " " + first.toString();
	}

	@Override
	public Object accept(PLVisitor plv, Object arg) {
		return plv.visitNotSentence(this, arg);
	}

	public boolean isNot() {
		return (getConnective().equals(Connective.NOT));
	}
}