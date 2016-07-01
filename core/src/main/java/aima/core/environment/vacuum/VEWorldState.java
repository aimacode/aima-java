package aima.core.environment.vacuum;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Ciaran O'Reilly
 */
public class VEWorldState {
	public final String currentLocation;
	//
	private Map<String, VELocalState> locationLocalStateMap;

	public VEWorldState(String currentLocation, VELocalState... leftToRightLocalStates) {
		locationLocalStateMap = new LinkedHashMap<>();
		for (VELocalState inState : leftToRightLocalStates) {
			locationLocalStateMap.put(inState.location, inState);
		}
		if (!locationLocalStateMap.containsKey(currentLocation)) {
			throw new IllegalArgumentException(
					"Current location " + currentLocation + " is not contained in the environments states");
		}
		if (locationLocalStateMap.size() != leftToRightLocalStates.length) {
			throw new IllegalArgumentException(
					"Repeated locations are not allowed in the list of left to right states provided.");
		}
		this.currentLocation = currentLocation;
	}

	public VEWorldState performDeterministic(String action) {
		// i.e. in the cases the action has no effect
		VEWorldState resultingWorldState = this;

		switch (action) {
		case VacuumEnvironment.ACTION_LEFT:
			VELocalState stateToLeft = null;
			for (VELocalState state : locationLocalStateMap.values()) {
				if (state.location.equals(currentLocation)) {
					if (stateToLeft != null) {
						resultingWorldState = new VEWorldState(stateToLeft.location, locationLocalStateMap);
					}
					break;
				}
				stateToLeft = state;
			}
			break;
		case VacuumEnvironment.ACTION_RIGHT:
			boolean currentStatePrevious = false;
			for (VELocalState state : locationLocalStateMap.values()) {
				if (currentStatePrevious) {
					resultingWorldState = new VEWorldState(state.location, locationLocalStateMap);
					break;
				}
				if (state.location.equals(currentLocation)) {
					currentStatePrevious = true;
				}
			}
			break;
		case VacuumEnvironment.ACTION_SUCK:
			if (locationLocalStateMap.get(currentLocation).status == VacuumEnvironment.Status.Dirty) {
				resultingWorldState = makeStatus(currentLocation, VacuumEnvironment.Status.Clean);
			}
			break;
		default:
			throw new UnsupportedOperationException("Action " + action + " is currently not supported.");
		}
		return resultingWorldState;
	}
	
	public boolean isClean(String location) {
		return locationLocalStateMap.get(currentLocation).status == VacuumEnvironment.Status.Clean;
	}

	public boolean isAllClean() {
		return locationLocalStateMap.values().stream()
				.allMatch(localState -> localState.status == VacuumEnvironment.Status.Clean);
	}
	
	public VEWorldState makeStatus(String location, VacuumEnvironment.Status status) {
		Map<String, VELocalState> updatedLocationLocalStateMap = new LinkedHashMap<>(locationLocalStateMap);
		updatedLocationLocalStateMap.put(location,
				new VELocalState(location, status));
		return new VEWorldState(location, updatedLocationLocalStateMap);
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && this.getClass() == obj.getClass()) {
			VEWorldState other = (VEWorldState) obj;
			result = this.currentLocation.equals(other.currentLocation)
					&& this.locationLocalStateMap.equals(other.locationLocalStateMap);
		}
		return result;
	}

	@Override
	public int hashCode() {
		return currentLocation.hashCode() + locationLocalStateMap.hashCode();
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("][", "[", "]");
		for (VELocalState localState : locationLocalStateMap.values()) {
			sj.add((localState.status == VacuumEnvironment.Status.Clean ? " " : "*")
					+ (currentLocation.equals(localState.location) ? "_/" : "  "));
		}

		return sj.toString();
	}

	private VEWorldState(String currentLocation, Map<String, VELocalState> locationLocalStateMap) {
		this.currentLocation = currentLocation;
		this.locationLocalStateMap = locationLocalStateMap;
	}
}