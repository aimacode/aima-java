package aima.core.util;

/**
 * Mock implementation of the Randomizer interface so that the set of Random
 * numbers returned are in fact predefined.
 * 
 * @author Ravi Mohan
 * 
 */
public class MockRandomizer implements Randomizer {

	private double[] values;
	private int index;

	/**
	 * 
	 * @param values
	 *            the set of predetermined random values to loop over.
	 */
	public MockRandomizer(double[] values) {
		this.values = new double[values.length];
		System.arraycopy(values, 0, this.values, 0, values.length);
		this.index = 0;
	}

	//
	// START-Randomizer
	@Override
	public double nextDouble() {
		if (index == values.length) {
			index = 0;
		}

		return values[index++];
	}
	// END-Randomizer
	//
}