package aima.gui.swing.applications.robotics.simple;

import aima.core.robotics.impl.map.IPoseFactory;
import aima.core.util.Util;
import aima.core.util.math.geom.shapes.Point2D;

/**
 * Implements {@link IPoseFactory} for the {@link SimplePose}.<br/>
 * As a specific limitation for the heading does not exist, that check always returns {@code true}.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class SimplePoseFactory implements IPoseFactory<SimplePose,SimpleMove> {

	@Override
	public SimplePose getPose(Point2D point) {
		return new SimplePose(point,Util.generateRandomDoubleBetween(0, 2*Math.PI));
	}
	
	@Override
	public SimplePose getPose(Point2D point, double heading) {
		return new SimplePose(point,heading);
	}

	@Override
	public boolean isHeadingValid(SimplePose pose) {
		return true;
	}
	
}
