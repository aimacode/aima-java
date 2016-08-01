package aima.core.search.basic.csp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import aima.core.search.api.Assignment;
import aima.core.search.api.CSP;
import aima.core.search.api.Constraint;
import aima.core.search.api.SearchForAssignmentFunction;
import aima.core.search.basic.support.BasicAssignment;
import aima.core.search.basic.support.BasicCSPUtil;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (4th Ed.): Figure ??, Page ??.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function BACKTRACKING-SEARCH(csp) returns a solution, or failure
 *    return BACKTRACK({ }, csp)
 * 
 * function BACKTRACK(assignment, csp) returns a solution, or failure
 *    if assignment is complete then return assignment
 *    var &larr; SELECT-UNASSIGNED-VARIABLE(assignment, csp)
 *    for each value in ORDER-DOMAIN-VALUES(var, assignment, csp) do
 *       if value is consistent with assignment then
 *          add {var = value} to assignment
 *          inferences &larr; INFERENCE(csp, assignment, var, value)
 *          if inferences &ne; failure then
 *             add inferences to assignment
 *             result &larr; BACKTRACK(assignment, csp)
 *             if result &ne; failure then
 *                return result
 *             remove inferences from assignment
 *          remove {var = value} from assignment
 *    return failure
 * </code>
 * </pre>
 * 
 * Figure ?? A simple backtracking algorithm for constraint satisfaction
 * problems. The algorithm is modeled on the recursive depth-first search of
 * Chapter 3. By varying the functions SELECT-UNASSIGNED-VARIABLE and
 * ORDER-DOMAIN-VALUES, we can implement the general-purpose heuristic discussed
 * in the text. The function INFERENCE can optionally be used to impose arc-,
 * path-, or k-consistency, as desired. If a value choice leads to failure
 * (noticed either by INFERENCE or by BACKTRACK), then value assignments
 * (including those made by INFERENCE) are removed from the current assignment
 * and a new value is tried.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 *
 */
public class BacktrackingSearch implements SearchForAssignmentFunction {

	// function BACKTRACKING-SEARCH(csp) returns a solution, or failure
	@Override
	public Assignment apply(CSP csp) {
		// return BACKTRACK({ }, csp)
		return backtrack(newAssignment(), csp);
	}

	// function BACKTRACK(assignment, csp) returns a solution, or failure
	public Assignment backtrack(Assignment assignment, CSP csp) {
		// if assignment is complete then return assignment
		if (assignment.isComplete(csp)) {
			return assignment;
		}
		// var <- SELECT-UNASSIGNED-VARIABLE(assignment, csp)
		String var = selectUnassignedVariable(assignment, csp);
		// for each value in ORDER-DOMAIN-VALUES(var, assignment, csp) do
		for (Object value : orderDomainValues(var, assignment, csp)) {
			// if value is consistent with assignment then
			if (assignment.isConsistent(var, value, csp)) {
				// add {var = value} to assignment
				assignment.add(var, value);
				// inferences <- INFERENCE(csp, assignment, var, value)
				Assignment inferences = inference(csp, assignment, var, value);
				// if inferences != failure then
				if (inferences != failure()) {
					// add inferences to assignment
					assignment.add(inferences);
					// result <- BACKTRACK(assignment, csp)
					Assignment result = backtrack(assignment, csp);
					// if result != failure then
					if (result != failure()) {
						// return result
						return result;
					}
					// remove inferences from assignment
					assignment.remove(inferences, csp);
				}
				// remove {var = value} from assignment
				assignment.remove(var, value);
			}
		}
		// return failure
		return failure();
	}

	//
	// Supporting Code
	@FunctionalInterface
	public interface OrderDomainValuesFunction {
		List<Object> apply(String var, Assignment assignment, CSP csp);
	}

	@FunctionalInterface
	public interface InferenceFunction {
		Assignment apply(CSP csp, Assignment currentAssignment, String currentVar, Object currentValue);
	}

	private BiFunction<Assignment, CSP, String> selectUnassignedVariableFn;
	private OrderDomainValuesFunction orderDomainValuesFn;
	private InferenceFunction inferenceFn;

	public BacktrackingSearch() {
	}

	public BacktrackingSearch(BiFunction<Assignment, CSP, String> selectUnassignedVariableFn,
			OrderDomainValuesFunction orderDomainValuesFn, InferenceFunction inferenceFn) {
		setSelectUnassignedVariableFunction(selectUnassignedVariableFn);
		setOrderDomainValuesFunction(orderDomainValuesFn);
		setInferenceFunction(inferenceFn);
	}

	public Assignment newAssignment() {
		return new BasicAssignment();
	}

	public Assignment failure() {
		return null;
	}

	public String selectUnassignedVariable(Assignment assignment, CSP csp) {
		return getSelectUnassignedVariableFunction().apply(assignment, csp);
	}

	public List<Object> orderDomainValues(String var, Assignment assignment, CSP csp) {
		return getOrderDomainValuesFunction().apply(var, assignment, csp);
	}

	public Assignment inference(CSP csp, Assignment currentAssignment, String currentVar, Object currentValue) {
		return getInferenceFunction().apply(csp, currentAssignment, currentVar, currentValue);
	}

	public BiFunction<Assignment, CSP, String> getSelectUnassignedVariableFunction() {
		if (selectUnassignedVariableFn == null) {
			selectUnassignedVariableFn = getSelectUnassignedVariableInOrderFunction();
		}
		return selectUnassignedVariableFn;
	}

	// The simplest strategy for SELECT-UNASSIGNED-VARIABLE
	// is to choose the next unassigned variable in order {X1, X2, ...}
	public static BiFunction<Assignment, CSP, String> getSelectUnassignedVariableInOrderFunction() {
		return (assignment, csp) -> {
			return csp.getVariables().stream().filter(var -> !assignment.contains(var)).findFirst().get();
		};
	}

	// MRV - minimum-remaining-values heuristic.
	public static BiFunction<Assignment, CSP, String> getSelectUnassignedVariableUsingMRVFunction() {
		return (assignment, csp) -> {
			return csp.getVariables().stream().filter(var -> !assignment.contains(var))
					.min(Comparator.comparingInt(var -> csp.getDomain(var).size())).get();
		};
	}

	// Degree Heuristic
	public static BiFunction<Assignment, CSP, String> getSelectUnassignedVariableUsingHighestDegreeFunction() {
		return (assignment, csp) -> {
			return csp.getVariables().stream().filter(var -> !assignment.contains(var))
					.max(Comparator.comparingLong(var -> csp.getConstraints().stream()
							.filter(constraint -> constraint.getScope().contains(var)).count()))
					.get();
		};
	}

	public void setSelectUnassignedVariableFunction(BiFunction<Assignment, CSP, String> selectUnassignedVariableFn) {
		this.selectUnassignedVariableFn = selectUnassignedVariableFn;
	}

	public OrderDomainValuesFunction getOrderDomainValuesFunction() {
		if (orderDomainValuesFn == null) {
			orderDomainValuesFn = getOrderDomainValuesInOrderFunction();
		}
		return orderDomainValuesFn;
	}

	// The simplest strategy for ORDER-DOMAIN-VALUES
	// is to choose the values in the order the are defined on the CSP
	public static OrderDomainValuesFunction getOrderDomainValuesInOrderFunction() {
		return (var, assignment, csp) -> {
			return csp.getDomainValues(var);
		};
	}

	// LCV - least-constraining-value heuristic
	public static OrderDomainValuesFunction getOrderDomainValuesInOrderUsingLCVFunction() {
		return (var, assignment, csp) -> {
			// Note: compute the number of neighboring conflict counts once and
			// cache the results as the call to getNumberNeigboringConflicts()
			// could be relatively expensive and called multiple times for
			// the same variable if used inside of the sort() call - i.e. when
			// comparing individual values with each other.
			Map<Object, Integer> valueNumConflictsLookup = new HashMap<>();
			for (Object value : csp.getDomainValues(var)) {
				valueNumConflictsLookup.put(value,
						BasicCSPUtil.getNumberNeigboringConflicts(var, value, csp, assignment));
			}
			List<Object> orderedValues = new ArrayList<>(csp.getDomainValues(var));
			orderedValues.sort(Comparator.comparingInt(value -> valueNumConflictsLookup.get(value)));
			return orderedValues;
		};
	}

	public void setOrderDomainValuesFunction(OrderDomainValuesFunction orderDomainValuesFn) {
		this.orderDomainValuesFn = orderDomainValuesFn;
	}

	public InferenceFunction getInferenceFunction() {
		if (inferenceFn == null) {
			inferenceFn = getInferenceNoneFunction();
		}
		return inferenceFn;
	}

	// No inference whatsoever occurs.
	public static InferenceFunction getInferenceNoneFunction() {
		return (CSP csp, Assignment currentAssignment, String currentVar, Object currentValue) -> {
			return new BasicAssignment();
		};
	}

	// A trivial inference is that we can infer that the domain for the current
	// variable should be reduced to the current value.
	public static InferenceFunction getInferenceCurrentDomainReducedToValueFunction() {
		return (CSP csp, Assignment currentAssignment, String currentVar, Object currentValue) -> {
			Assignment inferenceAssignments = new BasicAssignment();

			inferenceAssignments.executeInCSPListenerBlock(csp, () -> {
				csp.getDomain(currentVar).reduceDomainTo(currentValue);
			});

			return inferenceAssignments;
		};
	}

	// Forward Checking - whenever a variable X is assigned, the
	// forward-checking process establishes arc consistency for it: for each
	// unassigned variable Y that is connected to X by a constraint, delete from
	// Y's domain any value that is inconsistent with the value chosen for X.
	public static InferenceFunction getInferenceForwardCheckingFunction() {
		return (CSP csp, Assignment currentAssignment, String currentVar, Object currentValue) -> {
			final Assignment inferenceAssignments = new BasicAssignment();

			inferenceAssignments.executeInCSPListenerBlock(csp, () -> {
				// First reduce the current variables domain to the given value
				csp.getDomain(currentVar).reduceDomainTo(currentValue);

				// Collect the unassigned neighbor variables.
				Set<String> neighborVariables = csp.getNeighbors(currentVar);
				List<String> unassignedNeighborVariables = currentAssignment.getUnassignedNeigbors(csp, currentVar);

				// Determine the constraints covered by the unassigned
				// neighboring set of variables
				List<Constraint> unassignedNeighboringConstraints = csp.getNeighboringConstraints(currentVar).stream()
						.filter(constraint -> constraint.getScope().stream()
								.anyMatch(scopeVar -> unassignedNeighborVariables.contains(scopeVar)))
						.collect(Collectors.toList());

				Map<String, List<Object>> allowedAssignments = currentAssignment.getAllowedAssignments(csp,
						neighborVariables);

				// Determine which unassigned values conflict and remove them
				// from the domains
				List<List<Object>> possibleValues = new ArrayList<>();
				for (Constraint constraint : unassignedNeighboringConstraints) {
					// Collect the possible values for each variable in
					// the contraint's scope
					possibleValues.clear();
					constraint.getScope().forEach(scopeVar -> {
						possibleValues.add(allowedAssignments.get(scopeVar));
					});
					// For each permutation of possible arguments,
					// reduce the domains of those arguments that do
					// not satisfy the given constraints.
					Util.permuteArguments(Object.class, possibleValues, (Object[] args) -> {
						if (!constraint.getRelation().isMember(args)) {
							for (int i = 0; i < args.length; i++) {
								String scopeVar = constraint.getScope().get(i);
								if (unassignedNeighborVariables.contains(scopeVar)) {
									csp.getDomain(scopeVar).delete(args[i]);
								}
							}
						}
					});
				}
			});

			if (csp.isInconsistent()) {
				return null; // indicate failure
			}
			return inferenceAssignments;
		};
	}

	// Maintaining Arc Consistency (MAC)
	public static InferenceFunction getInferenceMACFunction() {
		return (CSP csp, Assignment currentAssignment, String currentVar, Object currentValue) -> {
			Assignment inferenceAssignments = new BasicAssignment();

			inferenceAssignments.executeInCSPListenerBlock(csp, () -> {
				// First reduce the current variables domain to the given value
				csp.getDomain(currentVar).reduceDomainTo(currentValue);

				// NOTE: AC3 causes side effects to occur to the CSP's domains.
				new AC3().test(csp,
						AC3.macArcs(csp, currentVar, currentAssignment.getUnassignedNeigbors(csp, currentVar)));
			});

			// If AC3 failed, the CSP will be inconsistent as a 0 domain
			// will have been detected by AC3.
			if (csp.isInconsistent()) {
				return null; // indicate failure
			}
			return inferenceAssignments;
		};
	}

	public void setInferenceFunction(InferenceFunction inferenceFn) {
		this.inferenceFn = inferenceFn;
	}
}