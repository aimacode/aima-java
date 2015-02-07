package aima.core.environment.support;

import aima.core.api.agent.Action;

/**
 * @author Ciaran O'Reilly
 */
public class NamedAction implements Action {
    private String name;

    public NamedAction(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && this.getClass() == obj.getClass()) {
            NamedAction other = (NamedAction) obj;
            result = this.name.equals(other.name);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
