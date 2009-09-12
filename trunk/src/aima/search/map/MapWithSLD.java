package aima.search.map;

import java.util.Hashtable;
import java.util.List;

import aima.search.map.Map;
import aima.util.LabeledGraph;
import aima.util.Util;


/**
 * Implements a map with locations, distance labeled links between the
 * locations, straight line distances, and 2d-placement positions of
 * locations. Locations are represented by strings and travel distances
 * by integer values. In spite of its superclass (which was in fact only added
 * for compatibility reasons), in this implementation locations and links can
 * be dynamically added and removed after creation. This enables to read maps
 * from file or to modify it with respect to newly obtained knowledge. 
 * @author R. Lunde
 */
public class MapWithSLD extends Map {

	/**
	 * Stores road data. Locations are represented as vertices and
	 * roads (links) as directed edges labeled with corresponding
	 * travel distances.
	 */
	private final LabeledGraph<String, Integer> roads; 
	
	/** Stores xy-coordinates for each location. */
	private final Hashtable<String, double[]> locationCoords;

	/** Creates an empty map. */
	public MapWithSLD() {
		super(new String[]{}); // we overwrite everything!
		roads = new LabeledGraph<String, Integer>();
		locationCoords = new Hashtable<String, double[]>();
	}
	
	/** Removes everything. */
	public void clear() {
		roads.clear();
		locationCoords.clear();
	}
	
	/** Clears all roads but keeps location position informations. */
	public void clearLinks() {
		roads.clear();
	}
	
	/** Returns a list of all locations. */
	public List<String> getLocations() {
		return roads.getVertexLabels();
	}
	
	/** Checks whether the given string is the name of a location. */
	public boolean isLocation(String str) {
		return roads.isVertexLabel(str);
	}
	
	/////////////////////////////////////////////////////////////////
	// overridden methods of Map
	
	/**
	 * Answers to the question: Where can I get, following one of the
	 * roads starting at the specified location?
	 */
	public List<String> getLocationsLinkedTo(String fromLocation) {		
		return roads.getSuccessors(fromLocation);
	}
	
	/**
	 * Returns the travel distance between the two specified locations
	 * if they are linked by a road and null otherwise.
	 */
	@Override
	public Integer getDistance(String fromLocation, String toLocation) {
		return roads.get(fromLocation, toLocation);
	}

	/** Adds a one-way street to the map. */
	public void addUnidirectionalLink(String fromLocation, String toLocation,
			Integer distance) {
		roads.set(fromLocation, toLocation, distance);
	}

	/**
	 * Adds a road which can be traveled in both direction. Internally,
	 * such a road is represented as two one-way streets.
	 */
	public void addBidirectionalLink(String fromLocation, String toLocation,
			Integer distance) {
		roads.set(fromLocation, toLocation, distance);
		roads.set(toLocation, fromLocation, distance);
	}

	/**
	 * Returns a location which is selected by random.
	 */
	public String randomlyGenerateDestination() {
		return Util.selectRandomlyFromList(getLocations());
	}
	
	/////////////////////////////////////////////////////////////////
	// additional methods
	
	/** Removes a one-way street. */
	public void removeUnidirectionalLink(String fromLocation, String toLocation) {
		roads.remove(fromLocation, toLocation);
	}

	/** Removes the two corresponding one-way streets. */
	public void removeBidirectionalLink(String fromLocation, String toLocation) {
		roads.remove(fromLocation, toLocation);
		roads.remove(toLocation, fromLocation);
	}

	/**
	 * Defines the position of a location as with respect to an orthogonal
	 * coordinate system. 
	 */
	public void setCoords(String loc, double x, double y) {
		locationCoords.put(loc, new double[]{x, y});
	}
	/**
	 * Defines the position of a location within the map. Using this method,
	 * one location should be selected as reference position
	 * (<code>dist=0</code> and <code>dir=0</code>) and all the other
	 * location should be placed relative to it. 
	 * @param loc  location name
	 * @param dist distance to a reference position 
	 * @param dir  bearing (compass direction) in which the location is seen
	 *             from the reference position 
	 */
	public void setDistAndDirToRefLocation(String loc, double dist, int dir) {
		double[] coords = new double[2];
		coords[0] = -Math.sin(dir * Math.PI / 180.0) * dist;
		coords[1] = Math.cos(dir * Math.PI / 180.0) * dist;
		locationCoords.put(loc, coords);
	}

	/**
	 * Returns the straight line distance between two specified locations.
	 * @return positive distance value or -1 denoting no information available. 
	 */
	public double getStraightLineDistance(String loc1, String loc2) {
		double result = -1;
		double[] cl1 = locationCoords.get(loc1);
		double[] cl2 = locationCoords.get(loc2);
		if (cl1 != null && cl2 != null) {
			result = (cl2[0]-cl1[0])*(cl2[0]-cl1[0]);
			result += (cl2[1]-cl1[1])*(cl2[1]-cl1[1]);
			result = Math.sqrt(result);
		}
		return result;
	}

	/**
	 * Returns an array with two integers describing the
	 * the position of the specified location.
	 */
	public double[] getXY(String loc) {
		return locationCoords.get(loc);
	}

}
