/*
 * Created on Sep 21, 2004
 *
 */
package aima.search.csp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

public class Domain {
	private Hashtable<String, List<Object>> variablesToValues;

	// a hash Of Lists { variable: ListOfDomainValues}
	public Domain(List<String> variables) {
		this.variablesToValues = new Hashtable<String, List<Object>>();
		Iterator<String> varIter = variables.iterator();
		while (varIter.hasNext()) {
			variablesToValues.put(varIter.next(), new ArrayList<Object>());
		}
	}

	public List<Object> getDomainOf(String variable) {
		return variablesToValues.get(variable);
	}

	public void add(String variable, Object value) {
		List<Object> varDomains = variablesToValues.get(variable);

		if (!(varDomains.contains(value))) {
			varDomains.add(value);
		}
	}

	public void addToDomain(String variable, List values) {
		for (int i = 0; i < values.size(); i++) {
			add(variable, values.get(i));
		}

	}

	public void remove(String variable, Object value) {
		List varDomains = variablesToValues.get(variable);
		varDomains.remove(value);
	}

	@Override
	public String toString() {
		return variablesToValues.toString();
	}

}