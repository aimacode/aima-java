package aima.core.util.math;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A <a href="http://en.wikipedia.org/wiki/Mixed_radix">mixed radix</a>
 * <a href="http://en.wikipedia.org/wiki/Interval_%28mathematics%29">interval
 * </a> where the end points are inclusive.
 *
 * @author Ciaran O'Reilly
 */
public class MixedRadixInterval implements Iterable<int[]> {
	private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
	private static final BigInteger TWO = BigInteger.valueOf(2);
	//
	private int[] radices;
	private int[] start;
	private int[] end;
	private BigInteger[] cachedRadixValues = null;
	private BigInteger leftEndPoint = BigInteger.ZERO;
	private BigInteger rightEndPoint = BigInteger.ZERO;
	private BigInteger maxPossibleValue = BigInteger.ZERO;
	private BigInteger size = BigInteger.ONE;

	public MixedRadixInterval(int[] radices) {
		this(radices, first(radices), last(radices));
	}

	public MixedRadixInterval(int[] radices, int[] leftEndPointNumeralValues, int[] rightEndPointNumeralValues) {
		this.radices = new int[radices.length];
		System.arraycopy(radices, 0, this.radices, 0, radices.length);

		this.start = new int[leftEndPointNumeralValues.length];
		System.arraycopy(leftEndPointNumeralValues, 0, this.start, 0, leftEndPointNumeralValues.length);

		this.end = new int[rightEndPointNumeralValues.length];
		System.arraycopy(rightEndPointNumeralValues, 0, this.end, 0, rightEndPointNumeralValues.length);

		initialize();
	}

	public static int[] first(int[] radices) {
		return new int[radices.length];
	}

	public static int[] last(int[] radices) {
		int[] last = new int[radices.length];
		for (int i = 0; i < radices.length; i++) {
			last[i] = radices[i] - 1;
		}
		return last;
	}

	public BigInteger getLeftEndPointValue() {
		return leftEndPoint;
	}

	public BigInteger getRightEndPointValue() {
		return rightEndPoint;
	}

	public BigInteger size() {
		return size;
	}

	public BigInteger getMinPossibleValue() {
		return BigInteger.ZERO;
	}

	public BigInteger getMaxPossibleValue() {
		return maxPossibleValue;
	}

	/**
	 * Returns the value of the mixed radix interval given the specified array
	 * of numerals.
	 *
	 * @return the value of the numerals at within the interval.
	 *
	 * @throws IllegalArgumentException
	 *             if any of the specified numerals is less than zero, or if any
	 *             of the specified numerals is greater than or equal to it's
	 *             corresponding radix.
	 */
	public BigInteger getValueFor(int[] numeralValues) {
		if (numeralValues.length != radices.length) {
			throw new IllegalArgumentException("Radix values not same length as Radices.");
		}

		BigInteger cvalue = BigInteger.ZERO;
		BigInteger mvalue = BigInteger.ONE;
		for (int i = numeralValues.length - 1; i >= 0; i--) {
			if (numeralValues[i] < 0 || numeralValues[i] >= radices[i]) {
				throw new IllegalArgumentException(
						"Radix value " + i + " is out of range for radix at this position, which is " + radices[i]);
			}
			if (i != numeralValues.length - 1) {
				mvalue = mvalue.multiply(cachedRadixValues[radices[i + 1]]);
			}
			cvalue = cvalue.add(mvalue.multiply(cachedRadixValues[numeralValues[i]]));
		}
		return cvalue;
	}

	public int[] getNumeralsFor(BigInteger value) {
		if (getMinPossibleValue().compareTo(value) > 0 || getMaxPossibleValue().compareTo(value) < 0) {
			throw new IllegalArgumentException("The value of " + value
					+ " is outside of the possible values that can be represented by this interval's radix values ["
					+ getMinPossibleValue() + ", " + getMaxPossibleValue() + "]");
		}

		int[] numerals = new int[radices.length];
		BigInteger quotient = value;
		for (int i = radices.length - 1; i >= 0; i--) {
			if (!quotient.equals(BigInteger.ZERO)) {
				numerals[i] = quotient.mod(cachedRadixValues[radices[i]]).intValue();
				quotient = quotient.divide(cachedRadixValues[radices[i]]);
			} else {
				// We are done, as the remaining numerals already have there
				// values defaulted to 0 on construction.
				break;
			}
		}
		return numerals;
	}

	//
	// START-Iterable
	@Override
	public Iterator<int[]> iterator() {
		return Spliterators.iterator(spliterator());
	}

	@Override
	public Spliterator<int[]> spliterator() {
		return new MixedRadixIntervalSpliterator();
	}
	// END-Iterable
	//

	public Stream<int[]> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	public Stream<int[]> parallelStream() {
		return StreamSupport.stream(spliterator(), true);
	}

	@Override
	public String toString() {
		return "[" + getLeftEndPointValue() + ", " + getRightEndPointValue() + "]";
	}

	//
	// PRIVATE
	//
	private void initialize() {
		if (radices.length != start.length || start.length != end.length) {
			throw new IllegalArgumentException("Lengths of array arguments must all be the same.");
		}
		if (0 == radices.length) {
			throw new IllegalArgumentException("At least 1 radix must be defined.");
		}
		for (int i = 0; i < radices.length; i++) {
			if (radices[i] < 1) {
				throw new IllegalArgumentException("Invalid radix, must be >= 1");
			}
			if (start[i] < 0 || start[i] >= radices[i]) {
				throw new IllegalArgumentException(
						"Start numeral value as position " + i + " must be >=0 and < " + radices.length);
			}
			if (end[i] < 0 || end[i] >= radices[i]) {
				throw new IllegalArgumentException(
						"End numeral value as position " + i + " must be >=0 and < " + radices.length);
			}
		}

		// Cache the possible radix values
		// so we don't have to create BigIntegers
		// for the same set of values multiple times
		int maxRadixValue = IntStream.of(radices).max().getAsInt();
		cachedRadixValues = new BigInteger[maxRadixValue + 1];
		for (int i = 0; i <= maxRadixValue; i++) {
			cachedRadixValues[i] = BigInteger.valueOf(i);
		}

		// Now get the min, max, and size information
		this.leftEndPoint = getValueFor(start);
		this.rightEndPoint = getValueFor(end);
		this.maxPossibleValue = getValueFor(last(radices));
		this.size = BigInteger.ONE.add(rightEndPoint.subtract(leftEndPoint));
		if (size.signum() <= 0) {
			throw new IllegalArgumentException("The start numerals have a value greater than the end numeral values");
		}
	}

	private class MixedRadixIntervalSpliterator implements Spliterator<int[]> {
		private static final int baseCharacteristics = DISTINCT | NONNULL | IMMUTABLE | ORDERED;
		private int characteristics;
		private int[] current;
		private BigInteger estSize;

		private MixedRadixIntervalSpliterator() {
			estSize = size;
			updateCharacteristics();
		}

		// START-Spliterator
		@Override
		public boolean tryAdvance(Consumer<? super int[]> action) {
			// If first time in
			if (current == null) {
				// Now set the current value to the start of the interval
				this.current = new int[start.length];
				System.arraycopy(start, 0, this.current, 0, start.length);
			} else {
				// If you can advance, i.e. not at the end
				if (!Arrays.equals(current, end)) {
					for (int i = radices.length - 1; i >= 0; i--) {
						if (current[i] < radices[i] - 1) {
							current[i] = current[i] + 1;
							for (int j = i + 1; j < radices.length; j++) {
								current[j] = 0;
							}
							break;
						}
					}
				} else {
					return false;
				}
			}

			action.accept(current);

			return true;
		}

		@Override
		public Spliterator<int[]> trySplit() {
			Spliterator<int[]> result = null; // no split by default
			// We'll split if we have more than 1 item remaining
			BigInteger currentValue = current == null ? getValueFor(start) : BigInteger.ONE.add(getValueFor(current));
			BigInteger remainingSize = BigInteger.ONE.add(getRightEndPointValue().subtract(currentValue));
			if (BigInteger.ONE.compareTo(remainingSize) < 0) {
				if (current == null) {
					current = start;
				}
				BigInteger splitSize = remainingSize.divide(TWO);
				BigInteger splitPos = currentValue.add(splitSize).subtract(BigInteger.ONE);

				// Get the prefix interval
				MixedRadixInterval prefix = new MixedRadixInterval(radices, getNumeralsFor(currentValue),
						getNumeralsFor(splitPos));
				// Move this iterator to the end of the prefix, will advance
				// past it when tryAdvance is called
				// and will never actually use the same value as the prefix does
				// for its endpoint.
				current = getNumeralsFor(splitPos);
				// Update the estimated size and characteristics of the current
				// spliterator
				this.estSize = rightEndPoint.subtract(getValueFor(current));
				updateCharacteristics();
				// And return a spliterator from the prefix as the result
				result = prefix.spliterator();
			}
			return result;
		}

		@Override
		public long estimateSize() {
			long result = Long.MAX_VALUE;
			if (hasCharacteristics(SIZED)) {
				result = estSize.longValueExact();
			}
			return result;
		}

		// NOTE: don't use ordered as we are keeping set semantics
		@Override
		public int characteristics() {
			return characteristics;
		}
		// END-Spliterator
		//

		//
		// PRIVATE
		//
		private void updateCharacteristics() {
			characteristics = baseCharacteristics;
			if (MAX_LONG.compareTo(estSize) > 0) {
				// Can support the APIs reporting of size (i.e. restricted to
				// long)
				// so can also report sized and subsized
				characteristics = characteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
			}
		}
	}
}
