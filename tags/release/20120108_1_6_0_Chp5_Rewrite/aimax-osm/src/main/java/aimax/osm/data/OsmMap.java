package aimax.osm.data;

import java.util.Collection;
import java.util.List;

import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

/**
 * Central container interface for OSM map data. It provides read access to
 * nodes and ways suitable for routing and map viewing. Additionally, tracks
 * and markers can be added and accessed. Map generation is delegated to a
 * <code>MapBuilder</code>. 
 * 
 * @author Ruediger Lunde
 */
public interface OsmMap extends WayNodeProvider {

	/** Checks whether data is available. */
	public boolean isEmpty();

	/**
	 * Returns a builder object, which receives map entities, for example from a
	 * reader, and builds up needed structures in the map for storing the
	 * entities (e.g. spatial indices). Normally, implementations will keep the
	 * current entities and add to them, what is provided by some map reader.
	 */
	public MapBuilder getBuilder();

	/**
	 * Closes all open resources and should be called before the application
	 * terminates.
	 */
	public void close();

	/**
	 * Provides the map with an entity classifier. The classifier defines the
	 * scale-dependent visibility of entities and by that strongly influences
	 * the organization of the data. This operation might not always be
	 * implemented because large maps are hard to reorganize in reasonable time.
	 */
	public void setEntityClassifier(EntityClassifier<EntityViewInfo> classifier);

	/**
	 * Returns a bounding box for all entities, which are maintained in this
	 * container.
	 */
	public BoundingBox getBoundingBox();

	/** Returns the number of all maintained nodes. */
	public int getNodeCount();

	/**
	 * Returns a node for the given id.
	 */
	public MapNode getNode(long id);

	/** Returns the number of all maintained ways. */
	public int getWayCount();

	/**
	 * Returns a way for the given id.
	 */
	public MapWay getWay(long id);

	/**
	 * Returns all map ways whose bounding box intersects the specified bounding
	 * box.
	 */
	public Collection<MapWay> getWays(BoundingBox bb);

	/**
	 * Returns the nearest way node from all ways which are accepted by the
	 * filter.
	 * 
	 * @param pos
	 *            The reference position.
	 */
	public MapNode getNearestWayNode(Position pos, MapWayFilter filter);

	/**
	 * Returns the number of all maintained point of interests. Nodes are
	 * classified as POIs if they have a name or other attributes of interest.
	 */
	public int getPoiCount();

	/**
	 * Returns all points of interest within the specified bounding box. Nodes
	 * are classified as POIs if they have a name or other attributes of
	 * interest.
	 */
	public List<MapNode> getPois(BoundingBox bb);

	/**
	 * Returns all nodes which are marked with the attribute place and whose
	 * name matches the specification.
	 */
	public List<MapNode> getPlaces(String name);

	/** Resets only marker and track informations. */
	public void clearMarkersAndTracks();

	/**
	 * Returns all markers and tracks, which are visible in the specified scale.
	 */
	public List<MapEntity> getVisibleMarkersAndTracks(float scale);

	/** Adds a new marker at the specified position. */
	public MapNode addMarker(float lat, float lon);

	/** Removes a previously added marker. */
	public void removeMarker(MapNode marker);

	/** Returns all maintained markers. */
	public List<MapNode> getMarkers();

	/** Removes the specified track. */
	public void clearTrack(String trackName);

	/**
	 * Creates a new track and adds it to the list of tracks. Possibly exiting
	 * tracks with the same name are removed.
	 */
	public void createTrack(String trackName, List<Position> positions);

	/**
	 * Adds a new point at the end of a specified track. If a track with the
	 * specified name does not exist, a new track is created.
	 */
	public void addToTrack(String trackName, Position pos);

	/** Returns all maintained tracks. */
	public List<Track> getTracks();

	/** Returns the track with the specified id. */
	public Track getTrack(long trackId);

	/** Returns the track with the specified name. */
	public Track getTrack(String trackName);

	/**
	 * Returns a search engine for finding entities with specific attributes and
	 * names in the map.
	 */
	public EntityFinder getEntityFinder();

	/**
	 * Provides a table with two columns with statistical information about the
	 * map. In each row, the first column contains the attribute name and the
	 * second the corresponding value.
	 */
	public Object[][] getStatistics();

	/**
	 * Enables interested visitors to visit all entities within a certain area
	 * which are visible in the specified scale.
	 */
	public void visitEntities(EntityVisitor visitor, BoundingBox bb, float scale);

	/** Adds a listener for map data events. */
	public void addMapDataEventListener(MapEventListener listener);

	/** Removes a listener for map data events. */
	public void removeMapDataEventListener(MapEventListener listener);

	/** Informs all interested listeners about map changes. */
	public void fireMapDataEvent(MapEvent event);
}
