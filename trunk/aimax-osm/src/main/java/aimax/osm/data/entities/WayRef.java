package aimax.osm.data.entities;


/**
 * Represents a reference to a way. The node index indicates,
 * where the node maintaining the reference occurs in the way definition.
 * Way references can also have a link to a next reference which provides
 * storage efficient means to represent short lists of references (from which
 * we need thousands of in a map).
 * @author Ruediger Lunde
 */
public class WayRef {
	private MapWay way;
	private short nodeIdx;
	private WayRef next;
	
	public WayRef(MapWay way, short nodeIdx) {
		this.way = way;
		this.nodeIdx = nodeIdx;
	}
	
	
	public MapWay getWay() { return way; }
	public short getNodeIdx() { return nodeIdx; }
	public WayRef getNext() { return next; }
	public void setNext(WayRef next) { this.next = next; }
}
