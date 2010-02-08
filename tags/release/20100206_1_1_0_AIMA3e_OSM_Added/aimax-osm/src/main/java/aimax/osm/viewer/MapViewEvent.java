package aimax.osm.viewer;

public class MapViewEvent {
	MapViewPane source;
	Type type;
	public MapViewEvent(MapViewPane source, Type type) {
		this.source = source;
		this.type = type;
	}
	
	public MapViewPane getSource() {
		return source;
	}
	
	public Type getType() {
		return type;
	}
	
	public enum Type {
		ADJUST, ZOOM, MAP_NEW, MARK_ADDED, TRK_PT_ADDED, TMP_NODES_REMOVED
	}
}
