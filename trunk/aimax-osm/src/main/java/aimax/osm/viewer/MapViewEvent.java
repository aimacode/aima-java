package aimax.osm.viewer;


/**
 * Allows map views to inform interested listeners about user interactions such
 * as zooming or marker placement.
 * 
 * @author Ruediger Lunde
 * 
 */
public class MapViewEvent {
	Object source;
	Type type;

	/**
	 * Creates a new MapViewEvent.
	 * @param source A map view component.
	 */
	public MapViewEvent(Object source, Type type) {
		this.source = source;
		this.type = type;
	}

	public Object getSource() {
		return source;
	}

	public Type getType() {
		return type;
	}

	public enum Type {
		ADJUST, ZOOM, NEW_MAP, NEW_RENDERER, MARKER_ADDED
	}
}
