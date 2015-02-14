package aima.core.environment.vacuum.perceive;

import aima.core.environment.vacuum.VacuumEnvironment;

/**
 * @author Ciaran O'Reilly
 */
public class LocalPercept {
    public final String                   location;
    public final VacuumEnvironment.Status status;

    public LocalPercept(String location, VacuumEnvironment.Status state) {
        this.location = location;
        this.status    = state;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && this.getClass() == obj.getClass()) {
            LocalPercept other = (LocalPercept) obj;
            result = this.status == other.status && this.location.equals(other.location);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return location.hashCode() + status.hashCode();
    }
}
