package aima.core.probability;

import java.util.Random;

/**
 * @author Ravi Mohan
 * 
 */
public class JavaRandomizer implements Randomizer {
	static Random r = new Random();

	public double nextDouble() {
		return r.nextDouble();
	}
}
