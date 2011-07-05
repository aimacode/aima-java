package aima.core.util;

/**
 * @author Ravi Mohan
 * 
 */
public interface Randomizer {

	/**
	 * 
	 * @return a double value, chosen (approximately) uniformly from the range
	 *         [0.0d, 1.0d), i.e. 0.0d (inclusive) to 1.0d (exclusive).
	 */
	public double nextDouble();
}
