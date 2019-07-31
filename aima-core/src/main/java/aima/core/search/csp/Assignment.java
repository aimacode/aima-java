package aima.core.search.csp;

import java.util.*;

/**
 * An assignment assigns values to some or all variables of a CSP.
 *
 * @author Ruediger Lunde
 */
public class Assignment<VAR extends Variable, VAL> implements Cloneable {
    /**
     * Maps variables to their assigned values.
     */
    private LinkedHashMap<VAR, VAL> variableToValueMap = new LinkedHashMap<>();

    public List<VAR> getVariables() {
        return new ArrayList<>(variableToValueMap.keySet());
    }

    public VAL getValue(VAR var) {
        return variableToValueMap.get(var);
    }

    public VAL add(VAR var, VAL value) {
        assert value != null;
        return variableToValueMap.put(var, value);
    }

    public VAL remove(VAR var) {
        return variableToValueMap.remove(var);
    }

    public boolean contains(VAR var) {
        return variableToValueMap.containsKey(var);
    }

    /**
     * Returns true if this assignment does not violate any constraints of
     * <code>constraints</code>.
     */
    public boolean isConsistent(List<Constraint<VAR, VAL>> constraints) {
        return constraints.stream().allMatch(cons -> cons.isSatisfiedWith(this));
    }

    /**
     * Returns true if this assignment assigns values to every variable of
     * <code>vars</code>.
     */
    public boolean isComplete(List<VAR> vars) {
        return vars.stream().allMatch(this::contains);
    }

    /**
     * Returns true if this assignment is consistent as well as complete with
     * respect to the given CSP.
     */
    public boolean isSolution(CSP<VAR, VAL> csp) {
        return isConsistent(csp.getConstraints()) && isComplete(csp.getVariables());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Assignment<VAR, VAL> clone() {
        Assignment<VAR, VAL> result;
        try {
            result = (Assignment<VAR, VAL>) super.clone();
            result.variableToValueMap = new LinkedHashMap<>(variableToValueMap);
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("Could not clone assignment."); // should never happen!
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (Map.Entry<VAR, VAL> entry : variableToValueMap.entrySet()) {
            if (result.length() > 1)
                result.append(", ");
            result.append(entry.getKey()).append("=").append(entry.getValue());
        }
        result.append("}");
        return result.toString();
    }
}