package aima.extra.probability.factory;

import java.math.BigInteger;
import java.math.MathContext;
import aima.extra.probability.*;

/**
 * This is a factory class specifically for creating RationalProbabilityNumber
 * instances. This factory class provides certain additional construction
 * methods that are unique to the RationalProbabilityNumber class. If these
 * extra methods are not required, then the superclass factory is good enough.
 * 
 * @author Nagaraj Poti
 */
public class RationalProbabilityFactory extends ProbabilityFactory<RationalProbabilityNumber> {

	// Constructor

	/**
	 * Pass parameter to superclass.
	 */
	public RationalProbabilityFactory() {
		this(RationalProbabilityNumber.class);
	}

	/**
	 * Pass clazz parameter to superclass for initialization.
	 * 
	 * @param clazz
	 */
	private RationalProbabilityFactory(Class<RationalProbabilityNumber> clazz) {
		super(clazz);
	}

	// PUBLIC 
	
	/**
	 * Create ProbabilityNumber instance by providing numerator and denominator,
	 * both of BigInteger type.
	 * 
	 * @param numerator
	 *            of BigInteger type.
	 * @param denominator
	 *            of BigInteger type.
	 * 
	 * @return a new RationalProbabilityNumber instance.
	 */
	public RationalProbabilityNumber valueOf(BigInteger numerator, BigInteger denominator) {
		return new RationalProbabilityNumber(numerator, denominator);
	}

	/**
	 * Create ProbabilityNumber instance by providing numerator and denominator,
	 * both of BigInteger type, and a MathContext setting.
	 * 
	 * @param numerator
	 *            of BigInteger type.
	 * @param denominator
	 *            of BigInteger type.
	 * @param mc
	 *            MathContext to be associated with RationalProbabilityNumber
	 *            instance.
	 * 
	 * @return a new RationalProbabilityNumber instance.
	 */

	public RationalProbabilityNumber valueOf(BigInteger numerator, BigInteger denominator, MathContext mc) {
		return new RationalProbabilityNumber(numerator, denominator, mc);
	}

	/**
	 * Create ProbabilityNumber instance by providing numerator of BigInteger
	 * type alone.
	 * 
	 * @param numerator
	 *            of BigInteger type.
	 * 
	 * @return a new RationalProbabilityNumber instance.
	 */
	public RationalProbabilityNumber valueOf(BigInteger numerator) {
		return new RationalProbabilityNumber(numerator);
	}

	/**
	 * Create ProbabilityNumber instance by providing numerator and denominator,
	 * both of long type.
	 * 
	 * @param numerator
	 *            of long type.
	 * @param denominator
	 *            of long type.
	 * 
	 * @return a new RationalProbabilityNumber instance.
	 */
	public RationalProbabilityNumber valueOf(long numerator, long denominator) {
		return new RationalProbabilityNumber(numerator, denominator);
	}
}