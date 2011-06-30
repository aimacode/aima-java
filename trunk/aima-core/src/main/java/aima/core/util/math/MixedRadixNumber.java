package aima.core.util.math;

import java.util.List;

/**
 * For details on <a
 * href="http://demonstrations.wolfram.com/MixedRadixNumberRepresentations/"
 * >Mixed Radix Number Representations.</a>
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class MixedRadixNumber extends Number {
	//
	private static final long serialVersionUID = 1L;
	//
	private long value = 0L;
	private long maxValue = 0L;
	private int[] radices = null;
	private int[] currentNumeralValue = null;
	private boolean recalculate = true;

	/**
	 * Constructs a mixed radix number with a specified value and a specified
	 * array of radices.
	 * 
	 * @param value
	 *            the value of the mixed radix number
	 * @param radices
	 *            the radices used to represent the value of the mixed radix
	 *            number
	 */
	public MixedRadixNumber(long value, int[] radices) {
		this.value = value;
		this.radices = new int[radices.length];
		System.arraycopy(radices, 0, this.radices, 0, radices.length);
		calculateMaxValue();
	}

	/**
	 * Constructs a mixed radix number with a specified value and a specified
	 * list of radices.
	 * 
	 * @param value
	 *            the value of the mixed radix number
	 * @param radices
	 *            the radices used to represent the value of the mixed radix
	 *            number
	 */
	public MixedRadixNumber(long value, List<Integer> radices) {
		this.value = value;
		this.radices = new int[radices.size()];
		for (int i = 0; i < radices.size(); i++) {
			this.radices[i] = radices.get(i);
		}
		calculateMaxValue();
	}

	/**
	 * Constructs a mixed radix number with a specified array of numerals and a
	 * specified array of radices.
	 * 
	 * @param radixValues
	 *            the numerals of the mixed radix number
	 * @param radices
	 *            the radices of the mixed radix number
	 */
	public MixedRadixNumber(int[] radixValues, int[] radices) {
		this(0, radices);
		setCurrentValueFor(radixValues);
	}

	/**
	 * Returns the value of the mixed radix number with the specified array of
	 * numerals and the current array of radices.
	 * 
	 * @return the value of the mixed radix number
	 * 
	 * @throws IllegalArgumentException
	 *             if any of the specified numerals is less than zero, or if any
	 *             of the specified numerals is greater than it's corresponding
	 *             radix.
	 */
	public long getCurrentValueFor(int[] radixValues) {
		if (radixValues.length != radices.length) {
			throw new IllegalArgumentException(
					"Radix values not same size as Radices.");
		}

		long cvalue = 0;
		long mvalue = 1;
		for (int i = 0; i < radixValues.length; i++) {
			if (radixValues[i] < 0 || radixValues[i] > radices[i]) {
				throw new IllegalArgumentException("Radix value " + i
						+ " is out of range for radix at this position");
			}
			if (i > 0) {
				mvalue *= radices[i - 1];
			}
			cvalue += mvalue * radixValues[i];
		}
		return cvalue;
	}

	/**
	 * Sets the value of the mixed radix number with the specified array of
	 * numerals and the current array of radices.
	 * 
	 * @param radixValues
	 *            the numerals of the mixed radix number
	 */
	public void setCurrentValueFor(int[] radixValues) {
		this.value = getCurrentValueFor(radixValues);
		System.arraycopy(radixValues, 0, this.currentNumeralValue, 0,
				radixValues.length);
		recalculate = false;
	}

	/**
	 * Returns the maximum value which can be represented by the current array
	 * of radices.
	 * 
	 * @return the maximum value which can be represented by the current array
	 *         of radices.
	 */
	public long getMaxAllowedValue() {
		return maxValue;
	}

	/**
	 * Increments the value of the mixed radix number, if the value is less than
	 * the maximum value which can be represented by the current array of
	 * radices.
	 * 
	 * @return <code>true</code> if the increment was successful.
	 */
	public boolean increment() {
		if (value < maxValue) {
			value++;
			recalculate = true;
			return true;
		}

		return false;
	}

	/**
	 * Decrements the value of the mixed radix number, if the value is greater
	 * than zero.
	 * 
	 * @return <code>true</code> if the decrement was successful.
	 */
	public boolean decrement() {
		if (value > 0) {
			value--;
			recalculate = true;
			return true;
		}
		return false;
	}

	/**
	 * Returns the numeral at the specified position.
	 * 
	 * @param atPosition
	 *            the position of the numeral to return
	 * @return the numeral at the specified position.
	 */
	public int getCurrentNumeralValue(int atPosition) {
		if (atPosition >= 0 && atPosition < radices.length) {
			if (recalculate) {
				long quotient = value;
				for (int i = 0; i < radices.length; i++) {
					if (0 != quotient) {
						currentNumeralValue[i] = (int) quotient % radices[i];
						quotient = quotient / radices[i];
					} else {
						currentNumeralValue[i] = 0;
					}

				}
				recalculate = false;
			}
			return currentNumeralValue[atPosition];
		}
		throw new IllegalArgumentException(
				"Argument atPosition must be >=0 and < " + radices.length);
	}

	//
	// START-Number
	@Override
	public int intValue() {
		return (int) longValue();
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return longValue();
	}

	@Override
	public double doubleValue() {
		return longValue();
	}

	// END-Number
	//

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < radices.length; i++) {
			sb.append("[");
			sb.append(this.getCurrentNumeralValue(i));
			sb.append("]");
		}

		return sb.toString();
	}

	//
	// PRIVATE
	//

	/**
	 * Sets the maximum value which can be represented by the current array of
	 * radices.
	 * 
	 * @throws IllegalArgumentException
	 *             if no radices are defined, if any radix is less than two, or
	 *             if the current value is greater than the maximum value which
	 *             can be represented by the current array of radices.
	 */
	private void calculateMaxValue() {
		if (0 == radices.length) {
			throw new IllegalArgumentException(
					"At least 1 radix must be defined.");
		}
		for (int i = 0; i < radices.length; i++) {
			if (radices[i] < 2) {
				throw new IllegalArgumentException(
						"Invalid radix, must be >= 2");
			}
		}

		// Calculate the maxValue allowed
		maxValue = radices[0];
		for (int i = 1; i < radices.length; i++) {
			maxValue *= radices[i];
		}
		maxValue -= 1;

		if (value > maxValue) {
			throw new IllegalArgumentException(
					"The value ["
							+ value
							+ "] cannot be represented with the radices provided, max value is "
							+ maxValue);
		}

		currentNumeralValue = new int[radices.length];
	}
}
