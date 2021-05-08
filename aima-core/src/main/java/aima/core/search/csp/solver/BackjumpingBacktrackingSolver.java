package aima.core.search.csp.solver;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;
import aima.core.util.Tasks;

import java.util.*;
import java.util.stream.Collectors;

// vm-options (Java > 8): --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Section 6.3, Page 223.<br>
 * A more intelligent approach to backtracking is to backtrack to a variable that might fix the problem — a variable
 * that was responsible for making one of the possible values of SA impossible. To do this, we will keep track of
 * a set of assignments that are in conflict with some value for SA. The set (in this case {Q=red, NSW=green, V=blue,}),
 * is called the conflict set for SA. The backjumping method backtracks to the most recent assignment in
 * the conflict set; in this case, backjumping would jump over Tasmania and try a new value for V. This method
 * is easily implemented by a modification to BACK TRACK such that it accumulates the conflict set while checking
 * for a legal value to assign. If no legal value is found, the algorithm should return the most recent element of
 * the conflict set along with the failure indicator.<br>
 *
 * In this implementation, backtrack returns the whole set of variables causing the dead-end instead of just the most
 * recent element. The set of variables involved in a conflict set is called nogood here.
 *
 * Limitations:
 * Only one nogood is tracked at a time thought there could be more of them
 * (e.g. resulting from two unsatisfied constraints).
 * Inferences like forward checking or constraint propagation are not supported.
 *
 * @param <VAR> Type which is used to represent variables
 * @param <VAL> Type which is used to represent the values in the domains
 *
 * @author Ruediger Lunde
 */
public class BackjumpingBacktrackingSolver<VAR extends Variable, VAL> extends CspSolver<VAR, VAL> {

    private CspHeuristics.VariableSelectionStrategy<VAR, VAL> varSelectionStrategy;
    private CspHeuristics.ValueOrderingStrategy<VAR, VAL> valOrderingStrategy;

    /**
     * Selects the algorithm for SELECT-UNASSIGNED-VARIABLE. Uses the fluent interface design pattern.
     */
    public BackjumpingBacktrackingSolver<VAR, VAL> set(CspHeuristics.VariableSelectionStrategy<VAR, VAL> varStrategy) {
        varSelectionStrategy = varStrategy;
        return this;
    }

    /**
     * Selects the algorithm for ORDER-DOMAIN-VALUES. Uses the fluent interface design pattern.
     */
    public BackjumpingBacktrackingSolver<VAR, VAL> set(CspHeuristics.ValueOrderingStrategy<VAR, VAL> valStrategy) {
        valOrderingStrategy = valStrategy;
        return this;
    }

    /**
     * Applies backtracking search with backjumping to solve the CSP.
     */
    public Optional<Assignment<VAR, VAL>> solve(CSP<VAR, VAL> csp) {
        SolutionOrNogood<VAR, VAL> result = backtrack(csp, new Assignment<>());
        return result.hasSolution() ? Optional.of(result.solution) : Optional.empty();
    }

    /**
     * Searches for a solution of the given CSP by recursively extending the given assignment.
     * Causes of dead-ends are analyzed by means of nogoods which are sets of variables whose
     * current value bindings have proven not to be a part of a solution. This is used for intelligent
     * upwards navigation (backjumping).
     *
     * @return An assignment (possibly incomplete if task was cancelled) or a nogood (set of variables)
     * if no solution was found.
     */
    private SolutionOrNogood<VAR, VAL> backtrack(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment) {
        SolutionOrNogood<VAR, VAL> result = new SolutionOrNogood<>();
        if (assignment.isComplete(csp.getVariables()) || Tasks.currIsCancelled()) {
            result.solution = assignment;
        } else {
            VAR var = selectUnassignedVariable(csp, assignment);
            for (VAL value : orderDomainValues(csp, assignment, var)) {
                assignment.add(var, value);
                fireStateChanged(csp, assignment, var);
                if (assignment.isConsistent(csp.getConstraints(var))) {
                    SolutionOrNogood<VAR, VAL> res = backtrack(csp, assignment);
                    if (res.hasSolution() || !res.nogood.contains(var))
                        return res; // jump back!
                    else
                        result.nogood.addAll(res.nogood);
                } else {
                    result.nogood.addAll(findCause(csp, assignment, var));
                }
                assignment.remove(var);
            }
            result.nogood.remove(var);
        }
        return result;
    }

    /**
     * Gets an assignment which is inconsistent at <code>var</code> and returns a collection of variables causing the
     * inconsistency. This implementation just returns the scope of one unsatisfied constraint. A smarter
     * selection could increase jump length.
     */
    protected Collection<VAR> findCause(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var) {
        for (Constraint<VAR, VAL> cons : csp.getConstraints(var))
            if (!cons.isSatisfiedWith(assignment))
                return cons.getScope();
        return null; // will never happen!
    }

    /**
     * Primitive operation, selecting a not yet assigned variable.
     */
    private VAR selectUnassignedVariable(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment) {
        List<VAR> vars = csp.getVariables().stream().filter(v -> !assignment.contains(v)).
                collect(Collectors.toList());
        if (varSelectionStrategy != null)
            vars = varSelectionStrategy.apply(csp, vars);
        return vars.get(0);
    }

    /**
     * Primitive operation, ordering the domain values of the specified variable.
     */
    private Iterable<VAL> orderDomainValues(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var) {
        return (valOrderingStrategy != null) ? valOrderingStrategy.apply(csp, assignment, var) : csp.getDomain(var);
    }

    /**
     * Data structure which can store an assignment as well as a nogood.
     */
    private static class SolutionOrNogood<VAR extends Variable, VAL> {
        Assignment<VAR, VAL> solution;
        Set<VAR> nogood = new HashSet<>(); // set of variables whose current value bindings caused a dead-end

        boolean hasSolution() { return solution != null; }
    }
}
