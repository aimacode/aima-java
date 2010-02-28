package aimax.osm.data.entities;

import java.util.ArrayList;
import java.util.List;

import aimax.osm.data.EntityVisitor;

/**
 * Represents a node within the map. Nodes
 * can be part of a way, points of interest, or marks.
 * A full java bean interface is provided. So it is
 * easy, to serialize objects using <code>XMLEncoder</code>.
 * @author R. Lunde
 */
public class MapNode extends MapEntity {
	private float lat;
	private float lon;
	private ArrayList<WayRef> ways;
	
	public MapNode() {
		ways = new ArrayList<WayRef>(0);
	}
	
	public MapNode(long id, float lat, float lon) {
		this();
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}
	
	public float getLon() {
		return lon;
	}
	
	public void setLon(float lon) {
		this.lon = lon;
	}

	public List<WayRef> getWays() {
		return ways;
	}

	public void addWayRef(long wayId, int nodeIdx) {
		if (ways.isEmpty() || ways.get(0).wayId != wayId)
			ways.add(new WayRef(wayId, nodeIdx));
	}
	
	public void accept(EntityVisitor visitor) {
		visitor.visitMapNode(this);
	}
	
	public int compareLatitude(float lat) {
		if (this.lat < lat)
			return -1;
		else if (this.lat > lat)
			return 1;
		else
			return 0;
	}
	
	public int compareLongitude(float lon) {
		if (this.lon < lon)
			return -1;
		else if (this.lon > lon)
			return 1;
		else
			return 0;
	}
	
	//////////////////////////////////////////////////////////////////////
	// Some inner classes
	
	/**
	 * Represents a reference to a way. The node index indicates,
	 * where the node maintaining the reference occurs in the way definition.
	 */
	public static class WayRef {
		public long wayId;
		public int nodeIdx;
		public WayRef(long wayId, int nodeIdx) {
			this.wayId = wayId;
			this.nodeIdx = nodeIdx;
		}
	}
}
