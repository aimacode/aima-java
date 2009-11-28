package aima.core.agent.impl.aprog.simplerule;

import aima.core.agent.Action;
import aima.core.agent.impl.ObjectWithDynamicAttributes;

/**
 *  A simple implementation of a "condition-action rule".
 *
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Rule {
	private Condition con;

	private Action action;

	public Rule(Condition aCon, Action anAction) {
		assert (null != aCon);
		assert (null != anAction);

		con = aCon;
		action = anAction;
	}

	public boolean evaluate(ObjectWithDynamicAttributes p) {
		return (con.evaluate(p));
	}

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
