package aima.core.search.csp;

import aima.core.util.Tasks;
import aima.core.util.Util;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.8, Page 221.<br>
 * <br>
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
 * @param <VAR> Type which is used to represent variables
 * @param <VAL> Type which is used to represent the values in the domains
 *
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class MinConflictsSolver<VAR extends Variable, VAL> extends CspSolver<VAR, VAL> {
	private int maxSteps;

	/**
	 * Constructs a min-conflicts strategy with a given number of steps allowed
	 * before giving up.
	 * 
	 * @param maxSteps
	 *            the number of steps allowed before giving up
	 */
	public MinConflictsSolver(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	public Optional<Assignment<VAR, VAL>> solve(CSP<VAR, VAL> csp) {
		Assignment<VAR, VAL> current = generateRandomAssignment(csp);
		fireStateChanged(csp, current, null);
		for (int i = 0; i < maxSteps && !Tasks.currIsCancelled(); i++) {
			if (current.isSolution(csp)) {
				return Optional.of(current);
			} else {
				Set<VAR> vars = getConflictedVariables(current, csp);
				VAR var = Util.selectRandomlyFromSet(vars);
				VAL value = getMinConflictValueFor(var, current, csp);
				current.add(var, value);
				fireStateChanged(csp, current, var);
			}
		}
		return Optional.empty();
	}

	private Assignment<VAR, VAL> generateRandomAssignment(CSP<VAR, VAL> csp) {
		Assignment<VAR, VAL> result = new Assignment<>();
		for (VAR var : csp.getVariables()) {
			VAL randomValue = Util.selectRandomlyFromList(csp.getDomain(var).asList());
			result.add(var, randomValue);
		}
		return result;
	}

	private Set<VAR> getConflictedVariables(Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
		Set<VAR> result = new LinkedHashSet<>();
		csp.getConstraints().stream().filter(constraint -> !constraint.isSatisfiedWith(assignment)).
				forEach(constraint -> constraint.getScope().stream().filter(var -> !result.contains(var)).
						forEach(result::add));
		return result;
	}

	private VAL getMinConflictValueFor(VAR var, Assignment<VAR, VAL> assignment, CSP<VAR, VAL> csp) {
		List<Constraint<VAR, VAL>> constraints = csp.getConstraints(var);
		Assignment<VAR, VAL> testAssignment = assignment.clone();
		int minConflict = Integer.MAX_VALUE;
		List<VAL> resultCandidates = new ArrayList<>();
		for (VAL value : csp.getDomain(var)) {
			testAssignment.add(var, value);
			int currConflict = countConflicts(testAssignment, constraints);
			if (currConflict <= minConflict) {
				if (currConflict < minConflict) {
					resultCandidates.clear();
					minConflict = currConflict;
				}
				resultCandidates.add(value);
			}
		}
		 return (!resultCandidates.isEmpty()) ? Util.selectRandomlyFromList(resultCandidates) : null;
	}

	private int countConflicts(Assignment<VAR, VAL> assignment,
			List<Constraint<VAR, VAL>> constraints) {
		int result = 0;
		for (Constraint<VAR, VAL> constraint : constraints)
			if (!constraint.isSatisfiedWith(assignment))
				result++;
		return result;
	}
}
