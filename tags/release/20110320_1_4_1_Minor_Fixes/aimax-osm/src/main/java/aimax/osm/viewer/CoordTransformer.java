package aimax.osm.viewer;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.Position;

/**
 * Class which is responsible for transformations between world
 * and view coordinates. It implements an equirectangular projection.
 * World coordinates are specified in latitude and longitude, view coordinates
 * in pixel coordinates (x and y). The actual screen resolution is maintained
 * for scale computation. Additionally, a unit-per-inch factor is maintained
 * for transforming logical symbol size into pixels. 
 * @author Ruediger Lunde
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
	private float dotsPerDeg;
	/**
	 * Factor for scaling symbols (widths of lines, size of icons etc.).
	 * We use logical units for defining the size of different kinds of symbols
	 * and compute the actual number of pixels for a symbol by multiplying
	 * the symbol's logical size with <code>dotsPerInch/unitsPerInch</code>.
	 * Default value is 92. This means, that a symbol with a specified size
	 * of 92 units should have the size of one inch on the screen.
	 */
	public static float unitsPerInch = 92f;
	/**
	 * Number of pixels per inch on the screen (100 per default). The value
	 * maintains the actual screen resolution (true size of a pixel) and is
	 * used for scale computation. Default value is {@link #unitsPerInch}.
	 */
	private float dotsPerInch = unitsPerInch;
	
	/** Sets the screen resolution (used for scale computation). */
	public void setScreenResolution(int dotsPerInch) {
		this.dotsPerInch = dotsPerInch;
	}
	
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
	    	dotsPerDeg = Math.max(scaleX, scaleY);
	    	originLon = bb.getLonMin();
	    	originLat = bb.getLatMax();
	    	originLon += (bb.getLonMax() - lon(viewWidth)) / 2.0;
	    	originLat += (bb.getLatMin() - lat(viewHeight)) / 2.0;
		} else {
			lonCorr = 1.0f;
			dotsPerDeg = 100;
	    	originLon = 0;
	    	originLat = 0;
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
		dotsPerDeg *= factor;
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
		this.originLon -= dx / (dotsPerDeg * lonCorr);
		this.originLat += dy / dotsPerDeg;
	}
	
	/** Returns the x_view of a given longitude value in world coordinates. */
	public int x(double lon) { return (int) Math.round(dotsPerDeg * (lon - originLon) * lonCorr); }
	/** Returns the y_view of a given latitude value in world coordinates. */
	public int y(double lat) { return (int) Math.round(dotsPerDeg * (originLat - lat)); }
	
	/** Computes the corresponding longitude for a given view x coordinate. */
	public float lon(int x) {
		return x / (dotsPerDeg * lonCorr) + originLon;
	}
	
	/** Computes the corresponding latitude for a given view y coordinate. */
	public float lat(int y) {
		return originLat - y / dotsPerDeg;
	}
	
	/** Returns the current {@link #dotsPerDeg} value. */
	public float getDotsPerDeg() {
		return dotsPerDeg;
	}
	
	/**
	 * Returns the scale. 1 / 100 000 means one cm on the screen corresponds to
	 * 1 km in real world. */
	public float computeScale() {
		//System.out.println(dotsPerInch + " ... " + Toolkit.getDefaultToolkit().getScreenResolution());
		//System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
		double kmPerInch = 0.0254e-3;
		double kmPerDeg = Position.EARTH_RADIUS * Math.toRadians(1.0);
		return (float) (dotsPerDeg / dotsPerInch * kmPerInch / kmPerDeg);	
	}
	
	/** Returns a factor for converting logical symbol size units into screen dots. */
	public float getDotsPerUnit() {
		return dotsPerInch / unitsPerInch;
	}
}
