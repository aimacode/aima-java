package aima.core.robotics.impl.map;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;

import aima.core.robotics.IMclMap;
import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.datatypes.IPose2D;
import aima.core.robotics.impl.datatypes.AbstractRangeReading;
import aima.core.util.math.geom.CartesianPlot2D;
import aima.core.util.math.geom.IGroupParser;
import aima.core.util.math.geom.shapes.IGeometric2D;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Ray2D;
import aima.core.util.math.geom.shapes.Rect2D;
import aima.core.util.math.geom.shapes.Vector2D;

/**
 * This class implements the interface {@link IMclMap} using the classes {@link Angle} and {@link AbstractRangeReading}.<br/>
 * It uses a parser that generates two sets of {@link IGeometric2D}.<br/>
 * The first set describes obstacles that can be measured by the range sensor. Thus only this group is considered for the {@code rayCast} function.<br/>
 * The second group specifies areas on the map. If a position is in one of these areas it is a valid position.<br/>
 * This functionality is implemented by {@code isPoseValid} which in addition tests whether the heading of that pose is valid and the position is inside an obstacle which makes it an invalid position.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <P> a pose implementing {@link IPose2D}.
 * @param <M> a movement (or sequence of movements) of the robot, implementing {@link IMclMove}.
 * @param <R> a range reading extending {@link AbstractRangeReading}.
 */
public final class MclCartesianPlot2D<P extends IPose2D<P,M>,M extends IMclMove<M>,R extends AbstractRangeReading> implements IMclMap<P,Angle,M,AbstractRangeReading> {

	/**
	 * This is the identifier that is used to find a group of obstacles in the map file.
	 */
	public static final String OBSTACLE_ID = "obstacles";
	/**
	 * This is the identifier that is used to find a group of areas in the map file.
	 */
	public static final String AREA_ID = "validMovementArea";
	
	private IPoseFactory<P,M> poseFactory;
	private IRangeReadingFactory<R> rangeReadingFactory;
	
	private CartesianPlot2D obstacles;
	private CartesianPlot2D areas;
	
	private Exception obstaclesException;
	private Exception areasException;
	
	/**
	 * @param obstaclesParser a map parser implementing {@link IGroupParser}. This parser is used to load a map file for the obstacles.
	 * @param areasParser a map parser implementing {@link IGroupParser}. This parser is used to load a map file for the areas. It should be a different object than obstaclesParser or implemented thread-safe.
	 * @param poseFactory a pose factory implementing {@link IPoseFactory}.
	 * @param rangeReadingFactory a range reading factory implementing {@link IRangeReadingFactory}.
	 */
	public MclCartesianPlot2D(IGroupParser obstaclesParser,IGroupParser areasParser, IPoseFactory<P,M> poseFactory, IRangeReadingFactory<R> rangeReadingFactory) {
		this.poseFactory = poseFactory;
		this.rangeReadingFactory = rangeReadingFactory;
		obstacles = new CartesianPlot2D(obstaclesParser);
		areas = new CartesianPlot2D(areasParser);
	}

	/**
	 * Sets the sensor range.
	 * @param sensorRange the maximum range that the sensor can reliably measure. This parameter is used to speed up {@code rayCast}.
	 */
	public void setSensorRange(double sensorRange) {
		obstacles.setRayRange(sensorRange);
		areas.setRayRange(sensorRange);
	}

	/**
	 * Calculate the maximum distance between all samples and compare it to {@code maxDistance}.
	 * If it is smaller or equals to {@code maxDistance} the mean is returned. {@code null} otherwise.
	 * @param samples the set of samples to be checked against.
	 * @param maxDistance the maxDistance that the cloud should have to return a mean.
	 * @return the mean of the samples or {@code null}.
	 */
	public P checkDistanceOfPoses(Set<P> samples, double maxDistance) {
		double maxDistanceSamples = 0.0d;
		for(P first: samples) {
			for(P second: samples) {
				double distance = first.distanceTo(second);
				maxDistanceSamples = distance > maxDistanceSamples ? distance : maxDistanceSamples;
			}
		}
		if(maxDistanceSamples <= maxDistance) {
			double averageX = 0.0d;
			double averageY = 0.0d;
			double averageHeading = 0.0d;
			for(P sample: samples) {
				averageX += sample.getX() / samples.size();
				averageY += sample.getY() / samples.size();
				averageHeading += sample.getHeading() / samples.size();
			}
			return poseFactory.getPose(new Point2D(averageX,averageY),averageHeading);
		}
		return null;
	}
	
	/**
	 * This function loads a map input stream into this Cartesian plot. The two streams have to be two different instances to be thread safe.
	 * @param obstacleInput the stream containing the obstacles.
	 * @param areaInput the stream containing the areas
	 * @throws Exception thrown by the implementing class of {@link IGroupParser} when calling {@code loadMap}.
	 */
	public void loadMap(final InputStream obstacleInput, final InputStream areaInput) throws Exception {
		obstaclesException = null;
		areasException = null;
		Thread obstaclesThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					obstacles.loadMap(obstacleInput, OBSTACLE_ID);
				} catch (Exception e) {
					obstaclesException = e;
				}
			}
		});
		Thread areasThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					areas.loadMap(areaInput, AREA_ID);
				} 
				catch (Exception e) {
					areasException = e;
				}
			}
		});
		obstaclesThread.setDaemon(true);
		areasThread.setDaemon(true);
		obstaclesThread.start();
		areasThread.start();
		try {
			obstaclesThread.join();
			areasThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(obstaclesException != null) throw obstaclesException;
		if(areasException != null) throw areasException;
	}
		
	/**
	 * Returns an iterator over the obstacle polygons.
	 * @return an iterator over the obstacle polygons.
	 */
	public Iterator<IGeometric2D> getObstacles() {
		return obstacles.getShapes();
	}
	
	/**
	 * Returns an iterator over the boundaries of the obstacle polygons.
	 * @return an iterator over the boundaries of the obstacle polygons.
	 */
	public Iterator<Rect2D> getObstacleBoundaries() {
		return obstacles.getBoundaries();
	}
	
	/**
	 * Returns an iterator over the area polygons.
	 * @return an iterator over the area polygons.
	 */
	public Iterator<IGeometric2D> getAreas() {
		return areas.getShapes();
	}
	
	/**
	 * Returns an iterator over the boundaries of the area polygons.
	 * @return an iterator over the boundaries of the area polygons.
	 */
	public Iterator<Rect2D> getAreaBoundaries() {
		return areas.getBoundaries();
	}
	
	/**
	 * Checks whether a map was loaded.
	 * @return {@code true} if a map was loaded.
	 */
	public boolean isLoaded() {
		return !areas.isEmpty();
	}

	@Override
	public P randomPose() {
		Point2D point;
		do { 
			point = areas.randomPoint();
		} while(obstacles.isPointInsideShape(point));
		return poseFactory.getPose(point);
	}

	@Override
	public R rayCast(P pose) {
		Ray2D ray = new Ray2D(new Point2D(pose.getX(), pose.getY()), Vector2D.calculateFromPolar(1, -pose.getHeading()));
		return rangeReadingFactory.getRangeReading(obstacles.rayCast(ray));
	}

	@Override
	public boolean isPoseValid(P pose) {
		if(!poseFactory.isHeadingValid(pose)) return false;
		Point2D point = new Point2D(pose.getX(),pose.getY());
		return areas.isPointInsideBorderShape(point) && !obstacles.isPointInsideShape(point);
	}
}
