package aima.core.search.csp.examples;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;

import java.util.*;

/**
 * Represents a special constraint alldiff(...) where all values have to be different.
 *
 * @param <VAR> Variable
 * @param <VAL> Value, e.g. Integer or String
 */
public class AllDiffConstraint<VAR extends Variable, VAL> implements Constraint<VAR, VAL> {

    private final List<VAR> variables;

    public AllDiffConstraint(List<VAR> variables) {
        this.variables = variables;
    }

    @Override
    public List<VAR> getScope() {
        return this.variables;
    }

    @Override
    public boolean isSatisfiedWith(Assignment<VAR, VAL> assignment) {
        Set<VAL> set = new HashSet<>();
        int valueCounter = 0;

        for (VAR variable : variables) {
            VAL value = assignment.getValue(variable);
            if (value != null) {
                set.add(value);
                valueCounter++;
            }
        }
        return set.size() == valueCounter;
    }

    public List<NotEqualConstraint<VAR, VAL>> getNotEqualConstraints() {
        List<NotEqualConstraint<VAR, VAL>> notEqualConstraints = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            for (int j = i; j < variables.size(); j++) {
                if (variables.get(i) != variables.get(j)) {
                    notEqualConstraints.add(new NotEqualConstraint<>(variables.get(i), variables.get(j)));
                }
            }
        }
        return notEqualConstraints;
    }
}
