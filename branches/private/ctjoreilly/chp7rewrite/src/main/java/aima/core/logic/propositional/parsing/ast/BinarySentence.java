package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.propositional.Connective;
import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class BinarySentence extends ComplexSentence {
	private Connective connective;

	private Sentence first;

	private Sentence second;

	public BinarySentence(Connective connective, Sentence first, Sentence second) {
		this.connective = connective;
		this.first = first;
		this.second = second;

	}

	public Sentence getFirst() {
		return first;
	}

	public Connective getConnective() {
		return connective;
	}

	public Sentence getSecond() {
		return second;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		BinarySentence bs = (BinarySentence) o;
		return ((bs.getConnective().equals(getConnective()))
				&& (bs.getFirst().equals(first)) && (bs.getSecond()
				.equals(second)));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + first.hashCode();
		result = 37 * result + second.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return " ( " + first.toString() + " " + getConnective() + " "
				+ second.toString() + " )";
	}

	@Override
	public Object accept(PLVisitor plv, Object arg) {
		return plv.visitBinarySentence(this, arg);
	}

	public boolean isOrSentence() {
		return (getConnective().equals(Connective.OR));
	}

	public boolean isAndSentence() {
		return (getConnective().equals(Connective.AND));
	}

	public boolean isImplication() {
		return (getConnective().equals(Connective.IMPLICATION));
	}

	public boolean isBiconditional() {
		return (getConnective().equals(Connective.BICONDITIONAL));
	}

	public boolean firstTermIsAndSentence() {
		return (getFirst() instanceof BinarySentence)
				&& (((BinarySentence) getFirst()).isAndSentence());
	}

	public boolean secondTermIsAndSentence() {
		return (getSecond() instanceof BinarySentence)
				&& (((BinarySentence) getSecond()).isAndSentence());
	}
}