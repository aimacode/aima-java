package aima.core.search.csp.solver;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;
import aima.core.util.Tasks;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// vm-options (Java > 8): --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml

/**
 * Simple version of Backtracking with backjumping. It analyzes reasons for dead-ends by means of so called nogoods
 * and by that tries to avoid useless search in subspaces without solution.
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
                    for (Constraint<VAR, VAL> cons : csp.getConstraints(var))
                        if (!cons.isSatisfiedWith(assignment)) {
                            result.nogood.addAll(cons.getScope());
                            break; // only one nogood is tracked (kiss)
                        }
                }
                assignment.remove(var);
            }
            result.nogood.remove(var);
        }
        return result;
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

    private static class SolutionOrNogood<VAR extends Variable, VAL> {
        Assignment<VAR, VAL> solution;
        Set<VAR> nogood = new HashSet<>(); // set of variables whose current value bindings caused a dead-end

        boolean hasSolution() { return solution != null; }
    }
}
