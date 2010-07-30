package aima.core.search.csp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Maintains a set of domains and a mapping, which maps variables
 * to their corresponding domains.
 * 
 * @author Ruediger Lunde
 */
public class Domains {
	/**
	 * Maps variables to their corresponding domain.
	 * Domains are represented by lists of objects.
	 */
	private Hashtable<Variable, List<Object>> variableToValues;

	public Domains(List<Variable> vars) {
		variableToValues = new Hashtable<Variable, List<Object>>();
		for (Variable var : vars)
			variableToValues.put(var, new ArrayList<Object>());
	}

	public List<Object> getDomain(Variable var) {
		return variableToValues.get(var);
	}
	
	public void setDomain(Variable var, List<?> values) {
		variableToValues.get(var).clear();
		for (Object value : values)
			addToDomain(var, value);
	}

	public void addToDomain(Variable var, Object value) {
		List<Object> varValues = variableToValues.get(var);
		if (!(varValues.contains(value))) {
			varValues.add(value);
		}
	}

	public void removeFromDomain(Variable var, Object value) {
		List<Object> varValues = variableToValues.get(var);
		varValues.remove(value);
	}

	@Override
	public String toString() {
		return variableToValues.toString();
	}
}