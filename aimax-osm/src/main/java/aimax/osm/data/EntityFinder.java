package aimax.osm.data;

import java.util.List;

import aimax.osm.data.entities.MapEntity;

/**
 * Interface for search engines which find entities by name or by
 * attribute. Search functionality is focused on analysis of the
 * vicinity of a reference position.
 * @author Ruediger Lunde
 */
public interface EntityFinder {
	/** Returns the radius which defines the search area when starting the search. */
	int getMinRadius();
	/** Defines the size of the area which is used when starting the search. */
	void setMinRadius(int km);
	/** Returns the radius of the maximal area which can be analyzed during search. */
	int getMaxRadius();
	/** Defines the maximum dimension of the area to be analyzed. */
	void setMaxRadius(int km);
	/**
	 * Initiates a search for nodes and ways.
	 * @param pattern Part of an entity name, attribute name or attribute value.
	 * @param pos Reference position.
	 */
	void findEntity(String pattern, Position pos);
	/**
	 * Initiates a search for nodes.
	 * @param pattern Part of an entity name, attribute name or attribute value.
	 * @param pos Reference position.
	 */
	void findNode(String pattern, Position pos);
	/**
	 * Initiates a search for ways.
	 * @param pattern Part of an entity name, attribute name or attribute value.
	 * @param pos Reference position.
	 */
	void findWay(String pattern, Position pos, MapWayFilter filter);
	/**
	 * Initiates a search for addresses.
	 * @param address Something like 'Ulm, Prittwitz'.
	 * @param pos Reference position.
	 */
	void findAddress(String address, Position pos);
	/**
	 * Returns true if further results might be obtained by extending the
	 * search area or by using the previously selected intermediate result.
	 */
	boolean canFindMore();
	/**
	 * Continues search by extending the search area or inferring new
	 * results based on a selected intermediate result (address search).
	 */
	void findMore();
	/** Returns the reference position of the current search. */
	Position getRefPosition();
	/**
	 * Returns intermediate results (e.g. possible nodes representing places
	 * with the same name in address search).
	 */
	List<MapEntity> getIntermediateResults();
	/**
	 * Allows to select one object from the intermediate results to continue
	 * search.
	 */
	void selectIntermediateResult(MapEntity entity);
	/** Returns all search results. */
	List<MapEntity> getResults();
}
