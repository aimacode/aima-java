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
	
	private Map aMap = null;
	
	public MapSuccessorFunction(Map aMap) {
		this.aMap = aMap;
	}
	
	public List getSuccessors(Object state) {
		List<Successor> successors = new ArrayList<Successor>();
		
		List<String> linkedLocations = aMap.getLocationsLinkedTo((String)state);
		for (String location : linkedLocations) {
			successors.add(new Successor(location, location));
		}
		
		return successors;
	}
}
