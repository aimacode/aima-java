package aimax.osm.viewer;


/**
 * Provides a platform independent interface to draw images. Implementations exist
 * for AWT and Android.
 * @author Daniel Wonnenberg
 *
 */
public interface UnifiedImageBuilder<IMG_TYPE> {

	/** Should be called first! */
	public void initialize(IMG_TYPE image);

	/** Returns the width of the image under construction. */
	public int getWidth();
	
	/** Returns the height of the image under construction. */
	public int getHeight();
	
	/**
	 * Draws a line from (x1,y1) to (x2,y2)
	 */
	public void drawLine(int x1, int y1, int x2, int y2);

	/**
	 * Can draw empty or filled rectangles, depending on the properties set.
	 */
	public void drawRect(int x, int y, int width, int height);
	

	/**
	 * Can draw empty or filled ovals, depending on the properties set.
	 */
	public void drawOval(int x, int y, int width, int height);
	
	/**
	 * Used to draw paths.
	 */
	public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints);
	
	/**
	 * Used to draw closed paths or areas. The method {@link #setAreaFilled} might have been
	 * called before.
	 */
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints);
	
	/**
	 * To display a nice text, lineWidth must be set to 1 and an appropriate font size
	 * must be set
	 * @param x horizontal position of upper left edge
	 * @param y vertical position of upper left edge
	 */
	public void drawString(String text, int x, int y);
	
	/**
	 * Finishes a drawing process
	 * @return The fully drawn Image.
	 */
	public abstract IMG_TYPE getResult();

	/**
	 * Sets the color for the following drawing action.
	 */
	public abstract void setColor(UColor color);
	
	/**
	 * @param dashed 
	 * @param width sets the line width.
	 */
	public abstract void setLineStyle(boolean dashed, float width);
	
	/**
	 * Decides whether to draw or to fill an area.
	 */
	public abstract void setAreaFilled(boolean value);
	
	/**
	 * Changes the text size.
	 */
	public abstract void setFontSize(float size);
	
	/**
	 * Gets the text size.
	 */
	public abstract float getFontSize();
}
