package aima.core.search.csp.solver;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;
import aima.core.search.csp.solver.inference.AC3Strategy;
import aima.core.search.csp.solver.inference.InferenceLog;
import aima.core.util.Tasks;

import java.util.*;
import java.util.stream.Collectors;

public class AC3BackjumpingBacktrackingSolver<VAR extends Variable, VAL> extends CspSolver<VAR, VAL> {

    private CspHeuristics.VariableSelectionStrategy<VAR, VAL> varSelectionStrategy;
    private CspHeuristics.ValueOrderingStrategy<VAR, VAL> valOrderingStrategy;
    private final AC3Strategy<VAR, VAL> inferenceStrategy = new AC3Strategy<>();

    /**
     * Selects the algorithm for SELECT-UNASSIGNED-VARIABLE. Uses the fluent interface design pattern.
     */
    public AC3BackjumpingBacktrackingSolver<VAR, VAL> set(CspHeuristics.VariableSelectionStrategy<VAR, VAL> varStrategy) {
        varSelectionStrategy = varStrategy;
        return this;
    }

    /**
     * Selects the algorithm for ORDER-DOMAIN-VALUES. Uses the fluent interface design pattern.
     */
    public AC3BackjumpingBacktrackingSolver<VAR, VAL> set(CspHeuristics.ValueOrderingStrategy<VAR, VAL> valStrategy) {
        valOrderingStrategy = valStrategy;
        return this;
    }

    /**
     * Selects MRV&DEG for variable selection and LCV for domain ordering.
     */
    public AC3BackjumpingBacktrackingSolver<VAR, VAL> setAll() {
        return set(CspHeuristics.mrvDeg()).set(CspHeuristics.lcv());
    }

    /**
     * Calls the solve() method without fixed starting assignment.
     */
    @Override
    public Optional<Assignment<VAR, VAL>> solve(CSP<VAR, VAL> csp) {
        return solve(csp, new Assignment<>());
    }

    /**
     * Applies an initial inference step and then calls the super class implementation.
     */
    @Override
    public Optional<Assignment<VAR, VAL>> solve(CSP<VAR, VAL> csp, Assignment<VAR, VAL> startAssignment) {
        csp = csp.copyDomains(); // do not change the original CSP!
        InferenceLog<VAR, VAL> log = inferenceStrategy.apply(csp);
        if (!log.isEmpty()) {
            fireStateChanged(csp, null, null);
            if (log.inconsistencyFound())
                return Optional.empty();
        }
        AC3BackjumpingBacktrackingSolver.SolutionOrNogood<VAR, VAL> result = backtrack(csp, startAssignment);
        return result.hasSolution() ? Optional.of(result.solution) : Optional.empty();
    }

    /**
     * Searches for a solution of the given CSP by recursively extending the given assignment.
     * Causes of dead-ends are analyzed by means of nogoods which are sets of variables whose
     * current value bindings have proven not to be a part of a solution. This is used for intelligent
     * upwards navigation (backjumping).
     * <p>
     * Other than BackjumpingBacktrackingSolver this solver also applies inference with a AC3Strategy.
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

                    InferenceLog<VAR, VAL> log = inference(csp, assignment, var);
                    if (!log.isEmpty())
                        fireStateChanged(csp, null, null);

                    if (!log.inconsistencyFound()) {
                        SolutionOrNogood<VAR, VAL> res = backtrack(csp, assignment);
                        if (res.hasSolution()) {
                            return res; // solution found!
                        } else if (!res.nogood.contains(var)) {
                            log.undo(csp);
                            assignment.remove(var);
                            return res; // jump back!
                        } else {
                            result.nogood.addAll(res.nogood);
                            result.nogood.addAll(getAssignedNeighbors(csp, assignment, var));
                        }
                    } else {
                        result.nogood.addAll(findCause(csp, assignment, log));
                    }
                    log.undo(csp);
                }
                assignment.remove(var);
            }
            result.nogood.remove(var);
        }
        return result;
    }

    /**
     * Gets an inference log which stores the variable that got an empty domain while reducing the domains in AC-3.
     * Neighbors of the variable with empty domain are used as conflict set.
     *
     * @param csp = associated CSP
     * @param log = inference log
     * @return neighbors of the variable with empty domain.
     */
    protected Collection<VAR> findCause(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, InferenceLog<VAR, VAL> log) {
        return getAssignedNeighbors(csp, assignment, log.getEmptyDomainVariable());
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
     * Primitive operation, which tries to optimize the CSP representation with respect to a new assignment.
     *
     * @param var The variable which just got a new value in the assignment.
     * @return An object which provides information about
     * (1) whether changes have been performed,
     * (2) possibly inferred empty domains, and
     * (3) how to restore the original CSP.
     */
    protected InferenceLog<VAR, VAL> inference(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var) {
        return inferenceStrategy.apply(csp, assignment, var);
    }

    /**
     * Detects the neighbors to a variable that are connected to it through a constraint.
     *
     * @param variable = variable for which the neighbors are searched
     * @param csp      = associated constraint satisfaction problem
     * @return a list of variables that are connected to the given variable through a constraint
     */
    private List<VAR> getAssignedNeighbors(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR variable) {
        List<VAR> neighbors = new ArrayList<>();
        for (Constraint<VAR, VAL> cons : csp.getConstraints(variable)) {
            VAR neighbor = csp.getNeighbor(variable, cons);
            if (neighbor != null && assignment.getValue(neighbor) != null)
                neighbors.add(neighbor);
        }
        return neighbors;
    }

    /**
     * Data structure which can store an assignment as well as a nogood.
     */
    private static class SolutionOrNogood<VAR extends Variable, VAL> {
        Assignment<VAR, VAL> solution;
        Set<VAR> nogood = new HashSet<>(); // set of variables whose current value bindings caused a dead-end

        boolean hasSolution() {
            return solution != null;
        }
    }
}
