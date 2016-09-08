package aima.gui.swing.framework.util;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import aima.core.util.math.geom.shapes.Circle2D;
import aima.core.util.math.geom.shapes.Ellipse2D;
import aima.core.util.math.geom.shapes.IGeometric2D;
import aima.core.util.math.geom.shapes.Line2D;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Polyline2D;
import aima.core.util.math.geom.shapes.Rect2D;

/**
 * This class is used to transfer a {@link IGeometric2D} into a {@code java.awt.}{@link Shape} to be able to display these shapes graphically.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public class GraphicsTransfer2D {

	/**
	 * Transfers a {@link IGeometric2D} into its {@link Shape} representative.
	 * @param geometric the geometric shape to be transferred.
	 * @return the transferred shape.
	 */
	public static Shape transfer(IGeometric2D geometric) {
		if(geometric instanceof Circle2D)
			return transfer((Circle2D) geometric);
		else if(geometric instanceof Ellipse2D)
			return transfer((Ellipse2D) geometric);
		else if(geometric instanceof Line2D)
			return transfer((Line2D) geometric);
		else if(geometric instanceof Polyline2D)
			return transfer((Polyline2D) geometric);
		else if(geometric instanceof Rect2D)
			return transfer((Rect2D) geometric);
		return null;
	}
	
	/**
	 * Transfers a {@link Circle2D} into its {@link Shape} representative.
	 * @param circle the circle to be transferred.
	 * @return the transferred shape.
	 */
	private static Shape transfer(Circle2D circle) {
		return new java.awt.geom.Ellipse2D.Double(circle.getCenter().getX()-circle.getRadius(),circle.getCenter().getY()-circle.getRadius(),2*circle.getRadius(),2*circle.getRadius());
	}
	
	/**
	 * Transfers a {@link Ellipse2D} into its {@link Shape} representative.
	 * @param ellipse the ellipse to be transferred.
	 * @return the transferred shape.
	 */
	private static Shape transfer(Ellipse2D ellipse) {
		Shape result = new java.awt.geom.Ellipse2D.Double(ellipse.getCenter().getX()-ellipse.getHorizontalLength(),ellipse.getCenter().getY()-ellipse.getVerticalLength(),2*ellipse.getHorizontalLength(),2*ellipse.getVerticalLength());
		return AffineTransform.getRotateInstance(ellipse.getAngle()).createTransformedShape(result);
	}
	
	/**
	 * Transfers a {@link Line2D} into its {@link Shape} representative.
	 * @param line the line to be transferred.
	 * @return the transferred shape.
	 */
	private static Shape transfer(Line2D line) {
		return new java.awt.geom.Line2D.Double(line.getStart().getX(), line.getStart().getY(), line.getEnd().getX(), line.getEnd().getY());
	}
	
	/**
	 * Transfers a {@link Polyline2D} into its {@link Shape} representative.
	 * @param polyline the polyline to be transferred.
	 * @return the transferred shape.
	 */
	private static Shape transfer(Polyline2D polyline) {
		Path2D result = new java.awt.geom.Path2D.Double();
		Point2D[] vertexes = polyline.getVertexes();
		result.moveTo(vertexes[0].getX(),vertexes[0].getY());
		for(int i=1;i<vertexes.length;i++) {
			result.lineTo(vertexes[i].getX(),vertexes[i].getY());
		}
		if(polyline.isClosed()) result.closePath();
		return result;
	}
	
	/**
	 * Transfers a {@link Rect2D} into its {@link Shape} representative.
	 * @param rect the rectangle to be transferred.
	 * @return the transferred shape.
	 */
	private static Shape transfer(Rect2D rect){
		return new java.awt.geom.Rectangle2D.Double(rect.getLowerLeft().getX(), rect.getLowerLeft().getY(), rect.getUpperRight().getX() - rect.getLowerLeft().getX(), rect.getUpperRight().getY() - rect.getLowerLeft().getY());
	}
}
