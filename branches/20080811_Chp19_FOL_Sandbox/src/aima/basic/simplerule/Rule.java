package aima.basic.simplerule;

import aima.basic.ObjectWithDynamicAttributes;

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

	private String action;

	public Rule(Condition aCon, String anAction) {
		assert (null != aCon);
		assert (null != anAction);

		con = aCon;
		action = anAction;
	}

	public boolean evaluate(ObjectWithDynamicAttributes p) {
		return (con.evaluate(p));
	}

	public String getAction() {
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
		StringBuffer sb = new StringBuffer();

		return sb.append("if ").append(con).append(" then ").append(action)
				.append(".").toString();
	}
}
