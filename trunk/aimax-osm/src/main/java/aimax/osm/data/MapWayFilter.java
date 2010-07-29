package aimax.osm.data;

/**
 * Decides, whether a map way is accepted or not by checking its id.
 * @author Ruediger Lunde
 */
public interface MapWayFilter {
	public boolean isAccepted(long wayId);
}
