package aima.core.search.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Each state in a CSP is defined by an assignment. An assignment assigns values
 * to some or all of the variables of a CSP.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface Assignment {
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

	//
	// Assignment tracking
	/**
	 * 
	 * @return a map of named variables and their corresponding assignments.
	 */
	default Object getAssignment(String var) {
		return getAssignments().get(var);
	}

	default Map<String, List<Object>> getAllowedAssignments(CSP csp, Collection<String> variables) {
		Map<String, List<Object>> allowedAssignments = new LinkedHashMap<>();
		for (String var : variables) {
			List<Object> allowed = new ArrayList<>();
			if (contains(var)) {
				allowed.add(getAssignment(var));
			} else {
				allowed.addAll(csp.getDomainValues(var));
			}
			allowedAssignments.put(var, allowed);
		}
		return allowedAssignments;
	}

	Map<String, Object> getAssignments();

	Object add(String var, Object value);

	boolean remove(String var, Object value);

	default boolean contains(String var) {
		return getAssignments().containsKey(var);
	}
	
	default List<String> getUnassignedNeigbors(CSP csp, String var) {
		return csp.getNeighbors(var).stream().filter(nvar -> !nvar.equals(var) && !contains(nvar))
				.collect(Collectors.toList());
	}

	//
	// Domain tracking
	boolean reducedDomain(String var, Object value);

	boolean restoredDomain(String var, Object value);

	Map<String, List<Object>> getDomainsReducedBy();

	default boolean add(Assignment otherAssignment) {
		boolean added = false;
		// Track the assignments from the other assignment
		for (Map.Entry<String, Object> entry : otherAssignment.getAssignments().entrySet()) {
			add(entry.getKey(), entry.getValue());
			added = true;
		}
		// Track the domain reductions from the other assignment
		for (Map.Entry<String, List<Object>> varDomainReducedBy : otherAssignment.getDomainsReducedBy().entrySet()) {
			String variable = varDomainReducedBy.getKey();
			for (Object valueToReduce : varDomainReducedBy.getValue()) {
				if (reducedDomain(variable, valueToReduce)) {
					added = true;
				}
			}
		}
		return added;
	}

	//
	// Remove the effects of an assignment from this assignment and the
	// corresponding CSP (in order to restore domains).
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
			String variable = varDomainReducedBy.getKey();
			Domain domain = csp.getDomain(variable);
			for (Object valueToRestore : varDomainReducedBy.getValue()) {
				if (restoredDomain(variable, valueToRestore)) {
					removed = true;
				}
				if (domain.restore(valueToRestore)) {
					removed = true;
				}
			}
		}

		return removed;
	}

	/**
	 * Execute a Runnable's run() method once this Assignment has registered
	 * itself as a listener to all of the domains on the CSP given. Once the
	 * Runnable's run() method is complete this Assignment will deregister
	 * itself as a listener on all of the CSP's domains.
	 * 
	 * @param csp
	 *            the CSP whose domains are to be listened to for changes.
	 * @param executionBlockRunner
	 *            the Runnable whose run() method is to be called once the
	 *            Assignment has registered itself as a listener on all of the
	 *            CSP's domains.
	 */
	default void executeInCSPListenerBlock(CSP csp, Runnable executionBlockRunner) {
		// Add domain listeners in order to track any changes in the domains
		// of the CSP
		final Map<Domain, Domain.Listener> domainListeners = new HashMap<>();
		csp.getVariables().forEach(var -> {
			Domain domain = csp.getDomain(var);
			Domain.Listener l = new Domain.Listener() {
				@Override
				public void deleted(Domain domain, Object value) {
					reducedDomain(var, value);
				}
			};
			domain.addDomainListener(l);
			domainListeners.put(domain, l);
		});

		executionBlockRunner.run();

		domainListeners.entrySet().forEach(entry -> entry.getKey().removeDomainListener(entry.getValue()));
	}
}