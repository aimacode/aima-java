/*
 * Created on Sep 18, 2004
 *
 */
package aima.logic.fol.parsing.ast;

import aima.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public class ConnectedSentence extends ParanthizedSentence {

	private Sentence first, second;

	private String connector;

	public ConnectedSentence(String connector, Sentence first, Sentence second) {
		super();
		this.first = first;
		this.second = second;
		this.connector = connector;

	}

	public String getConnector() {
		return connector;
	}

	public Sentence getFirst() {
		return first;
	}

	public Sentence getSecond() {
		return second;
	}

	public void setFirst(Sentence first) {
		this.first = first;
	}

	public void setSecond(Sentence second) {
		this.second = second;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		ConnectedSentence cs = (ConnectedSentence) o;
		return ((cs.getConnector().equals(getConnector()))
				&& (cs.getFirst().equals(getFirst())) && (cs.getSecond()
				.equals(getSecond())));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + getConnector().hashCode();
		result = 37 * result + getFirst().hashCode();
		result = 37 * result + getSecond().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return " (" + first.toString() + " " + connector + " "
				+ second.toString() + " )";
	}

	@Override
	public Object accept(FOLVisitor v, Object arg) {

		return v.visitConnectedSentence(this, arg);
	}

	@Override
	public FOLNode copy() {
		return new ConnectedSentence(connector, (Sentence) first.copy(),
				(Sentence) second.copy());
	}

}
