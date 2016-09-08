package aima.core.robotics.impl.map;

import aima.core.robotics.impl.datatypes.AbstractRangeReading;

/**
 * This interface defines a factory for the class extending {@link AbstractRangeReading} in the context of the {@link MclCartesianPlot2D}.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <R> a range reading extending {@link AbstractRangeReading}.
 */
public interface IRangeReadingFactory<R extends AbstractRangeReading> {
	/**
	 * Creates a new instance of {@code <R>} for the given parameters.
	 * @param value the value of the new range reading.
	 * @return the new range reading.
	 */
	R getRangeReading(double value);
}
