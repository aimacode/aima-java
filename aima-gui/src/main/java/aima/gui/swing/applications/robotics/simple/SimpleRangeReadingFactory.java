package aima.gui.swing.applications.robotics.simple;

import aima.core.robotics.impl.datatypes.AbstractRangeReading;
import aima.core.robotics.impl.map.IRangeReadingFactory;

/**
 * Implements {@link IRangeReadingFactory} for the {@link SimpleRangeReading}.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 */
public final class SimpleRangeReadingFactory implements IRangeReadingFactory<AbstractRangeReading> {

	@Override
	public AbstractRangeReading getRangeReading(double value) {
		return new SimpleRangeReading(value);
	}
}
