package aimax.osm.data.entities;

import java.util.List;

/**
 * Represents a node within the map. Nodes
 * can be part of a way, points of interest, or marks.
 * A full java bean interface is provided. So it is
 * easy, to serialize objects using <code>XMLEncoder</code>.
 * @author Ruediger Lunde
 */
public interface MapNode extends MapEntity {
	
	/** Checks whether latitude and longitude values have been set. */
	public boolean hasPosition();
	/** Returns the latitude value. */
	public float getLat();
	/** Sets the latitude value for the node. */
	public void setLat(float lat);
	/** Returns the longitude value. */
	public float getLon();
	/** Sets the longitude value for the node. */
	public void setLon(float lon);

	/** Provides read-only access to the maintained list of way references. */
	public List<WayRef> getWayRefs();
	/** Adds the information to the node that it is part of the specified way. */
	public void addWayRef(MapWay way, int nodeIdx);
	/** Removes the link to the specified way. */
	public void removeWayRef(MapWay way);
}
