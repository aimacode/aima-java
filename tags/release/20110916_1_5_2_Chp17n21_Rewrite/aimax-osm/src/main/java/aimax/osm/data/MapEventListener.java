package aimax.osm.data;

/**
 * Allows to listen to map changes.
 * @author Ruediger Lunde
 */
public interface MapEventListener {
	void eventHappened(MapEvent event);
}
