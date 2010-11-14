package aimax.osm.data.entities;

/**
 * Represents a reference to a way. The node index indicates,
 * where the node maintaining the reference occurs in the way definition.
 * 
 * @author Ruediger Lunde
 */
public interface WayRef {
	/** Returns the referenced way. */
	public MapWay getWay();
	/** Returns a position within the sequence of nodes describing the way. */
	public short getNodeIdx();
}
