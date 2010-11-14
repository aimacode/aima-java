package aimax.osm.data.entities;


/**
 * Represents a reference to a way. The node index indicates,
 * where the node maintaining the reference occurs in the way definition
 * (zoom level 0).
 * 
 * @author Ruediger Lunde
 */
public interface WayRef {
	public MapWay getWay();
	public short getNodeIdx();
}
