package aima.core.search.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Each state in a CSP is defined by an assignment. An assignment assigns values
 * to some or all of the variables of a CSP.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface Assignment {
	/**
	 * 
	 * @return a map of named variables and their corresponding assignments.
	 */
	Map<String, Object> getAssignments();

	Object add(String var, Object value);	

	boolean remove(String var, Object value);

	boolean reducedDomain(String var, Object value);

	Map<String, List<Object>> getDomainsReducedBy();

	boolean add(Assignment assignment);
	
	default boolean remove(Assignment assignment, CSP csp) {
		// Set to true if any changes in this assignment or the CSP occur as a
		// result of removing the given assignment's values.
		boolean removed = false;
		// Remove the individual var = value assignments
		for (Map.Entry<String, Object> entry : assignment.getAssignments().entrySet()) {
			if (remove(entry.getKey(), entry.getValue())) {
				removed = true;
			}
		}
		// Restore relevant domains
		for (Map.Entry<String, List<Object>> varDomainReducedBy : assignment.getDomainsReducedBy().entrySet()) {
			Domain domain = csp.getDomains().get(csp.indexOf(varDomainReducedBy.getKey()));
			for (Object valueToRestore : varDomainReducedBy.getValue()) {
				if (domain.restore(valueToRestore)) {
					removed = true;
				}
			}
		}

		return removed;
	}

	/**
	 * Determine if an assignment is complete, where every variable is assigned.
	 * 
	 * @param csp.
	 *            the CSP for which the assignment is to be tested.
	 * @return true if the assignment is complete, false otherwise.
	 */
	default boolean isComplete(CSP csp) {
		return getAssignments().keySet().containsAll(csp.getVariables());
	}

	default boolean isPartial(CSP csp) {
		return !isComplete(csp);
	}

	default boolean isConsistent(CSP csp) {
		return !violates(getAssignments(), csp);
	}

	default boolean isSolution(CSP csp) {
		return isComplete(csp) && isConsistent(csp);
	}

	default boolean isConsistent(String var, Object value, CSP csp) {
		Map<String, Object> assignments = new HashMap<>(getAssignments());
		assignments.put(var, value);
		return !violates(assignments, csp);
	}

	default boolean violates(Map<String, Object> assignedValues, CSP csp) {
		return csp.getConstraints().stream()
				// An assignment can only violate a constraint if it has values
				// for all the variables in its scope.
				.filter(constraint -> assignedValues.keySet().containsAll(constraint.getScope()))
				.anyMatch(constraint -> {
					Object[] values = new Object[constraint.getScope().size()];
					for (int i = 0; i < values.length; i++) {
						values[i] = assignedValues.get(constraint.getScope().get(i));
					}
					return !constraint.getRelation().isMember(values);
				});
	}
}