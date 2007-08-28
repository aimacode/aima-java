package aima.search.map;

import java.util.ArrayList;
import java.util.List;

import aima.util.Table;
import aima.util.Util;

/**
 * A simple representation of a Map, whereby locations are linked via specified distances.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Map {
	private final ArrayList<String> locations = new ArrayList<String>();

	private Table<String, String, Integer> map = null;

	public Map(String[] locs) {
		for (int i = 0; i < locs.length; i++) {
			locations.add(locs[i]);
		}
		map = new Table<String, String, Integer>(locations, locations);
	}

	public List<String> getLocationsLinkedTo(String fromLocation) {
		ArrayList<String> linkedLocations = new ArrayList<String>();

		for (String toLocation : locations) {
			if (!toLocation.equals(fromLocation)) {
				if (null != getDistance(fromLocation, toLocation)) {
					linkedLocations.add(toLocation);
				}
			}
		}

		return linkedLocations;
	}

	public Integer getDistance(String fromLocation, String toLocation) {
		return map.get(fromLocation, toLocation);
	}

	public void addUnidirectionalLink(String fromLocation, String toLocation,
			Integer distance) {
		map.set(fromLocation, toLocation, distance);
	}

	public void addBidirectionalLink(String fromLocation, String toLocation,
			Integer distance) {
		map.set(fromLocation, toLocation, distance);
		map.set(toLocation, fromLocation, distance);
	}

	public String randomlyGenerateDestination() {
		return Util.selectRandomlyFromList(locations);
	}
}
