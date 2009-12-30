package aimax.osm.viewer;

import aimax.osm.data.BoundingBox;

/**
 * Class which is responsible for transformations between world
 * and view coordinates.
 * @author rlunde
 *
 */
public class CoordTransformer {
	/** Longitude of left upper corner. */
	private float originLon;
	/** Latitude of left upper corner. */
	private float originLat;
	/** Correction factor for longitude values. */
	private float lonCorr;
	/** Number of pixels corresponding to one degree of latitude. */
	private float scale;
	
	/**
	 * Adjusts the transformation with respect to a given world coordinate
	 * bounding box.
	 */
	public void adjustTransformation(BoundingBox bb, int viewWidth, int viewHeight) {
		if (bb != null) {
			lonCorr = (float) Math.cos((bb.getLatMax() + bb.getLatMin()) / 360.0 * Math.PI);
	    	// adjust coordinates relative to the left upper corner of the graph area
	    	float scaleX = 1f;
	    	float scaleY = 1f;
	    	if (bb.getLonMax() > bb.getLonMin())
	    		scaleX = viewWidth / ((bb.getLonMax()-bb.getLonMin()) * lonCorr);
	    	if (bb.getLatMax() > bb.getLatMin())
	    		scaleY = viewHeight / (bb.getLatMax()-bb.getLatMin());
	    	scale = Math.max(scaleX, scaleY);
	    	originLon = bb.getLonMin();
	    	originLat = bb.getLatMax();
	    	originLon += (bb.getLonMax() - lon(viewWidth)) / 2.0;
	    	originLat += (bb.getLatMin() - lat(viewHeight)) / 2.0;
		}
	}
	
	/**
	 * Multiples the current scale with the specified factor and
	 * adjusts the view so that the objects shown at the
	 * specified view focus keep at their position.
	 */
	public void zoom(float factor, int focusX, int focusY) {
		float focusLon = lon(focusX);
		float focusLat = lat(focusY);
		scale *= factor;
		int focusXNew = x(focusLon);
		int focusYNew = y(focusLat);
		adjust(focusX-focusXNew, focusY-focusYNew);
	}
	
	/**
	 * Adjusts the transformation.
	 * @param dx Number of pixels for horizontal shift.
	 * @param dy Number of pixels for vertical shift.
	 */
	public void adjust(double dx, double dy) {
		this.originLon -= dx / (scale * lonCorr);
		this.originLat += dy / scale;
	}
	
	/** Returns the x_view of a given longitude value in world coordinates. */
	public int x(double lon) { return (int) Math.round(scale * (lon - originLon) * lonCorr); }
	/** Returns the y_view of a given latitude value in world coordinates. */
	public int y(double lat) { return (int) Math.round(scale * (originLat - lat)); }
	
	/** Computes the corresponding longitude for a given view x coordinate. */
	public float lon(int x) {
		return x / (scale * lonCorr) + originLon;
	}
	
	/** Computes the corresponding latitude for a given view y coordinate. */
	public float lat(int y) {
		return originLat - y / scale;
	}
	
	/** Returns the current {@link #scale} value. */
	public float getScale() {
		return scale;
	}
}
