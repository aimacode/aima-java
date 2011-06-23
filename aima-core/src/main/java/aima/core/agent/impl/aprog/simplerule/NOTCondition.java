package aima.core.agent.impl.aprog.simplerule;

import aima.core.agent.impl.ObjectWithDynamicAttributes;

/**
 * Implementation of a NOT condition.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class NOTCondition extends Condition {
	private Condition con;

	public NOTCondition(Condition aCon) {
		assert (null != aCon);

		con = aCon;
	}

	@Override
	public boolean evaluate(ObjectWithDynamicAttributes p) {
		return (!con.evaluate(p));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		return sb.append("![").append(con).append("]").toString();
	}
}