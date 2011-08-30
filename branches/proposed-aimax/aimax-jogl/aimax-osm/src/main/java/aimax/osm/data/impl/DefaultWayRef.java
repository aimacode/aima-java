package aimax.osm.data.impl;

import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.WayRef;

/**
 * Represents a reference to a way. The node index indicates,
 * where the node maintaining the reference occurs in the way definition.
 * @author Ruediger Lunde
 */
public class DefaultWayRef implements WayRef {
	private MapWay way;
	private short nodeIdx;
	
	public DefaultWayRef(MapWay way, short nodeIdx) {
		this.way = way;
		this.nodeIdx = nodeIdx;
	}
	/** {@inheritDoc} */
	@Override
	public MapWay getWay() { return way; }
	/** {@inheritDoc} */
	@Override
	public short getNodeIdx() { return nodeIdx; }
}

