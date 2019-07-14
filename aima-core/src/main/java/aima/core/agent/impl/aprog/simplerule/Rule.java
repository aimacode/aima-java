package aima.core.agent.impl.aprog.simplerule;

import aima.core.agent.impl.ObjectWithDynamicAttributes;

/**
 * A simple implementation of a "condition-action rule".
 *
 * @param <A> Type which is used to represent actions
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class Rule<A> {
	private Condition con;
	private A action;

	/**
	 * Constructs a condition-action rule.
	 * @param con
	 *            a condition
	 * @param action
	 *            an action, possibly null
	 */
	public Rule(Condition con, A action) {
		assert (null != con);

		this.con = con;
		this.action = action;
	}

	public boolean evaluate(ObjectWithDynamicAttributes p) {
		return (con.evaluate(p));
	}

	/**
	 * Returns the action of this condition-action rule.
	 * 
	 * @return the action of this condition-action rule.
	 */
	public A getAction() {
		return action;
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o.getClass() == getClass() && toString().equals(((Rule) o).toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return "if " + con + " then " + action + ".";
	}
}
