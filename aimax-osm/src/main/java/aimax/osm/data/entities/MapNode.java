package aimax.osm.data.entities;

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
	private WayRef ways;
	
	public MapNode(long id, float lat, float lon) {
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

	public WayRefList getWayRefs() {
		return new WayRefList(ways);
	}
	
	public void addWayRef(MapWay way, int nodeIdx) {
		// be careful with closed ways (begin == end)
		if (ways == null || ways.getWay() != way) {
			WayRef ref = new WayRef(way, (short) nodeIdx);
			ref.setNext(ways);
			ways = ref;
		}
	}
	
	public void removeWayRef(MapWay way) {
		if (ways.getWay() == way)
			ways = ways.getNext();
		else {
			WayRef ref = ways;
			while (ref.getNext().getWay() != way)
				ref = ref.getNext();
			ref.setNext(ref.getNext().getNext());
		}
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
}
