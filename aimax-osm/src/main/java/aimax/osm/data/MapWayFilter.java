package aimax.osm.data;

import aimax.osm.data.entities.MapWay;

/**
 * Decides, whether a map way is accepted or not by checking its id.
 * @author Ruediger Lunde
 */
public interface MapWayFilter {
	public boolean isAccepted(MapWay way);
}
