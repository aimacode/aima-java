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
	//TODO remove <Object> if possible
	private Hashtable<String,List<Object>> hash;

	//a hash Of Lists { variable: ListOfDomainValues}
	public Domain(List<String> variables) {
		this.hash = new Hashtable<String,List<Object>>();
		Iterator<String> varIter = variables.iterator();
		while (varIter.hasNext()) {
			hash.put(varIter.next(), new ArrayList<Object>());
		}
	}

	public List<Object> getDomainOf(String variable) {
		return  hash.get(variable);
	}

	public void addToDomain(String variable, Object value) {
		List<Object> varDomains =  hash.get(variable);

		if (!(varDomains.contains(value))) {
			varDomains.add(value);
		}
	}

	public void addToDomain(String variable, List values) {
		List varDomains = (List) hash.get(variable);
		for (int i = 0; i < values.size(); i++) {
			addToDomain(variable, values.get(i));
		}

	}

	public void removeFromDomain(String variable, Object value) {
		List varDomains = (List) hash.get(variable);
		varDomains.remove(value);
	}

	public String toString() {
		return hash.toString();
	}

}