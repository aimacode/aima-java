package aima.core.Probability.Proposition;

import aima.core.Probability.RandomVariable;

import java.util.Map;

public class AssignmentProposition extends AbstractTermProposition {

    private Object value = null;

    public AssignmentProposition(RandomVariable var,Object value) {
        super(var);
        setValue(value);
    }

    @Override
    public boolean holds(Map<RandomVariable, Object> possibleWorld) {
        return value.equals(possibleWorld.get(getTermVariable()));
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "The value for the Random Variable must be specified.");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return "AssignmentProposition{ " + getTermVariable().getName()+
                "=" + value +
                '\'' +
                '}';
    }
}
