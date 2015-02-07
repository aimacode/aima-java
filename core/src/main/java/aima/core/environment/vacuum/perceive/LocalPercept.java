package aima.core.environment.vacuum.perceive;

import aima.core.api.agent.Percept;
import aima.core.environment.vacuum.VacuumEnvironment;

/**
 * @author Ciaran O'Reilly
 */
public class LocalPercept implements Percept {
    private String                          location;
    private VacuumEnvironment.LocationState state;

    public LocalPercept(String location, VacuumEnvironment.LocationState state) {
        this.location = location;
        this.state    = state;
    }

    public String getLocation() {
        return location;
    }

    public VacuumEnvironment.LocationState getState() {
        return state;
    }

    public boolean isClean() {
        return !isDirty();
    }

    public boolean isDirty() {
        return getState() == VacuumEnvironment.LocationState.Dirty;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && this.getClass() == obj.getClass()) {
            LocalPercept other = (LocalPercept) obj;
            result = this.state == other.state && this.location.equals(other.location);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return location.hashCode() + state.hashCode();
    }
}
