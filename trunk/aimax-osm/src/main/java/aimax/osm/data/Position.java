package aimax.osm.data;

import java.util.Collection;
import java.util.List;

import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.WayRef;

/**
 * Maintains a latitude longitude pair and provides some useful methods for
 * distance calculations.
 * 
 * @author Ruediger Lunde
 */
public class Position {

	/** Earth's mean radius in km. */
	public static double EARTH_RADIUS = 6371.0;
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
	 * Computes the distance in kilometer from this position to a specified map
	 * node.
	 */
	public double getDistKM(MapEntity entity) {
		if (entity instanceof MapNode) {
			return getDistKM(lat, lon, ((MapNode) entity).getLat(),
					((MapNode) entity).getLon());
		} else if (entity instanceof MapWay) {
			MapWay way = (MapWay) entity;
			BoundingBox bb = way.computeBoundingBox();
			float bbLat = (Math.abs(lat - bb.getLatMin()) < Math.abs(lat
					- bb.getLatMax())) ? bb.getLatMin() : bb.getLatMax();
			float bbLon = (Math.abs(lon - bb.getLonMin()) < Math.abs(lon
					- bb.getLonMax())) ? bb.getLonMin() : bb.getLonMax();
			return getDistKM(lat, lon, bbLat, bbLon);
		}
		return Double.NaN;
	}

	public boolean insertInAscendingDistanceOrder(List<MapEntity> nodes,
			MapNode node) {
		int pos = getInsertPosition(nodes, node);
		if (pos != -1)
			nodes.add(pos, node);
		return pos != -1;
	}
	
	public boolean insertInAscendingDistanceOrder(List<MapEntity> ways,
			MapWay way) {
		int pos = getInsertPosition(ways, way);
		if (pos != -1)
			ways.add(pos, way);
		return pos != -1;
	}
	
	private int getInsertPosition(List<?> entities, MapEntity entity) {
		int pos1 = 0;
		int pos2 = entities.size();
		double newDistance = getDistKM(entity);
		while (pos1 < pos2) {
			int pos3 = (pos1 + pos2) / 2;
			double dist3 = getDistKM((MapEntity) entities.get(pos3));
			if (newDistance < dist3)
				pos2 = pos3;
			else if (newDistance > dist3)
				pos1 = pos3 + 1;
			else
				pos1 = pos2 = pos3;
		}
		if (pos1 < entities.size()) {
			for (int i = pos1; i >= 0
					&& getDistKM((MapEntity) entities.get(i)) == newDistance; i--)
				if (entities.get(i) == entity)
					return -1;
			for (int i = pos1 + 1; i < entities.size()
					&& getDistKM((MapEntity) entities.get(i)) == newDistance; i++)
				if (entities.get(i) == entity)
					return -1;
		}
		return pos1;
	}

	/**
	 * Returns the node from <code>nodes</code> which is nearest to this
	 * position. If a filter is given, only those nodes are inspected, which are
	 * part of a way accepted by the filter.
	 * 
	 * @param nodes
	 * @param filter
	 *            possibly null
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
					if (filter.isAccepted(ref.getWay()))
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
	 * Computes a simple approximation of the compass course from this position
	 * to the specified node.
	 * 
	 * @param node
	 * @return Number between 1 and 360
	 */
	public int getCourseTo(MapNode node) {
		double lonCorr = Math.cos(Math.PI / 360.0 * (lat + node.getLat()));
		double latDist = node.getLat() - lat;
		double lonDist = lonCorr * (node.getLon() - lon);
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
			result += getDistKM(n1.getLat(), n1.getLon(), n2.getLat(), n2
					.getLon());
		}
		return result;
	}

	/**
	 * Computes the distance between two positions on the earth surface using
	 * the haversine formula.
	 */
	public static double getDistKM(float lat1, float lon1, float lat2,
			float lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c;
	}
}
