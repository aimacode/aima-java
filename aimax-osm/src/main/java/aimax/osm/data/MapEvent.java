package aimax.osm.data;

/**
 * Provides map data change information to interested listeners.
 * @author Ruediger Lunde
 */
public class MapEvent {
	OsmMap source;
	Type type;
	long objId;
	
	public MapEvent(OsmMap source, Type type) {
		this.source = source;
		this.type = type;
		objId = -1;
	}
	
	public MapEvent(OsmMap source, Type type, long objId) {
		this.source = source;
		this.type = type;
		this.objId = objId;
	}
	
	public OsmMap getSource() {
		return source;
	}
	
	public Type getType() {
		return type;
	}
	
	public long getObjId() {
		return objId;
	}
	
	/** Describes the kind of change. */
	public enum Type {
		MAP_NEW, MAP_MODIFIED, MARKER_ADDED, MARKER_REMOVED, TRACK_MODIFIED, MAP_CLEARED
	}
}
