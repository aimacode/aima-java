package aimax.osm.data;

import java.util.Collection;

import aimax.osm.data.entities.MapNode;

/**
 * Simple representation of a bounding box for geo coordinates.
 * 
 * @author Ruediger Lunde
 */
public class BoundingBox {
	private float latMin;
	private float lonMin;
	private float latMax;
	private float lonMax;

	public BoundingBox() {
		latMin = Float.NaN;
		lonMin = Float.NaN;
		latMax = Float.NaN;
		lonMax = Float.NaN;
	}

	public BoundingBox(float latMin, float lonMin, float latMax, float lonMax) {
		this.latMin = latMin;
		this.lonMin = lonMin;
		this.latMax = latMax;
		this.lonMax = lonMax;
	}

	public BoundingBox(Position center, int radiusKM) {
		double phi = radiusKM / Position.EARTH_RADIUS;
		float dLat = (float) Math.toDegrees(phi);
		float dLon = (float) Math.toDegrees(phi
				/ Math.cos(Math.toRadians(center.getLat())));
		this.latMin = center.getLat() - dLat;
		this.lonMin = center.getLon() - dLon;
		this.latMax = center.getLat() + dLat;
		this.lonMax = center.getLon() + dLon;
	}

	/** Adjusts the box so that the specified box is also included. */
	public BoundingBox unifyWith(BoundingBox bb) {
		latMin = Math.min(latMin, bb.latMin);
		lonMin = Math.min(lonMin, bb.lonMin);
		latMax = Math.max(latMax, bb.latMax);
		lonMax = Math.max(lonMax, bb.lonMax);
		return this;
	}

	/** Adjusts the box so that the result is the intersection of both boxes. */
	public BoundingBox intersectWith(BoundingBox bb) {
		latMin = Math.max(latMin, bb.latMin);
		lonMin = Math.max(lonMin, bb.lonMin);
		latMax = Math.min(latMax, bb.latMax);
		lonMax = Math.min(lonMax, bb.lonMax);
		return this;
	}

	public boolean intersectsWith(BoundingBox bb) {
		if (latMin > bb.latMax || latMax < bb.latMin || lonMin > bb.lonMax
				|| lonMax < bb.lonMin)
			return false;
		else
			return true;
	}

	/**
	 * Makes sure, that all given nodes are within the box and extends the
	 * bounds if necessary.
	 */
	public void adjust(Collection<MapNode> nodes) {
		for (MapNode node : nodes) {
			if (Float.isNaN(latMin)) {
				latMin = latMax = node.getLat();
				lonMin = lonMax = node.getLon();
			} else if (node.hasPosition()) {
				if (node.getLat() < latMin)
					latMin = node.getLat();
				else if (node.getLat() > latMax)
					latMax = node.getLat();
				if (node.getLon() < lonMin)
					lonMin = node.getLon();
				else if (node.getLon() > lonMax)
					lonMax = node.getLon();
			}
		}
	}

	/** Returns the distance between <code>latMax</code> and <code>latMin</code> in Kilometers. */
	public float getNorthSouthDistKM() {
		return (float) Position.getDistKM(latMax, lonMin, latMin, lonMin);
	}

	/**
	 * Returns the distance between <code>lonMin</code> and <code>lonMax</code> at
	 * latitude <code>(latMax-latMin)/2</code> in Kilometers.
	 */
	public float getWestEastDistKM() {
		return (float) Position.getDistKM((latMin + latMax) / 2f, lonMin,
				(latMin + latMax) / 2f, lonMax);
	}

	/**
	 * Checks whether the specified position is inside the box.
	 */
	public boolean isInside(double lat, double lon) {
		return lat >= latMin && lat <= latMax && lon >= lonMin && lon <= lonMax;
	}

	public float getLatMin() {
		return latMin;
	}

	public float getLonMin() {
		return lonMin;
	}

	public float getLatMax() {
		return latMax;
	}

	public float getLonMax() {
		return lonMax;
	}

	public String toString() {
		return "BB(" + latMin + ", " + lonMin + ", " + latMax + ", " + lonMax
				+ ")";
	}
}
