package aima.core.util.math.geom.shapes;

/**
 * This interface defines functionality that geometric shapes in a two-dimensional Cartesian plot have to implement.<br/>
 * If pre-calculations can speed up the functions {@code isInside},{@code isInsideBorder} and {@code rayCast} these calculations should be made.<br/>
 * The reason being that these functions will be called more frequently than the constructor of the shapes.
 *
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public interface IGeometric2D {
	
	/**
	 * Generates a random point that is on this geometric shape. {@code isInsideBorder()} has to return {@code true} for that point.
	 * @return a new random point inside the shape.
	 */
	Point2D randomPoint();
	/**
	 * Tests whether the point is inside this geometric shape excluding its border.
	 * @param point the point to be tested.
	 * @return true if the point is inside the shape.
	 */
	boolean isInside(Point2D point);
	/**
	 * Tests whether the point is inside this geometric shape including its border.
	 * @param point the point to be tested.
	 * @return true if the point is inside the shape or on its border.
	 */
	boolean isInsideBorder(Point2D point);
	/**
	 * Intersects this geometric shape with a ray.
	 * @param ray the ray to intersect.
	 * @return the length of the ray until it intersects with this shape. {@code Double.POSITIVE_INFINITY} if it does not intersect this shape.
	 */
	double rayCast(Ray2D ray);
	/**
	 * Gets the boundaries of this geometric shape as a {@link Rect2D}.
	 * @return the boundaries of this shape.
	 */
	Rect2D getBounds();
	/**
	 * Calculates a {@link TransformMatrix2D} onto this geometric shape.
	 * @param matrix the transformation matrix that gets calculated into this shape.
	 * @return the transformed shape.
	 */
	IGeometric2D transform(TransformMatrix2D matrix);
}
