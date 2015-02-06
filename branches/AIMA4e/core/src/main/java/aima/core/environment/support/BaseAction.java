package aima.core.environment.support;

import aima.core.api.agent.Action;

/**
 * @author Ciaran O'Reilly
 */
public class BaseAction implements Action {
    private String name;

    public BaseAction(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        // By default we'll assume actions of the same class are the same.
        return getClass().equals(obj.getClass());
    }

    @Override
    public String toString() {
        return name;
    }
}
