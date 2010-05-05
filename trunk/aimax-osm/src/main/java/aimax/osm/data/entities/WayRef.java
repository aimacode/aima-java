package aimax.osm.data.entities;

import java.util.Iterator;

/**
 * Represents a reference to a way. The node index indicates,
 * where the node maintaining the reference occurs in the way definition.
 * Way references can also have a link to a next reference which provides
 * storage efficient means to represent short lists of references (from which
 * we need thousands of in a map).
 */
public class WayRef {
	private long wayId;
	private short nodeIdx;
	private WayRef next;
	
	public WayRef(long wayId, short nodeIdx) {
		this.wayId = wayId;
		this.nodeIdx = nodeIdx;
	}
	
	
	public long getWayId() { return wayId; }
	public short getNodeIdx() { return nodeIdx; }
	public WayRef getNext() { return next; }
	public void setNext(WayRef next) { this.next = next; }
}
