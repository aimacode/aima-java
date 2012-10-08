package aimax.osm.gps;

/**
 * Interface which allows locators to inform interested listeners about
 * new gps fixes.
 * @author Ruediger Lunde
 */
public interface GpsPositionListener {
	void positionUpdated(GpsFix pos);
}
