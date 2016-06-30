package aima.core.logic.basic.firstorder.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.logic.basic.firstorder.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class ConnectedSentence implements Sentence {
	private String connector;
	private Sentence first, second;
	private List<Sentence> args = new ArrayList<Sentence>();
	private String stringRep = null;
	private int hashCode = 0;

	public ConnectedSentence(String connector, Sentence first, Sentence second) {
		this.connector = connector;
		this.first = first;
		this.second = second;
		args.add(first);
		args.add(second);
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

	//
	// START-Sentence
	public String getSymbolicName() {
		return getConnector();
	}

	public boolean isCompound() {
		return true;
	}

	public List<Sentence> getArgs() {
		return Collections.unmodifiableList(args);
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitConnectedSentence(this, arg);
	}

	public ConnectedSentence copy() {
		return new ConnectedSentence(connector, first.copy(), second.copy());
	}

	// END-Sentence
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		ConnectedSentence cs = (ConnectedSentence) o;
		return cs.getConnector().equals(getConnector()) && cs.getFirst().equals(getFirst())
				&& cs.getSecond().equals(getSecond());
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + getConnector().hashCode();
			hashCode = 37 * hashCode + getFirst().hashCode();
			hashCode = 37 * hashCode + getSecond().hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			sb.append(first.toString());
			sb.append(" ");
			sb.append(connector);
			sb.append(" ");
			sb.append(second.toString());
			sb.append(")");
			stringRep = sb.toString();
		}
		return stringRep;
	}
}
