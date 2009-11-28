package aima.core.learning.inductive;

import java.util.Hashtable;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;

/**
 * @author Ravi Mohan
 * 
 */
public class DLTest {

	// represents a single test in the Decision List
	private Hashtable<String, String> attrValues;

	public DLTest() {
		attrValues = new Hashtable<String, String>();
	}

	public void add(String nta, String ntaValue) {
		attrValues.put(nta, ntaValue);

	}

	public boolean matches(Example e) {
		for (String key : attrValues.keySet()) {
			if (!(attrValues.get(key).equals(e.getAttributeValueAsString(key)))) {
				return false;
			}
		}
		return true;
		// return e.targetValue().equals(targetValue);
	}

	public DataSet matchedExamples(DataSet ds) {
		DataSet matched = ds.emptyDataSet();
		for (Example e : ds.examples) {
			if (matches(e)) {
				matched.add(e);
			}
		}
		return matched;
	}

	public DataSet unmatchedExamples(DataSet ds) {
		DataSet unmatched = ds.emptyDataSet();
		for (Example e : ds.examples) {
			if (!(matches(e))) {
				unmatched.add(e);
			}
		}
		return unmatched;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("IF  ");
		for (String key : attrValues.keySet()) {
			buf.append(key + " = ");
			buf.append(attrValues.get(key) + " ");
		}
		buf.append(" DECISION ");
		return buf.toString();
	}
}
