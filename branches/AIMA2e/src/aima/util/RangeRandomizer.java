/*
 * Created on Aug 6, 2005
 *
 */
package aima.util;

/**
 * @author Ravi Mohan
 * 
 */

import aima.probability.JavaRandomizer;
import aima.probability.Randomizer;

public class RangeRandomizer implements Randomizer {

	private double lower;

	private double upper;

	private JavaRandomizer random;

	public RangeRandomizer(double lower, double upper) {
		this.lower = lower;
		this.upper = upper;
		this.random = new JavaRandomizer();
	}

	public double nextDouble() {
		double ran = random.nextDouble();
		double scaled_ran = ran * (upper - lower);
		return lower + scaled_ran;
	}

}
