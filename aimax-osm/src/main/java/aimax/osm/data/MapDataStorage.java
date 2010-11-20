package aimax.osm.data;

import java.util.Collection;
import java.util.List;

import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

/**
 * Central container for OSM map data. It is responsible for storing loaded map
 * data and also for data preparation to support efficient routing and map
 * viewing. Data preparation is based on two fundamental tree structures:
 * 
 * <p>
 * The first is an entity classifier. It is used to attach viewing information
 * to map entities based on attribute value checks. Renderers can store whatever
 * they need in those view information objects. However, the details are not
 * visible to this layer. The data store only sees the minimal scale in which
 * the entity shall be visible.
 * </p>
 * 
 * <p>
 * The second is a kd-tree (see {@link aimax.osm.data.impl.KDTree}).
 * </p>
 * 
 * <p>
 * The map data store is used as model for the viewer.
 * </p>
 * 
 * @author Ruediger Lunde
 */
public interface MapDataStorage extends WayNodeProvider {

	/** Checks whether data is available. */
	public boolean isEmpty();

	/**
	 * Returns a builder object, which receives map entities, for example from a
	 * reader, and builds up needed structures in the map for storing the
	 * entities (e.g. spatial indices).
	 */
	public MapContentBuilder getContentBuilder();

	/**
	 * Closes all open resources and should be called before the application
	 * terminates.
	 */
	public void close();

	/**
	 * Returns a node for the given id.
	 */
	public MapNode getNode(long id);

	/**
	 * Returns a way for the given id.
	 */
	public MapWay getWay(long id);

	/**
	 * Adds a new point at the end of a specified track. If a track with the
	 * specified name does not exist, a new track is created.
	 */
	public void addToTrack(String trackName, Position pos);

	/**
	 * Provides the data store with an entity classifier. The classifier
	 * strongly influences the generation of the entity tree.
	 */
	public void setEntityClassifier(EntityClassifier<EntityViewInfo> classifier);

	/**
	 * Returns a bounding box for all entities, which are maintained in this
	 * container.
	 */
	public BoundingBox getBoundingBox();

	/** Returns the number of all maintained nodes. */
	public int getNodeCount();

	/** Returns the number of all maintained ways. */
	public int getWayCount();

	/** Returns all map ways. */
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
	 * Returns all points of interest. Nodes are classified as POIs if they have
	 * a name or other attributes of interest.
	 */
	public List<MapNode> getPois(BoundingBox bb);

	/**
	 * Returns all nodes which are marked with the attribute place and whose
	 * name matches the specification.
	 */
	public List<MapNode> getPlaces(String name);

	/** Resets only mark and track information. */
	public void clearMarksAndTracks();

	/** Returns all marks and tracks, which are visible in the specified scale. */
	public List<MapEntity> getVisibleMarksAndTracks(float scale);

	/** Adds a new mark at the specified position. */
	public MapNode addMark(float lat, float lon);

	/** Removes a previously added marker. */
	public void removeMark(MapNode mark);

	/** Returns all maintained marks. */
	public List<MapNode> getMarks();

	/** Checks whether a mark is set at the specified position. */
	public boolean isMarked(double lat, double lon);

	/** Removes the specified track. */
	public void clearTrack(String trackName);

	/**
	 * Creates a new track and adds it to the list of tracks. Possibly exiting
	 * tracks with the same name are removed.
	 */
	public void createTrack(String trackName, List<Position> positions);

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
	 * map. In each row, the first column contains the attribute name and the second
	 * the corresponding value.
	 */
	public Object[][] getStatistics();

	/**
	 * Enables interested visitors to visit all entities within a certain area
	 * which are visible in the specified scale.
	 */
	public void visitEntities(EntityVisitor visitor, BoundingBox bb, float scale);

	/** Adds a listener for map data events. */
	public void addMapDataEventListener(MapDataEventListener listener);

	/** Removes a listener for map data events. */
	public void removeMapDataEventListener(MapDataEventListener listener);

	/** Informs all interested listeners about map changes. */
	public void fireMapDataEvent(MapDataEvent event);
}
