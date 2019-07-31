package aima.core.environment.vacuum;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import aima.core.environment.vacuum.VacuumEnvironment.LocationState;

/**
 * Represents a percept in the Vacuum World. It provides information about the
 * current location name and state. To support experiments with nondeterministic
 * environments or environments with more than two locations, additionally, an
 * optional set of (key, value) pairs is maintained.
 *
 * @author Ruediger Lunde
 */
public class VacuumPercept {
	private String currLocation;
	private LocationState currState;
	// for extended vacuum environments (e.g. fully observable environments, two dimensional checkerboards)
	private Map<String, Object> dynAttributes;

	public VacuumPercept(String currLocation, LocationState state) {
		this.currLocation = currLocation;
		this.currState = state;
		dynAttributes = new LinkedHashMap<>();
	}

	public String getCurrLocation() {
		return currLocation;
	}

	public LocationState getCurrState() {
		return currState;
	}

	public Object getAttribute(String key) {
		return dynAttributes.get(key);
	}

	public void setAttribute(String key, Object value) {
		dynAttributes.put(key, value);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[currLocation=").append(getCurrLocation()).append(", currState=").append(getCurrState());
		if (!dynAttributes.isEmpty())
			for (Map.Entry<String, Object> e : dynAttributes.entrySet())
				result.append(", ").append(e.getKey()).append("=").append(e.getValue());
		result.append("]");
		return result.toString();
	}

	//
	// needed for TableDrivenVacuumAgent:

	@Override
	public int hashCode() {
		return 7 * currLocation.hashCode() + 13 * currState.hashCode() + 17 * dynAttributes.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass() == obj.getClass()) {
			VacuumPercept vp = (VacuumPercept) obj;
			return Objects.equals(currLocation, vp.currLocation)
					&& currState == vp.currState
					&& dynAttributes.equals(vp.dynAttributes);
		}
		return false;
	}
}
