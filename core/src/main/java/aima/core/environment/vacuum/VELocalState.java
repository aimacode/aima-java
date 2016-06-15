package aima.core.environment.vacuum;

/**
 * @author Ciaran O'Reilly
 */
public class VELocalState {
	public final String location;
	public final VacuumEnvironment.Status status;

	public VELocalState(String location, VacuumEnvironment.Status state) {
		this.location = location;
		this.status = state;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && this.getClass() == obj.getClass()) {
			VELocalState other = (VELocalState) obj;
			result = this.status == other.status && this.location.equals(other.location);
		}
		return result;
	}

	@Override
	public int hashCode() {
		return location.hashCode() + status.hashCode();
	}
	
	@Override
	public String toString() {
		return "VE("+location+", "+status+")";
	}
}
