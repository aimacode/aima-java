package aima.core.agent.impl.aprog.simplerule;

import aima.core.agent.Action;
import aima.core.agent.impl.ObjectWithDynamicAttributes;

/**
 * A simple implementation of a "condition-action rule".
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class Rule {
	private Condition con;

	private Action action;

	/**
	 * Constructs a condition-action rule.
	 * 
	 * @param aCon
	 *            a condition
	 * @param anAction
	 *            an action
	 */
	public Rule(Condition aCon, Action anAction) {
		assert (null != aCon);
		assert (null != anAction);

		con = aCon;
		action = anAction;
	}

	public boolean evaluate(ObjectWithDynamicAttributes p) {
		return (con.evaluate(p));
	}

	/**
	 * Returns the action of this condition-action rule.
	 * 
	 * @return the action of this condition-action rule.
	 */
	public Action getAction() {
		return action;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Rule)) {
			return super.equals(o);
		}
		return (toString().equals(((Rule) o).toString()));
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.append("if ").append(con).append(" then ").append(action)
				.append(".").toString();
	}
}
