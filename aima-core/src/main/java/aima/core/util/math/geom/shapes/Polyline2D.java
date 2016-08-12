package aima.core.util.math.geom.shapes;

import aima.core.util.Util;

/**
 * This class implements a polyline in a two-dimensional Cartesian plot.<br/>
 * The polyline is represented by a starting point represented by {@link Point2D} and all edges as {@link Vector2D}.<br/>
 * If the last side ends in the starting point the polyline is a closed polygon.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class Polyline2D implements IGeometric2D {
	
	private final Point2D[] vertexes;
	private final Vector2D[] edges;
	private final boolean isClosed;
	private final Rect2D boundingRect;
	
	/**
	 * @param vertexes the vertexes of the polyline or polygon.
	 * @param isClosed true if the sum of the edges is the zero vector.
	 */
	public Polyline2D(Point2D[] vertexes, boolean isClosed) {
		this.vertexes = vertexes;
		this.isClosed = isClosed;
		final int length = isClosed ? vertexes.length : vertexes.length - 1;
		this.edges = new Vector2D[length];
		Point2D previousPoint = vertexes[0];
		for(int i=1; i<vertexes.length; i++) {
			Point2D targetPoint = vertexes[i];
			edges[i-1] = previousPoint.vec(targetPoint);
			previousPoint = targetPoint;
		}
		if(isClosed) {
			edges[vertexes.length - 1] = previousPoint.vec(vertexes[0]);
		}
		//Calculate the bounding rectangle:
		double  minX = vertexes[0].getX(),
				minY = vertexes[0].getY(),
				maxX = vertexes[0].getX(),
				maxY = vertexes[0].getY();
		for(int i=1; i < vertexes.length; i++) {
			minX = minX > vertexes[i].getX() ? vertexes[i].getX() : minX;
			minY = minY > vertexes[i].getY() ? vertexes[i].getY() : minY;
			maxX = maxX < vertexes[i].getX() ? vertexes[i].getX() : maxX;
			maxY = maxY < vertexes[i].getY() ? vertexes[i].getY() : maxY;
		}
		boundingRect = new Rect2D(minX,minY,maxX,maxY);
	}
	
	/**
	 * @return the starting point of the polyline.
	 */
	public Point2D[] getVertexes() {
		return vertexes;
	}
	
	/**
	 * @return the edges of the polyline.
	 */
	public Vector2D[] getEdges() {
		return edges;
	}
	
	/**
	 * @return true if this polyline is a polygon.
	 */
	public boolean isClosed() {
		return isClosed;
	}
	
	@Override
	public Point2D randomPoint() {
		if(isClosed) {
			//Generate random points within the bounding rectangle...
			final double minX = boundingRect.getUpperLeft().getX();
			final double maxX = boundingRect.getLowerRight().getX();
			final double minY = boundingRect.getUpperLeft().getY();
			final double maxY = boundingRect.getLowerRight().getY();
			
			Point2D randPoint = new Point2D(Util.generateRandomDoubleBetween(minX, maxX),Util.generateRandomDoubleBetween(minY, maxY));
			
			//...until one is inside the polygon.
			while (!isInsideBorder(randPoint)){
				randPoint = new Point2D(Util.generateRandomDoubleBetween(minX, maxX),Util.generateRandomDoubleBetween(minY, maxY));
			}
			
			return randPoint;
		} else {
			final int index = Util.randomNumberBetween(0, vertexes.length-2);
			final Line2D line = new Line2D(vertexes[index],edges[index]);
			return line.randomPoint();
		}
	}
	
	@Override
	public boolean isInside(Point2D point) {
		if(!isClosed) return false;
		int intersections = 0;
		Ray2D pointRay = new Ray2D(point,Vector2D.X_VECTOR);
		for (int i=0; i<edges.length;i++) {
			if(vertexes[i].equals(point)) {
				return false;
			}
			final double result = new Line2D(vertexes[i],edges[i]).rayCast(pointRay);
			if(!Util.compareDoubles(result, Double.POSITIVE_INFINITY) && !Util.compareDoubles(result,0.0d)) {
				if(!Util.compareDoubles(edges[i].angleTo(Vector2D.X_VECTOR),0.0d)) intersections++;
			}
		}
		return intersections % 2 == 1;
	}
	
	@Override
	public boolean isInsideBorder(Point2D point) {
		int intersections = 0;
		Ray2D pointRay = new Ray2D(point,Vector2D.X_VECTOR);
		for (int i=0; i<edges.length;i++) {
			final Line2D line = new Line2D(vertexes[i],edges[i]);
			if(line.isInsideBorder(point)) return true;
			final double result = line.rayCast(pointRay);
			if(!Util.compareDoubles(result,Double.POSITIVE_INFINITY) && isClosed) {
				if(!Util.compareDoubles(edges[i].angleTo(Vector2D.X_VECTOR),0.0d)) intersections++;
			}
		}
		return intersections % 2 == 1;
	}

	@Override
	public double rayCast(Ray2D ray) {
		double result = Double.POSITIVE_INFINITY;
		for(int i=0; i < edges.length; i++) {
			if(!ray.getDirection().isParallel(edges[i])) {
				final double divisor = (ray.getDirection().getX()*edges[i].getX()-ray.getDirection().getX()*edges[i].getY());
				final double len1 = (vertexes[i].getY()*edges[i].getX()-ray.getStart().getY()*edges[i].getX()-vertexes[i].getX()*edges[i].getY()+ray.getStart().getX()*edges[i].getY())/divisor;
				if(len1 > 0) {
					final double len2 = (ray.getDirection().getY()*ray.getStart().getX() - ray.getDirection().getY()*vertexes[i].getX() - ray.getDirection().getX()*ray.getStart().getY() + ray.getDirection().getX()*vertexes[i].getY())/divisor;
					if(len2 >= 0 && len2 <= 1) result = result > len1 ? len1 : result;
				}
			}  else {
				final Vector2D startVec = ray.getStart().vec(vertexes[i]);
				if(ray.getDirection().isAbsoluteParallel(startVec)) {
					return startVec.length();
				} else {
					final Point2D endVertex = isClosed && i == edges.length - 1 ? vertexes[0] : vertexes[i+1];
					final Vector2D endVec = ray.getStart().vec(endVertex);
					if(ray.getDirection().isAbsoluteParallel(endVec)) {
						return endVec.length();
					}
				}
			}
		}
		return result * ray.getDirection().length();
	}

	@Override
	public Rect2D getBounds() {
		return boundingRect;
	}
	
	@Override
	public Polyline2D transform(TransformMatrix2D matrix) {
		Point2D[] vertexesNew = new Point2D[vertexes.length];
		for(int i=0;i<vertexes.length;i++) {
			vertexesNew[i] = matrix.multiply(vertexes[i]);
		}
		return new Polyline2D(vertexesNew,isClosed || (Util.compareDoubles(vertexesNew[0].getX(),vertexesNew[vertexes.length - 1].getX()) && Util.compareDoubles(vertexesNew[0].getY(), vertexesNew[vertexes.length - 1].getY())));
	}
}
