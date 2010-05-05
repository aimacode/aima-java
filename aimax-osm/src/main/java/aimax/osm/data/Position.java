package aimax.osm.data;

import java.util.Collection;
import java.util.List;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.WayRef;


/**
 * Maintains a latitude longitude pair and provides some useful
 * methods for distance calculations.
 * @author R. Lunde
 */
public class Position {

	protected float lat;
	protected float lon;
	
	public Position(float lat, float lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public Position(MapNode node) {
		lat = node.getLat();
		lon = node.getLon();
	}
	
	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}
	
	/**
	 * Computes the distance in kilometer from this position to a
	 * specified map node.
	 */
	public double getDistKM(MapNode node) {
		return getDistKM(lat, lon, node.getLat(), node.getLon());
	}
	
	/**
	 * Returns the node from <code>nodes</code> which is nearest to this position.
	 * If a filter is given, only those nodes are inspected, which
	 * are part of a way accepted by the filter.
	 * @param nodes
	 * @param filter possibly null
	 * @return A node or null
	 */
	public MapNode selectNearest(Collection<MapNode> nodes, MapWayFilter filter) {
		MapNode result = null;
		double dist = Double.MAX_VALUE;
		double newDist;
		for (MapNode node : nodes) {
			newDist = getDistKM(node);
			boolean found = (newDist < dist);
			if (found && filter != null) {
				found = false;
				for (WayRef ref : node.getWayRefs()) {
					if (filter.isAccepted(ref.getWayId()))
						found = true;
				}
			}
			if (found) {
				result = node;
				dist = newDist;
			}
		}
		return result;
	}
	
	/**
	 * Computes a simple approximation of the compass course from this position to
	 * the specified node.
	 * @param node
	 * @return Number between 1 and 360
	 */
	public int getCourseTo(MapNode node) {
		double lonCorr = Math.cos(Math.PI / 360.0 * (lat + node.getLat()));
		double latDist = node.getLat()-lat;
		double lonDist = lonCorr * (node.getLon()-lon);
		int course = (int) (180.0 / Math.PI * Math.atan2(lonDist, latDist));
		if (course <= 0)
			course += 360;
		return course;
	}
	
	/** Computes the total length of a track in kilometers. */
	public static double getTrackLengthKM(List<MapNode> nodes) {
		double result = 0.0;
		for (int i = 1; i < nodes.size(); i++) {
			MapNode n1 = nodes.get(i - 1);
			MapNode n2 = nodes.get(i);
			result += getDistKM(n1.getLat(), n1.getLon(), n2.getLat(), n2.getLon());
		}
		return result;
	}
	
	/**
	 * Computes the distance between two positions on the earth surface
	 * using the haversine formula.
	 */
	public static double getDistKM(float lat1, float lon1, float lat2, float lon2) {
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double radius = 6371; // earth's mean radius 
        return radius * c;
	}
}
