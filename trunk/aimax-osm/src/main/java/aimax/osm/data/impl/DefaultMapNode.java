package aimax.osm.data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aimax.osm.data.EntityVisitor;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.WayRef;

/**
 * Default implementation of a node.
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
	
	/** {@inheritDoc} */
	@Override
	public boolean hasPosition() {
		return !Float.isNaN(lat) && !Float.isNaN(lon);
	}

	/** {@inheritDoc} */
	@Override
	public float getLat() {
		return lat;
	}

	/** {@inheritDoc} */
	@Override
	public void setLat(float lat) {
		this.lat = lat;
	}
	
	/** {@inheritDoc} */
	@Override
	public float getLon() {
		return lon;
	}
	
	/** {@inheritDoc} */
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
	
	/** {@inheritDoc} */
	@Override
	public void addWayRef(MapWay way, int nodeIdx) {
		if (ways == null)
			ways = new ArrayList<WayRef>(2);
		// be careful with closed ways (begin == end)
		if (ways.isEmpty() || ways.get(0) != way)
			ways.add(new DefaultWayRef(way, (short) nodeIdx));
	}
	
	/** {@inheritDoc} */
	@Override
	public void removeWayRef(MapWay way) {
		for (int i = 0; i < ways.size(); i++)
			if (ways.get(i).getWay() == way) {
				ways.remove(i);
				i--;
			}
	}
	
	/** {@inheritDoc} */
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
	
	/** {@inheritDoc} */
	@Override
	public int compareLatitude(float lat) {
		if (this.lat < lat)
			return -1;
		else if (this.lat > lat)
			return 1;
		else
			return 0;
	}
	
	/** {@inheritDoc} */
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
