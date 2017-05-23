package aima.core.search.csp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

/**
 * An assignment assigns values to some or all variables of a CSP.
 *
 * @author Ruediger Lunde
 */
public class Assignment<VAR extends Variable, VAL> {
    /**
     * Contains all assigned variables. Positions reflect the the order in which
     * the variables were assigned to values.
     */
    private List<VAR> variables;
    /**
     * Maps variables to their assigned values.
     */
    private Hashtable<VAR, VAL> variableToValue;

    public Assignment() {
        variables = new ArrayList<>();
        variableToValue = new Hashtable<>();
    }

    public List<VAR> getVariables() {
        return Collections.unmodifiableList(variables);
    }

    public VAL getValue(VAR var) {
        return variableToValue.get(var);
    }

    public void add(VAR var, VAL value) {
        if (variableToValue.put(var, value) == null)
            variables.add(var);
    }

    public void remove(VAR var) {
        if (contains(var)) {
            variableToValue.remove(var);
            variables.remove(var);
        }
    }

    public boolean contains(VAR var) {
        return variableToValue.get(var) != null;
    }

    /**
     * Returns true if this assignment does not violate any constraints of
     * <code>constraints</code>.
     */
    public boolean isConsistent(List<Constraint<VAR, VAL>> constraints) {
        for (Constraint<VAR, VAL> cons : constraints)
            if (!cons.isSatisfiedWith(this))
                return false;
        return true;
    }

    /**
     * Returns true if this assignment assigns values to every variable of
     * <code>vars</code>.
     */
    public boolean isComplete(List<VAR> vars) {
        for (VAR var : vars)
            if (!contains(var))
                return false;
        return true;
    }

    /**
     * Returns true if this assignment is consistent as well as complete with
     * respect to the given CSP.
     */
    public boolean isSolution(CSP<VAR, VAL> csp) {
        return isConsistent(csp.getConstraints()) && isComplete(csp.getVariables());
    }

    public Assignment<VAR, VAL> copy() {
        Assignment<VAR, VAL> copy = new Assignment<>();
        for (VAR var : variables) copy.add(var, variableToValue.get(var));
        return copy;
    }

    @Override
    public String toString() {
        boolean comma = false;
        StringBuilder result = new StringBuilder("{");
        for (VAR var : variables) {
            if (comma)
                result.append(", ");
            result.append(var).append("=").append(variableToValue.get(var));
            comma = true;
        }
        result.append("}");
        return result.toString();
    }
}