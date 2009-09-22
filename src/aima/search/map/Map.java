package aima.search.map;

import java.util.List;
/**
 * Provides a general interface for maps.
 * @author R. Lunde
 */
public interface Map {
	
	/** Returns a list of all locations. */
	public List<String> getLocations();
	
	/**
	 * Answers to the question: Where can I get, following one of the
	 * connections starting at the specified location?
	 */
	public List<String> getLocationsLinkedTo(String fromLocation);

	/**
	 * Returns the travel distance between the two specified locations if
	 * they are linked by a connection and null otherwise.
	 */
	public Integer getDistance(String fromLocation, String toLocation);

	/**
	 * Returns an array with two integers describing the the position of the
	 * specified location.
	 */
	public Point2D getPosition(String loc);
	
	/**
	 * Returns a location which is selected by random.
	 */
	public String randomlyGenerateDestination();
}
