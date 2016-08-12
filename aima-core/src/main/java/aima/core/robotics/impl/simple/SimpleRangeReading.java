package aima.core.robotics.impl.simple;

import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.datatypes.AbstractRangeReading;
import aima.core.util.Util;

/**
 * A simple range reading that extends {@link AbstractRangeReading} by adding the range noise model.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class SimpleRangeReading extends AbstractRangeReading {

	private static double RANGE_NOISE;
	
	/**
	 * Sets the noise model for all range readings.
	 * @param value the noise of the model.
	 */
	public static void setRangeNoise(double value) {
		RANGE_NOISE = value;
	}
	
	/**
	 * Constructor for a range reading that has a zero angle.
	 * @param value the range reading.
	 */
	public SimpleRangeReading(double value) {
		super(value);
	}
	
	/**
	 * Constructor for a range reading at a given angle.
	 * @param value the range reading.
	 * @param angle the angle of the range reading.
	 */
	public SimpleRangeReading(double value, Angle angle) {
		super(value, angle);
	}
	
	@Override
	public AbstractRangeReading addRangeNoise() {
		if(Double.isInfinite(getValue())) return this;
		final double adaptedRangeReading = Util.generateRandomDoubleBetween(getValue() - RANGE_NOISE, getValue() + RANGE_NOISE);
		return new SimpleRangeReading(adaptedRangeReading,getAngle());
	}
	
}
