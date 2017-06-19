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


    public interface VariableSelection<VAR extends Variable, VAL> {
        List<VAR> apply(CSP<VAR, VAL> csp, List<VAR> vars);
    }

    public interface ValueSelection<VAR extends Variable, VAL> {
        List<VAL> apply(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var);
    }

    public static <VAR extends Variable, VAL> VariableSelection<VAR, VAL> mrv() { return new MrvHeuristic<>(); }
    public static <VAR extends Variable, VAL> VariableSelection<VAR, VAL> deg() { return new DegHeuristic<>(); }
    public static <VAR extends Variable, VAL> VariableSelection<VAR, VAL> mrvDeg() {
        return (csp, vars) -> new DegHeuristic<VAR, VAL>().apply(csp, new MrvHeuristic<VAR, VAL>().apply(csp, vars));
    }
    public static <VAR extends Variable, VAL> ValueSelection<VAR, VAL> lcv() { return new LcvHeuristic<>();}

    /**
     * Implements the minimum-remaining-values heuristic.
     */
    public static class MrvHeuristic<VAR extends Variable, VAL> implements VariableSelection<VAR, VAL> {

        /** Returns variables from <code>vars</code> which are the best with respect to MRV. */
        public List<VAR> apply(CSP<VAR, VAL> csp, List<VAR> vars) {
            List<VAR> result = new ArrayList<>();
            int mrv = Integer.MAX_VALUE;
            for (VAR var : vars) {
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
    public static class DegHeuristic<VAR extends Variable, VAL> implements VariableSelection<VAR, VAL> {

        /** Returns variables from <code>vars</code> which are the best with respect to DEG. */
        public List<VAR> apply(CSP<VAR, VAL> csp, List<VAR> vars) {
            List<VAR> result = new ArrayList<>();
            int maxDegree = -1;
            for (VAR var : vars) {
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
    public static class LcvHeuristic<VAR extends Variable, VAL> implements ValueSelection<VAR, VAL> {

        /** Returns the values of Dom(var) in a special order. The least constraining value comes first. */
        public List<VAL> apply(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var) {
            List<Pair<VAL, Integer>> pairs = new ArrayList<>();
            for (VAL value : csp.getDomain(var)) {
                int num = countLostValues(csp, assignment, var, value);
                pairs.add(new Pair<>(value, num));
            }
            return pairs.stream().sorted(Comparator.comparing(Pair::getSecond)).map(Pair::getFirst)
                    .collect(Collectors.toList());
        }

        /**
         * Ignores constraints which are not binary.
         */
        private int countLostValues(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR var, VAL value) {
            int result = 0;
            Assignment<VAR, VAL> assign = new Assignment<>();
            assign.add(var, value);
            for (Constraint<VAR, VAL> constraint : csp.getConstraints(var)) {
                if (constraint.getScope().size() == 2) {
                    VAR neighbor = csp.getNeighbor(var, constraint);
                    if (!assignment.contains(neighbor))
                        for (VAL nValue : csp.getDomain(neighbor)) {
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
