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
	
	public boolean hasPosition();
	
	public float getLat();
	
	public void setLat(float lat);
	
	public float getLon();
	
	public void setLon(float lon);

	/** Provides read-only access to the maintained list of way references. */
	public List<WayRef> getWayRefs();
	
	public void addWayRef(MapWay way, int nodeIdx);
	
	public void removeWayRef(MapWay way);
}
