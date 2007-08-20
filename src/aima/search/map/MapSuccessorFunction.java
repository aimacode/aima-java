package aima.search.map;

import java.util.ArrayList;
import java.util.List;

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

	public List getSuccessors(Object state) {
		List<Successor> successors = new ArrayList<Successor>();

		List<String> linkedLocations = map.getLocationsLinkedTo((String) state);
		for (String location : linkedLocations) {
			successors.add(new Successor(location, location));
		}

		return successors;
	}
}
