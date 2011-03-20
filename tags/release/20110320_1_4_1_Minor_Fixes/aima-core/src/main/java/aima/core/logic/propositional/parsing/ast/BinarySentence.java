package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class BinarySentence extends ComplexSentence {
	private String operator;

	private Sentence first;

	private Sentence second;

	public BinarySentence(String operator, Sentence first, Sentence second) {
		this.operator = operator;
		this.first = first;
		this.second = second;

	}

	public Sentence getFirst() {
		return first;
	}

	public String getOperator() {
		return operator;
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
		return ((bs.getOperator().equals(getOperator()))
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
		return " ( " + first.toString() + " " + operator + " "
				+ second.toString() + " )";
	}

	@Override
	public Object accept(PLVisitor plv, Object arg) {
		return plv.visitBinarySentence(this, arg);
	}

	public boolean isOrSentence() {
		return (getOperator().equals("OR"));
	}

	public boolean isAndSentence() {
		return (getOperator().equals("AND"));
	}

	public boolean isImplication() {
		return (getOperator().equals("=>"));
	}

	public boolean isBiconditional() {
		return (getOperator().equals("<=>"));
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