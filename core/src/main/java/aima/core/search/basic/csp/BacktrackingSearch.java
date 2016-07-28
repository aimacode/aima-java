package aima.core.search.basic.csp;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

import aima.core.search.api.Assignment;
import aima.core.search.api.CSP;
import aima.core.search.api.SearchForAssignmentFunction;
import aima.core.search.basic.support.BasicAssignment;

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
 *          inferences &larr; INFERENCE(csp, var, value)
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
				// inferences <- INFERENCE(csp, var, value)
				Assignment inferences = inference(csp, var, value);
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
		Assignment apply(CSP csp, String currentVar, Object currentValue);
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

	public Assignment inference(CSP csp, String currentVar, Object currentValue) {
		return getInferenceFunction().apply(csp, currentVar, currentValue);
	}

	public BiFunction<Assignment, CSP, String> getSelectUnassignedVariableFunction() {
		if (selectUnassignedVariableFn == null) {
			selectUnassignedVariableFn = getSelectUnassignedVariableInOrderFunction();
		}
		return selectUnassignedVariableFn;
	}

	// The simplest strategy for SELECT-UNASSIGNED-VARIABLE.
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
			orderDomainValuesFn = (var, assignment, csp) -> {
				// Default implementation just returns the order of values as
				// specified on the CSP
				return csp.getDomainValues(var);
			};
		}
		return orderDomainValuesFn;
	}

	public void setOrderDomainValuesFunction(OrderDomainValuesFunction orderDomainValuesFn) {
		this.orderDomainValuesFn = orderDomainValuesFn;
	}

	public InferenceFunction getInferenceFunction() {
		if (inferenceFn == null) {
			inferenceFn = (CSP csp, String currentVar, Object currentValue) -> {
				Assignment inferenceAssignments = newAssignment();

				inferenceAssignments.executeInCSPListenerBlock(csp, () -> {
					// At a minimum we can infer that the domain for the current
					// variable should be reduced to the current value.
					csp.getDomain(currentVar).reduceDomainTo(currentValue);
				});

				return inferenceAssignments;
			};
		}
		return inferenceFn;
	}

	public void setInferenceFunction(InferenceFunction inferenceFn) {
		this.inferenceFn = inferenceFn;
	}
}