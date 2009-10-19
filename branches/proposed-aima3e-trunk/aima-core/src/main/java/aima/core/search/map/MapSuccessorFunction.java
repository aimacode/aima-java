package aima.core.search.map;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.Successor;
import aima.core.search.framework.SuccessorFunction;

/**
 * Implementation of SuccessorFunction interface that derives successors based on the
 * locations linked/traversible to the current location.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapSuccessorFunction implements SuccessorFunction {

	private Map map = null;

	public MapSuccessorFunction(Map aMap) {
		this.map = aMap;
	}

	public List<Successor> getSuccessors(Object currentState) {
		List<Successor> successors = new ArrayList<Successor>();

		String location = currentState.toString();
		if (currentState instanceof DynamicPercept) {
			location = (String) ((DynamicPercept) currentState)
					.getAttribute(DynAttributeNames.PERCEPT_IN);
		}

		List<String> linkedLocations = map.getLocationsLinkedTo(location);
		for (String linkLoc : linkedLocations) {
			successors.add(new Successor(new MoveToAction(linkLoc, map
					.getDistance(location, linkLoc)), linkLoc));
		}

		return successors;
	}
}
