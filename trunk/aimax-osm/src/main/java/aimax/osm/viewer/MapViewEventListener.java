package aimax.osm.viewer;

/**
 * Observer interface which should be implemented by clients interested in
 * map view events.
 * @author Ruediger Lunde
 */
public interface MapViewEventListener {
	void eventHappened(MapViewEvent event);
}
