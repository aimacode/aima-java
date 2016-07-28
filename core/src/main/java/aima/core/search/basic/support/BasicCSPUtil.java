package aima.core.search.basic.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import aima.core.search.api.Assignment;
import aima.core.search.api.CSP;
import aima.core.search.api.Constraint;
import aima.core.util.collect.CartesianProduct;

/**
 * Some basic utility routines for CSPs.
 * 
 * @author Ciaran O'Reilly
 *
 */
public class BasicCSPUtil {
	public static int getNumberNeigboringConflicts(String variable, Object value, CSP csp,
			Assignment currentAssignment) {
		int nconflicts = 0;

		// Use a local assignment and update to have the currently set value for
		// the input variable
		Assignment assignment = new BasicAssignment(currentAssignment);
		assignment.add(variable, value);

		// Get all of the neighboring variables along with the input variable
		// So we can identify constraints that are covered by their scope
		Set<String> neighborVariables = new LinkedHashSet<>(csp.getNeighbors(variable));
		neighborVariables.add(variable);

		// Determine the constraints covered by the neighboring set of variables
		List<Constraint> neighboringConstraints = csp.getConstraints().stream()
				.filter(constraint -> neighborVariables.containsAll(constraint.getScope()))
				.collect(Collectors.toList());

		// Based on the assignment and neighboring values, determine the set of
		// allowed assignments for each variable.
		Map<String, List<Object>> allowedAssignments = new LinkedHashMap<>();
		for (String var : neighborVariables) {
			List<Object> allowed = new ArrayList<>();
			if (assignment.contains(var)) {
				allowed.add(assignment.getAssignment(var));
			} else {
				allowed.addAll(csp.getDomainValues(var));
			}
			allowedAssignments.put(var, allowed);
		}

		// Determine the # of conflicts
		List<List<? extends Object>> possibleValues = new ArrayList<>();
		for (Constraint constraint : neighboringConstraints) {
			// Collect the possible values for each variable in
			// the contraint's scope
			possibleValues.clear();
			constraint.getScope().forEach(scopeVar -> {
				possibleValues.add(allowedAssignments.get(scopeVar));
			});
			// For each combination of possible values, count
			// the # that are not a member of the constraint's
			// relation (i.e. the # of possible conflicts).
			Iterator<Object[]> allowedValuesIt = new CartesianProduct<Object>(Object.class, possibleValues).iterator();
			while (allowedValuesIt.hasNext()) {
				if (!constraint.getRelation().isMember(allowedValuesIt.next())) {
					nconflicts++;
				}
			}
		}

		return nconflicts;
	}
}
