package aimax.osm.data;

import java.util.Collection;

import aimax.osm.data.entities.MapNode;


/**
 * Simple representation of a bounding box for geo coordinates.
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
	
	/**
	 * Makes sure, that all given nodes are within the box and extends
	 * the bounds if necessary.
	 */
	public void adjust(Collection<MapNode> nodes) {
		for (MapNode node : nodes) {
			if (Double.isNaN(latMin)) {
				latMin = latMax = node.getLat();
				lonMin = lonMax = node.getLon();
			} else {
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
	
	/**
	 * Checks whether the specified position is inside the box.
	 */
	public boolean isInside(double lat, double lon) {
		return lat >= latMin && lat <= latMax
		&& lon >= lonMin && lon <= lonMax;
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
}
