package aima.core.environment.map2d;

import java.util.List;

import aima.core.util.datastructure.Point2D;

/**
 * Provides a general interface for two dimensional maps.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 */
public interface Map2D {
	/**
	 *
	 * @return a list of all locations in the map.
	 */
	List<String> getLocations();

	/**
	 * Answers to the question: Where can I get, following one of the
	 * connections starting at the specified location?
	 *
	 * @param fromLocation
	 *            locations linked from.
	 * @return a list of the locations that are connected from the given
	 *         location.
	 */
	List<String> getLocationsLinkedTo(String fromLocation);

	/**
	 * Get the travel distance between the two specified locations if they are
	 * linked by a connection and null otherwise.
	 *
	 * @param fromLocation
	 *            the starting from location.
	 * @param toLocation
	 *            the to location.
	 * @return the travel distance between the two specified locations if they
	 *         are linked by a connection and null otherwise.
	 */
	Double getDistance(String fromLocation, String toLocation);

	/**
	 * Get the position of the specified location.
	 *
	 * @param location
	 *            the location whose position is to be returned.
	 * @return the position of the specified location in the two dimensional
	 *         space.
	 */
	Point2D getPosition(String location);
}
