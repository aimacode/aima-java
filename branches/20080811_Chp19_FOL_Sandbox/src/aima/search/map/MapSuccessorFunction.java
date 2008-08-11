package aima.search.map;

import java.util.ArrayList;
import java.util.List;

import aima.basic.Percept;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

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

	public List getSuccessors(Object currentState) {
		List<Successor> successors = new ArrayList<Successor>();

		String location = currentState.toString();
		if (currentState instanceof Percept) {
			location = (String) ((Percept) currentState)
					.getAttribute(MapEnvironment.STATE_IN);
		}

		List<String> linkedLocations = map.getLocationsLinkedTo(location);
		for (String linkLoc : linkedLocations) {
			successors.add(new Successor(linkLoc, linkLoc));
		}

		return successors;
	}
}
