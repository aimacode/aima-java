 package aima.core.util.math.geom.shapes;

import aima.core.util.Util;

/**
 * This class implements a line in a two-dimensional Cartesian plot.<br/>
 * Every line consists of a starting point and an ending point represented by a {@link Point2D}.<br/>
 * In addition the line between these two points can be represented as a {@link Vector2D}. 
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class Line2D implements IGeometric2D {
	
	private final Point2D start;
	private final Vector2D line;
	private final Point2D end;
	
	private final boolean zeroX;
	private final boolean zeroY;
	
	/**
	 * @param start the starting point of the line.
	 * @param line the vector representing the line.
	 */
	public Line2D(Point2D start, Vector2D line) {
		this.start = start;
		this.line = line;
		this.end = start.add(line);
		
		this.zeroX = Util.compareDoubles(line.getX(),0.0d);
		this.zeroY = Util.compareDoubles(line.getY(),0.0d);
	}
	
	/**
	 * @param start the starting point of the line.
	 * @param end the ending point of the line.
	 */
	public Line2D(Point2D start, Point2D end) {
		this.start = start;
		this.line = start.vec(end);
		this.end = end;
		
		this.zeroX = Util.compareDoubles(line.getX(),0.0d);
		this.zeroY = Util.compareDoubles(line.getY(),0.0d);
	}
	
	/**
	 * @param startX the X coordinate of the starting point of the line.
	 * @param startY the Y coordinate of the starting point of the line.
	 * @param endX the X coordinate of the ending point of the line.
	 * @param endY the Y coordinate of the ending point of the line.
	 */
	public Line2D(double startX, double startY, double endX, double endY) {
		this.start = new Point2D(startX, startY);
		this.line = new Vector2D(endX - startX, endY - startY);
		this.end = new Point2D(endX,endY);
		
		this.zeroX = Util.compareDoubles(line.getX(),0.0d);
		this.zeroY = Util.compareDoubles(line.getY(),0.0d);
	}

	/**
	 * @return the starting point of the line.
	 */
	public Point2D getStart() {
		return start;
	}

	/**
	 * @return the vector representing the line.
	 */
	public Vector2D getDirection() {
		return line;
	}

	/**
	 * @return the ending point of the line.
	 */
	public Point2D getEnd() {
		return end;
	}
	
	@Override
	public Point2D randomPoint() {
		if(zeroX && zeroY) {
			return start.clone();
		} else if(zeroX) {
			return new Point2D(start.getX(),Util.generateRandomDoubleBetween(start.getY(),end.getY()));
		} else if(zeroY) {
			return new Point2D(Util.generateRandomDoubleBetween(start.getX(),end.getX()),start.getY());
		} else {
			final double x = Util.generateRandomDoubleBetween(start.getX(),end.getX());
			final double y = ((x-start.getX())/line.getX())*line.getY()+start.getY();
			return new Point2D(x,y);
		}
	}
	
	@Override
	public boolean isInside(Point2D point) {
		return false;
	}

	@Override
	public boolean isInsideBorder(Point2D point) {
		if(zeroX && zeroY) {
			return start.equals(point);
		} else if(zeroX) {
			final double len = (point.getY() - start.getY()) / line.getY();
			return len <= 1 && len >= 0 && Util.compareDoubles(start.getX(),point.getX());
		} else if(zeroY) {
			final double len = (point.getX() - start.getX()) / line.getX();
			return len <= 1 && len >= 0 && Util.compareDoubles(start.getY(),point.getY());
		} else {
			final double len1 = (point.getX() - start.getX()) / line.getX();
			final double len2 = (point.getY() - start.getY()) / line.getY();
			return len1 <= 1 && len1 >= 0 && Util.compareDoubles(len1,len2);
		}
	}

	@Override
	public double rayCast(Ray2D ray) {
		if(!ray.getDirection().isParallel(line)) {
			final double divisor = (ray.getDirection().getY()*line.getX()-ray.getDirection().getX()*line.getY());
			if(Util.compareDoubles(divisor,0.0d)) return Double.POSITIVE_INFINITY;
			final double len1 = (start.getY()*line.getX()-ray.getStart().getY()*line.getX()-start.getX()*line.getY()+ray.getStart().getX()*line.getY())/divisor;
			if(len1 > 0) {
				final double len2 = (ray.getDirection().getY()*ray.getStart().getX() - ray.getDirection().getY()*start.getX() - ray.getDirection().getX()*ray.getStart().getY() + ray.getDirection().getX()*start.getY())/divisor;
				if(len2 >= 0 && len2 <= 1) return len1*ray.getDirection().length();
			}
		} else {
			final Vector2D startVec = ray.getStart().vec(start);
			if(ray.getDirection().isAbsoluteParallel(startVec)) {
				return startVec.length();
			} else {
				final Vector2D endVec = ray.getStart().vec(end);
				if(ray.getDirection().isAbsoluteParallel(endVec)) {
					return endVec.length();
				}
			}
		}
		return Double.POSITIVE_INFINITY;
	}

	@Override
	public Rect2D getBounds() {
		return new Rect2D(start.getX(),start.getY(),end.getX(),end.getY());
	}
	
	@Override
	public Line2D transform(TransformMatrix2D matrix) {
		final Point2D   startNew = matrix.multiply(start),
						endNew = matrix.multiply(end);
		return new Line2D(startNew,endNew);
	}
}
