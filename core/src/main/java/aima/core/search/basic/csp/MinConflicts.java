package aima.core.search.basic.csp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import aima.core.search.api.Assignment;
import aima.core.search.api.CSP;
import aima.core.search.api.Constraint;
import aima.core.search.api.SearchForAssignmentFunction;
import aima.core.search.basic.support.BasicAssignment;

/**
 * Artificial Intelligence A Modern Approach (4th Ed.): Figure ??, Page ???.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function MIN-CONFLICTS(csp, max_steps) returns a solution or failure
 *    inputs: csp, a constraint satisfaction problem
 *            max_steps, the number of steps allowed before giving up
 *    current &larr; an initial complete assignment for csp
 *    for i = 1 to max steps do
 *       if current is a solution for csp then return current
 *       var &larr; a randomly chosen conflicted variable from csp.VARIABLES
 *       value &larr; the value v for var that minimizes CONFLICTS(var, v, current, csp)
 *       set var = value in current
 *    return failure
 * </code>
 * </pre>
 * 
 * Figure ?? The MIN-CONFLICTS algorithm for solving CSPs by local search. The
 * initial state may be chosen randomly or by a greedy assignment process that
 * chooses a minimal-conflict value for each variable in turn. The CONFLICTS
 * function counts the number of constraints violated by a particular value,
 * given the rest of the current assignment.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class MinConflicts implements SearchForAssignmentFunction {

	/**
	 * function MIN-CONFLICTS(csp, max_steps)
	 * 
	 * @param csp
	 *            a constraint satisfaction problem
	 * @param max_steps
	 *            the number of steps allowed before giving up
	 * @return a solution or failure
	 */
	public Assignment minConflicts(CSP csp, int max_steps) {
		// current <- an initial complete assignment for csp
		Assignment current = initialCompleteAssignment(csp);
		// for i = 1 to max steps do
		for (int i = 1; i <= max_steps; i++) {
			// if current is a solution for csp then return current
			if (current.isSolution(csp)) {
				return current;
			}
			// var <- a randomly chosen conflicted variable from csp.VARIABLES
			String var = randomlyChooseConflictedVariable(csp, current);
			// value <- the value v for var that minimizes
			// CONFLICTS(var, v, current, csp)
			Object value = csp.getDomain(var).getValues().stream()
					.min(Comparator.comparingInt(v -> conflicts(var, v, current, csp))).get();
			// set var = value in current
			current.add(var, value);
		}
		return failure();
	}

	//
	// Supporting Code
	private Random random = null;
	private Function<CSP, Assignment> initialCompleteAssignmentFn = null;

	public MinConflicts() {

	}

	public MinConflicts(Function<CSP, Assignment> initialCompleteAssignmentFn, Random random) {
		setInitialCompleteAssignmentInOrderFunction(initialCompleteAssignmentFn);
		setRandom(random);
	}

	@Override
	public Assignment apply(CSP csp) {
		return minConflicts(csp, Integer.MAX_VALUE);
	}

	public Assignment initialCompleteAssignment(CSP csp) {
		return getInitialCompleteAssignmentFunction().apply(csp);
	}

	public Function<CSP, Assignment> getInitialCompleteAssignmentFunction() {
		if (initialCompleteAssignmentFn == null) {
			initialCompleteAssignmentFn = getInitialCompleteAssignmentInOrderFunction();
		}
		return initialCompleteAssignmentFn;
	}

	// Trivial implementation: Just assign the first allowed value from
	// each variable's domain.
	public static Function<CSP, Assignment> getInitialCompleteAssignmentInOrderFunction() {
		return (csp) -> {
			Assignment complete = new BasicAssignment();

			for (String var : csp.getVariables()) {
				complete.add(var, csp.getDomain(var).getValues().get(0));
			}

			return complete;
		};
	}

	public void setInitialCompleteAssignmentInOrderFunction(Function<CSP, Assignment> initialCompleteAssignmentFn) {
		this.initialCompleteAssignmentFn = initialCompleteAssignmentFn;
	}

	public Random getRandom() {
		if (random == null) {
			random = new Random(0);
		}
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public String randomlyChooseConflictedVariable(CSP csp, Assignment completeAssignment) {
		Set<String> conflictedVariables = new LinkedHashSet<>();
		for (Constraint c : csp.getConstraints()) {
			Object[] values = new Object[c.getScope().size()];
			for (int i = 0; i < values.length; i++) {
				values[i] = completeAssignment.getAssignment(c.getScope().get(i));
			}
			if (!c.getRelation().isMember(values)) {
				conflictedVariables.addAll(c.getScope());
			}
		}
		return new ArrayList<>(conflictedVariables).get(getRandom().nextInt(conflictedVariables.size()));
	}

	public int conflicts(String var, Object value, Assignment completeAssignment, CSP csp) {
		int nconflicts = 0;

		for (Constraint c : csp.getNeighboringConstraints(var)) {
			Object[] values = new Object[c.getScope().size()];
			for (int i = 0; i < values.length; i++) {
				String scopeVar = c.getScope().get(i);
				if (var.equals(scopeVar)) {
					values[i] = value;
				} else {
					values[i] = completeAssignment.getAssignment(scopeVar);
				}
			}
			if (!c.getRelation().isMember(values)) {
				nconflicts++;
			}
		}

		return nconflicts;
	}

	public Assignment failure() {
		return null;
	}
}
