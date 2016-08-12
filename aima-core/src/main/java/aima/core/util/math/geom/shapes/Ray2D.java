package aima.core.util.math.geom.shapes;

/**
 * This class implements a ray in a two-dimensional Cartesian plot.<br/>
 * A ray is represented by a {@link Point2D} and a directional {@link Vector2D}.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class Ray2D {
	
	private final Point2D start;
	private final Vector2D direction;
	
	/**
	 * @param start the initial point from that the ray originates.
	 * @param direction the direction in which the ray beams.
	 */
	public Ray2D(Point2D start, Vector2D direction) {
		this.start = start;
		this.direction = direction;
	}
	
	/**
	 * @param startX the X coordinate of the initial point from which the ray originates.
	 * @param startY the Y coordinate of the initial point from which the ray originates.
	 * @param endX the X coordinate of any point on the ray that is different from the start.
	 * @param endY the Y coordinate of any point on the ray that is different from the start.
	 */
	public Ray2D(double startX, double startY, double endX, double endY) {
		this.start = new Point2D(startX, startY);
		this.direction = new Vector2D(endX - startX, endY - startY);
	}

	/**
	 * @return the origin of the ray.
	 */
	public Point2D getStart() {
		return start;
	}

	/**
	 * @return the direction of the ray.
	 */
	public Vector2D getDirection() {
		return direction;
	}
	
	/**
	 * Calculates a transformation matrix onto the ray.
	 * 
	 * @param matrix the {@link TransformMatrix2D} that gets calculated into this ray.
	 * @return the transformed ray.
	 */
	public Ray2D transform(TransformMatrix2D matrix) {
		Point2D startNew = matrix.multiply(start);
		Vector2D directionNew = startNew.vec(matrix.multiply(start.add(direction)));
		return new Ray2D(startNew,directionNew);
	}
}
