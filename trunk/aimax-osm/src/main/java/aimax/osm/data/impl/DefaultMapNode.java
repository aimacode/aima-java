package aimax.osm.data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aimax.osm.data.EntityVisitor;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.WayRef;

/**
 * Represents a node within the map. Nodes
 * can be part of a way, points of interest, or marks.
 * A full java bean interface is provided. So it is
 * easy, to serialize objects using <code>XMLEncoder</code>.
 * @author Ruediger Lunde
 */
public class DefaultMapNode extends DefaultMapEntity implements MapNode {
	private float lat;
	private float lon;
	private List<WayRef> ways;
	
	public DefaultMapNode(long id) {
		this.id = id;
		this.lat = Float.NaN;
		this.lon = Float.NaN;
	}
	
	@Override
	public boolean hasPosition() {
		return !Float.isNaN(lat) && !Float.isNaN(lon);
	}

	@Override
	public float getLat() {
		return lat;
	}

	@Override
	public void setLat(float lat) {
		this.lat = lat;
	}
	
	@Override
	public float getLon() {
		return lon;
	}
	
	@Override
	public void setLon(float lon) {
		this.lon = lon;
	}

	/** Provides read-only access to the maintained list of way references. */
	@Override
	public List<WayRef> getWayRefs() {
		if (ways == null)
			return Collections.emptyList();
		else
			return Collections.unmodifiableList(ways);
	}
	
	@Override
	public void addWayRef(MapWay way, int nodeIdx) {
		if (ways == null)
			ways = new ArrayList<WayRef>(2);
		// be careful with closed ways (begin == end)
		if (ways.isEmpty() || ways.get(0) != way)
			ways.add(new DefaultWayRef(way, (short) nodeIdx));
	}
	
	@Override
	public void removeWayRef(MapWay way) {
		for (int i = 0; i < ways.size(); i++)
			if (ways.get(i).getWay() == way) {
				ways.remove(i);
				i--;
			}
	}
	
	@Override
	public void accept(EntityVisitor visitor) {
		visitor.visitMapNode(this);
	}
	
	@Override
	public String toString() {
		return "Node(" + id + ")";
	}
	
	/////////////////////////////////////////////////////////////////
	// extensions for KDTree
	
	@Override
	public int compareLatitude(float lat) {
		if (this.lat < lat)
			return -1;
		else if (this.lat > lat)
			return 1;
		else
			return 0;
	}
	
	@Override
	public int compareLongitude(float lon) {
		if (this.lon < lon)
			return -1;
		else if (this.lon > lon)
			return 1;
		else
			return 0;
	}
}
