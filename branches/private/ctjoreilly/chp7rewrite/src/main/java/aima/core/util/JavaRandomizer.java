package aima.core.util;

import java.util.Random;

/**
 * Implementation of the Randomizer Interface using Java's java.util.Random
 * class.
 * 
 * @author Ravi Mohan
 * 
 */
public class JavaRandomizer implements Randomizer {
	private static Random _r = new Random();
	private Random r = null;

	public JavaRandomizer() {
		this(_r);
	}

	public JavaRandomizer(Random r) {
		this.r = r;
	}

	//
	// START-Randomizer
	@Override
	public double nextDouble() {
		return r.nextDouble();
	}

	// END-Randomizer
	//
}
