package aima.core.environment.map2d;

import java.util.Map;

import aima.core.util.datastructure.Point2D;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Implements a Map2D with locations, distance labeled links between the
 * locations, straight line distances, and 2d-placement positions of locations.
 * Locations are represented by strings and travel distances by double values.
 * Locations and links can be added dynamically and removed after creation. This
 * enables to read maps from file or to modify them with respect to newly
 * obtained knowledge.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 */
public class ExtendableMap2D implements Map2D {
	/**
	 * Stores map data. Locations are represented as vertices and connections
	 * (links) as directed edges labeled with corresponding travel distances.
	 */
	private final LabeledGraph<String, Double> links;

	/** Stores xy-coordinates for each location. */
	private final Map<String, Point2D> locationPositions;

	/** Default constructor. Creates an empty map. */
	public ExtendableMap2D() {
		links = new LabeledGraph<>();
		locationPositions = new LinkedHashMap<>();
	}

	//
	// START-Map2D
	@Override
	public List<String> getLocations() {
		return links.getVertexLabels();
	}

	@Override
	public List<String> getLocationsLinkedTo(String fromLocation) {
		List<String> result = links.getSuccessors(fromLocation);
		return result;
	}

	@Override
	public Double getDistance(String fromLocation, String toLocation) {
		return links.get(fromLocation, toLocation);
	}

	@Override
	public Point2D getPosition(String loc) {
		return locationPositions.get(loc);
	}
	// END-Map2D
	//

	/**
	 * Remove everything.
	 */
	public void clear() {
		links.clear();
		locationPositions.clear();
	}

	/**
	 * Clear all connections but keeps location and position information.
	 */
	public void clearLinks() {
		links.clear();
	}

	/**
	 * Add a one-way connection to the map.
	 *
	 * @param fromLocation
	 *            the from location.
	 * @param toLocation
	 *            the to location.
	 * @param distance
	 *            the distance between the two given locations.
	 */
	public void addUnidirectionalLink(String fromLocation, String toLocation, Double distance) {
		links.set(fromLocation, toLocation, distance);
	}

	/**
	 * Adds a connection which can be traveled in both direction. Internally,
	 * such a connection is represented as two one-way connections.
	 *
	 * @param fromLocation
	 *            the from location.
	 * @param toLocation
	 *            the to location.
	 * @param distance
	 *            the distance between the two given locations.
	 */
	public void addBidirectionalLink(String fromLocation, String toLocation, Double distance) {
		links.set(fromLocation, toLocation, distance);
		links.set(toLocation, fromLocation, distance);
	}

	/**
	 * Remove a one-way connection.
	 *
	 * @param fromLocation
	 *            the from location.
	 * @param toLocation
	 *            the to location.
	 */
	public void removeUnidirectionalLink(String fromLocation, String toLocation) {
		links.remove(fromLocation, toLocation);
	}

	/**
	 * Remove the two corresponding one-way connections.
	 *
	 * @param fromLocation
	 *            the from location.
	 * @param toLocation
	 *            the to location.
	 */
	public void removeBidirectionalLink(String fromLocation, String toLocation) {
		links.remove(fromLocation, toLocation);
		links.remove(toLocation, fromLocation);
	}

	/**
	 * Defines the position of a location with respect to a 2 dimensional
	 * coordinate system.
	 *
	 * @param loc
	 *            the location.
	 * @param x
	 *            position x
	 * @param y
	 *            position y
	 */
	public void setPosition(String loc, double x, double y) {
		locationPositions.put(loc, new Point2D(x, y));
	}

	/**
	 * Defines the position of a location within the map. Using this method, one
	 * location should be selected as a reference position (<code>dist=0</code>
	 * and <code>dir=0</code>) and all the other locations should be placed
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
		Point2D coords = new Point2D(-Math.sin(dir * Math.PI / 180.0) * dist, Math.cos(dir * Math.PI / 180.0) * dist);
		links.addVertex(loc);
		locationPositions.put(loc, coords);
	}
}
