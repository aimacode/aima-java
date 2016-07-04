package aima.core.util.datastructure;

/**
 * Simplified version of <code>java.awt.geom.Point2D</code>. We do not want
 * dependencies to presentation layer packages here.
 * 
 * @author R. Lunde
 * @author Mike Stampone
 */
public class Point2D {
	private double x;
	private double y;

	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the X coordinate of this <code>Point2D</code> in
	 * <code>double</code> precision.
	 * 
	 * @return the X coordinate of this <code>Point2D</code>.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the Y coordinate of this <code>Point2D</code> in
	 * <code>double</code> precision.
	 * 
	 * @return the Y coordinate of this <code>Point2D</code>.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Returns the Euclidean distance between a specified point and this point.
	 * 
	 * @return the Euclidean distance between a specified point and this point.
	 */
	public double distance(Point2D pt) {
		// Distance Between X Coordinates
		double x_distance = (pt.getX() - x) * (pt.getX() - x);
		// Distance Between Y Coordinates
		double y_distance = (pt.getY()-y)*(pt.getY()-y) ;
		// Distance Between 2d Points
		double total_distance = Math.sqrt(x_distance + y_distance) ;
		
		return total_distance ;
	}
}
