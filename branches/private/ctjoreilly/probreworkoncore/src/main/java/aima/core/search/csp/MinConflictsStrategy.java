package aima.core.search.csp;

import java.util.ArrayList;
import java.util.List;

import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.8, Page 221.
 * 
 * <pre>
 * <code>
 * function MIN-CONFLICTS(csp, max-steps) returns a solution or failure
 *    inputs: csp, a constraint satisfaction problem
 *            max-steps, the number of steps allowed before giving up
 *    current = an initial complete assignment for csp
 *    for i = 1 to max steps do
 *       if current is a solution for csp then return current
 *       var = a randomly chosen conflicted variable from csp.VARIABLES
 *       value = the value v for var that minimizes CONFLICTS(var, v, current, csp)
 *       set var = value in current
 *    return failure
 * </code>
 * </pre>
 * 
 * Figure 6.8 The MIN-CONFLICTS algorithm for solving CSPs by local search. The
 * initial state may be chosen randomly or by a greedy assignment process that
 * chooses a minimal-conflict value for each variable in turn. The CONFLICTS
 * function counts the number of constraints violated by a particular value,
 * given the rest of the current assignment.
 * 
 * @author Ruediger Lunde
 */
public class MinConflictsStrategy extends SolutionStrategy {
	private int maxSteps;

	public MinConflictsStrategy(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	public Assignment solve(CSP csp) {
		Assignment assignment = generateRandomAssignment(csp);
		fireStateChanged(assignment, csp);
		for (int i = 0; i < maxSteps; i++) {
			if (assignment.isSolution(csp)) {
				return assignment;
			} else {
				List<Variable> vars = getConflictedVariables(assignment, csp);
				Variable var = Util.selectRandomlyFromList(vars);
				Object value = getMinConflictValueFor(var, assignment, csp);
				assignment.setAssignment(var, value);
				fireStateChanged(assignment, csp);
			}
		}
		return null;
	}

	private Assignment generateRandomAssignment(CSP csp) {
		Assignment assignment = new Assignment();
		for (Variable var : csp.getVariables()) {
			Object randomValue = Util.selectRandomlyFromList(csp.getDomain(var)
					.asList());
			assignment.setAssignment(var, randomValue);
		}
		return assignment;
	}

	private List<Variable> getConflictedVariables(Assignment assignment, CSP csp) {
		List<Variable> result = new ArrayList<Variable>();
		for (Constraint constraint : csp.getConstraints()) {
			if (!constraint.isSatisfiedWith(assignment))
				for (Variable var : constraint.getScope())
					if (!result.contains(var))
						result.add(var);
		}
		return result;
	}

	private Object getMinConflictValueFor(Variable var, Assignment assignment,
			CSP csp) {
		List<Constraint> constraints = csp.getConstraints(var);
		Assignment duplicate = assignment.copy();
		int minConflict = Integer.MAX_VALUE;
		List<Object> resultCandidates = new ArrayList<Object>();
		for (Object value : csp.getDomain(var)) {
			duplicate.setAssignment(var, value);
			int currConflict = countConflicts(duplicate, constraints);
			if (currConflict <= minConflict) {
				if (currConflict < minConflict) {
					resultCandidates.clear();
					minConflict = currConflict;
				}
				resultCandidates.add(value);
			}
		}
		if (!resultCandidates.isEmpty())
			return Util.selectRandomlyFromList(resultCandidates);
		else
			return null;
	}

	private int countConflicts(Assignment assignment,
			List<Constraint> constraints) {
		int result = 0;
		for (Constraint constraint : constraints)
			if (!constraint.isSatisfiedWith(assignment))
				result++;
		return result;
	}
}
