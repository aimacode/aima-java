package aima.basic.simplerule;

import aima.basic.ObjectWithDynamicAttributes;

/**
 * Implementation of a NOT condition.
 *
 */

/**
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
		StringBuffer sb = new StringBuffer();

		return sb.append("![").append(con).append("]").toString();

	}
}