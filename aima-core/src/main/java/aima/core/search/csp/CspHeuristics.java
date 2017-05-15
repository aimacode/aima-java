package aima.core.search.csp;

import aima.core.util.datastructure.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines variable and value selection heuristics for CSP backtracking strategies.
 * @author Ruediger Lunde
 */
public class CspHeuristics {


    public interface VariableSelection {
        List<Variable> apply(List<Variable> vars, CSP csp);
    }

    public interface ValueSelection {
        List<Object> apply(Variable var, Assignment assignment, CSP csp);
    }

    public static VariableSelection mrv() { return new MrvHeuristic(); }
    public static VariableSelection deg() { return new DegHeuristic(); }
    public static VariableSelection mrvDeg() { return (vars, csp) -> deg().apply(mrv().apply(vars, csp), csp); }
    public static ValueSelection lcv() { return new LcvHeuristic();}

    /**
     * Implements the minimum-remaining-values heuristic.
     */
    private static class MrvHeuristic implements VariableSelection {
        public List<Variable> apply(List<Variable> vars, CSP csp) {
            List<Variable> result = new ArrayList<>();
            int mrv = Integer.MAX_VALUE;
            for (Variable var : vars) {
                int rv = csp.getDomain(var).size();
                if (rv <= mrv) {
                    if (rv < mrv) {
                        result.clear();
                        mrv = rv;
                    }
                    result.add(var);
                }
            }
            return result;
        }
    }

    /**
     * Implements the degree heuristic. Constraints with arbitrary scope size are supported.
     */
    private static class DegHeuristic implements VariableSelection {
        public List<Variable> apply(List<Variable> vars, CSP csp) {
            List<Variable> result = new ArrayList<>();
            int maxDegree = -1;
            for (Variable var : vars) {
                int degree = csp.getConstraints(var).size();
                if (degree >= maxDegree) {
                    if (degree > maxDegree) {
                        result.clear();
                        maxDegree = degree;
                    }
                    result.add(var);
                }
            }
            return result;
        }
    }

    /**
     * Implements the least constraining value heuristic.
     */
    public static class LcvHeuristic implements ValueSelection {
        public List<Object> apply(Variable var, Assignment assignment, CSP csp) {
            List<Pair<Object, Integer>> pairs = new ArrayList<>();
            for (Object value : csp.getDomain(var)) {
                int num = countLostValues(var, value, assignment, csp);
                pairs.add(new Pair<>(value, num));
            }
            return pairs.stream().sorted(Comparator.comparing(Pair::getSecond)).map(Pair::getFirst)
                    .collect(Collectors.toList());
        }

        /**
         * Ignores constraints which are not binary.
         */
        private int countLostValues(Variable var, Object value, Assignment assignment, CSP csp) {
            int result = 0;
            Assignment assign = new Assignment();
            assign.add(var, value);
            for (Constraint constraint : csp.getConstraints(var)) {
                if (constraint.getScope().size() == 2) {
                    Variable neighbor = csp.getNeighbor(var, constraint);
                    if (!assignment.contains(neighbor))
                        for (Object nValue : csp.getDomain(neighbor)) {
                            assign.add(neighbor, nValue);
                            if (!constraint.isSatisfiedWith(assign)) {
                                ++result;
                            }
                        }
                }
            }
            return result;
        }
    }
}
