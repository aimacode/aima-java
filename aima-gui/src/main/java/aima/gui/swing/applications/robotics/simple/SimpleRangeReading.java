package aima.gui.swing.applications.robotics.simple;

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
	private static double BAD_DELTA;
	private static double SENSOR_RANGE;
	/**
	 * Sets the noise model for all range readings.
	 * @param value the noise of the model.
	 */
	public static void setRangeNoise(double value) {
		RANGE_NOISE = value;
	}
	
	/**
	 * Sets the delta between two range readings, above which a weight of zero is calculated.
	 * @param badDelta the worst acceptable delta.
	 */
	public static void setBadDelta(double badDelta) {
		BAD_DELTA = badDelta;
	}
	
	/**
	 * Sets the sensor range that the virtual robot can measure at most.
	 * @param sensorRange the range of the robot's sensor.
	 */
	public static void setSensorRange(double sensorRange) {
		SENSOR_RANGE = sensorRange;
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
	
	/**
	 * Generates noise onto the range reading.
	 * @return a new range reading with noise applied.
	 */
	public AbstractRangeReading addRangeNoise() {
		if(Double.isInfinite(getValue())) return this;
		final double adaptedRangeReading = Util.generateRandomDoubleBetween(getValue() - RANGE_NOISE, getValue() + RANGE_NOISE);
		return new SimpleRangeReading(adaptedRangeReading,getAngle());
	}

	@Override
	public double calculateWeight(AbstractRangeReading secondRange) {
		final AbstractRangeReading firstRange = addRangeNoise();
		if(firstRange.getValue() < 0 || secondRange.getValue() < 0) return 0;
		if(Double.isInfinite(firstRange.getValue()) && Double.isInfinite(secondRange.getValue())) return 1;
		final double firstRangeValue;
		if(Double.isInfinite(firstRange.getValue()) || firstRange.getValue() > SENSOR_RANGE) firstRangeValue = SENSOR_RANGE;
		else firstRangeValue = firstRange.getValue();
		final double secondRangeValue;
		if(Double.isInfinite(secondRange.getValue()) || secondRange.getValue() > SENSOR_RANGE) secondRangeValue = SENSOR_RANGE;
		else secondRangeValue = secondRange.getValue();
		final double delta = Math.abs(firstRangeValue - secondRangeValue);
		if(delta > BAD_DELTA) return 0.0f;
		return (float) (1.0d - delta / BAD_DELTA);
	}
}
