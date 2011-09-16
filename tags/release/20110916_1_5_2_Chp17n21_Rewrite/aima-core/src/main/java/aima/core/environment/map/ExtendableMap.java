package aima.core.environment.map;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import aima.core.util.Util;
import aima.core.util.datastructure.LabeledGraph;
import aima.core.util.datastructure.Point2D;

/**
 * Implements a map with locations, distance labeled links between the
 * locations, straight line distances, and 2d-placement positions of locations.
 * Locations are represented by strings and travel distances by double values.
 * Locations and links can be added dynamically and removed after creation. This
 * enables to read maps from file or to modify them with respect to newly
 * obtained knowledge.
 * 
 * @author Ruediger Lunde
 */
public class ExtendableMap implements Map {

	/**
	 * Stores map data. Locations are represented as vertices and connections
	 * (links) as directed edges labeled with corresponding travel distances.
	 */
	private final LabeledGraph<String, Double> links;

	/** Stores xy-coordinates for each location. */
	private final Hashtable<String, Point2D> locationPositions;

	/** Creates an empty map. */
	public ExtendableMap() {
		links = new LabeledGraph<String, Double>();
		locationPositions = new Hashtable<String, Point2D>();
	}

	/** Removes everything. */
	public void clear() {
		links.clear();
		locationPositions.clear();
	}

	/** Clears all connections but keeps location position informations. */
	public void clearLinks() {
		links.clear();
	}

	/** Returns a list of all locations. */
	public List<String> getLocations() {
		return links.getVertexLabels();
	}

	/** Checks whether the given string is the name of a location. */
	public boolean isLocation(String str) {
		return links.isVertexLabel(str);
	}

	/**
	 * Answers to the question: Where can I get, following one of the
	 * connections starting at the specified location?
	 */
	public List<String> getLocationsLinkedTo(String fromLocation) {
		List<String> result = links.getSuccessors(fromLocation);
		Collections.sort(result);
		return result;
	}

	/**
	 * Returns the travel distance between the two specified locations if they
	 * are linked by a connection and null otherwise.
	 */
	public Double getDistance(String fromLocation, String toLocation) {
		return links.get(fromLocation, toLocation);
	}

	/** Adds a one-way connection to the map. */
	public void addUnidirectionalLink(String fromLocation, String toLocation,
			Double distance) {
		links.set(fromLocation, toLocation, distance);
	}

	/**
	 * Adds a connection which can be traveled in both direction. Internally,
	 * such a connection is represented as two one-way connections.
	 */
	public void addBidirectionalLink(String fromLocation, String toLocation,
			Double distance) {
		links.set(fromLocation, toLocation, distance);
		links.set(toLocation, fromLocation, distance);
	}

	/**
	 * Returns a location which is selected by random.
	 */
	public String randomlyGenerateDestination() {
		return Util.selectRandomlyFromList(getLocations());
	}

	/** Removes a one-way connection. */
	public void removeUnidirectionalLink(String fromLocation, String toLocation) {
		links.remove(fromLocation, toLocation);
	}

	/** Removes the two corresponding one-way connections. */
	public void removeBidirectionalLink(String fromLocation, String toLocation) {
		links.remove(fromLocation, toLocation);
		links.remove(toLocation, fromLocation);
	}

	/**
	 * Defines the position of a location as with respect to an orthogonal
	 * coordinate system.
	 */
	public void setPosition(String loc, double x, double y) {
		locationPositions.put(loc, new Point2D(x, y));
	}

	/**
	 * Defines the position of a location within the map. Using this method, one
	 * location should be selected as reference position (<code>dist=0</code>
	 * and <code>dir=0</code>) and all the other location should be placed
	 * relative to it.
	 * 
	 * @param loc
	 *            location name
	 * @param dist
	 *            distance to a reference position
	 * @param dir
	 *            bearing (compass direction) in which the location is seen from
	 *            the reference position
	 */
	public void setDistAndDirToRefLocation(String loc, double dist, int dir) {
		Point2D coords = new Point2D(-Math.sin(dir * Math.PI / 180.0) * dist,
				Math.cos(dir * Math.PI / 180.0) * dist);
		links.addVertex(loc);
		locationPositions.put(loc, coords);
	}

	/**
	 * Returns the position of the specified location as with respect to an
	 * orthogonal coordinate system.
	 */
	public Point2D getPosition(String loc) {
		return locationPositions.get(loc);
	}
}
