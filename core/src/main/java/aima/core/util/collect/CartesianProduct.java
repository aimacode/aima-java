package aima.core.util.collect;

import aima.core.util.math.MixedRadixInterval;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An {@link java.lang.Iterable} over an
 * <a href="http://en.wikipedia.org/wiki/Cartesian_product">n-fold Cartesian
 * product</a>, represented by a {@link java.util.List} of n dimensions, where
 * each element is an n-length {@link java.util.List}.
 *
 * @param <T>
 *            the type of elements in the Cartesian Product.
 *
 * @author Ciaran O'Reilly
 */
public class CartesianProduct<T> implements Iterable<T[]> {
	//
	private Class<T> elementBaseType;
	private int[] radices;
	private MixedRadixInterval mri;
	// For access efficiency we will access the elements from a
	// contiguous 2d array.
	private T[][] dimensions;

	@SuppressWarnings("unchecked")
	public CartesianProduct(Class<T> elementBaseType, List<List<T>> dimensions) {
		Objects.requireNonNull(elementBaseType, "elementBaseType");
		Objects.requireNonNull(dimensions, "dimensions");
		if (dimensions.size() == 0) {
			throw new IllegalArgumentException("No dimensions specified");
		}

		this.elementBaseType = elementBaseType;
		// Copy the dimension information into own arrays
		// to ensure they cannot be mutated. This ensures
		// we keep the immutable property.
		this.radices = new int[dimensions.size()];
		for (int i = 0; i < dimensions.size(); i++) {
			this.radices[i] = dimensions.get(i).size();
		}
		this.mri = new MixedRadixInterval(this.radices);
		// NOTE: the sizes of the 2nd dimension can vary so we will only set the first dimension
		// size first.
		this.dimensions = (T[][]) Array.newInstance(elementBaseType, radices.length, 0);
		AtomicInteger currentDimension = new AtomicInteger(-1);
		dimensions.forEach(dimension -> {
			int dimensionIdx = currentDimension.incrementAndGet();
			if (dimension.size() == 0) {
				throw new IllegalArgumentException("Dimension " + dimensionIdx + " has no elements in it.");
			}
			// Up front, ensure all the dimension's elements are of the required
			// base type.
			List<T> typeSafeDimension = Collections.checkedList(new ArrayList<>(), elementBaseType);
			typeSafeDimension.addAll(dimension);
			// Assign the correctly sized second dimension.
			this.dimensions[dimensionIdx] = (T[]) Array.newInstance(elementBaseType, typeSafeDimension.size());
			for (int i = 0; i < typeSafeDimension.size(); i++) {
				this.dimensions[dimensionIdx][i] = typeSafeDimension.get(i);
			}
		});
	}

	/**
	 * Cartesian Power constructor (i.e dimension^n).
	 *
	 * @param elementBaseType
	 *            the base type of the elements in the list (to permit
	 *            construction of a type safe array).
	 * @param dimension
	 *            the dimension to be raised to a given power.
	 * @param n
	 *            the power to raise the dimension by.
	 */
	public CartesianProduct(Class<T> elementBaseType, List<T> dimension, int n) {
		this(elementBaseType, nDimensions(dimension, n));
	}

	public BigInteger size() {
		return mri.size();
	}

	//
	// START-Iterable
	public Iterator<T[]> iterator() {
		return Spliterators.iterator(spliterator());
	}

	@Override
	public Spliterator<T[]> spliterator() {
		return new CartesianProductSpliterator(mri.spliterator());
	}
	// END-Iterable
	//

	public Stream<T[]> stream() {
		return StreamSupport.stream(spliterator(), false);
	}

	public Stream<T[]> parallelStream() {
		return StreamSupport.stream(spliterator(), true);
	}

	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner("x", "(", ")");
		IntStream.of(radices).forEachOrdered(i -> sj.add("" + i));
		return sj.toString();
	}

	//
	// PRIVATE
	//
	private class CartesianProductSpliterator implements Spliterator<T[]> {
		private Spliterator<int[]> mixedRadixIntervalSplor;
		private T[] currentValues;

		@SuppressWarnings("unchecked")
		public CartesianProductSpliterator(Spliterator<int[]> mixedRadixIntervalSplor) {
			this.mixedRadixIntervalSplor = mixedRadixIntervalSplor;
			this.currentValues = (T[]) Array.newInstance(elementBaseType, radices.length);
		}

		//
		// START-Spliterator
		@Override
		public boolean tryAdvance(Consumer<? super T[]> action) {
			if (mixedRadixIntervalSplor.tryAdvance(this::setCurrentValues)) {
				action.accept(currentValues);
				return true;
			}
			return false;
		}

		@Override
		public Spliterator<T[]> trySplit() {
			Spliterator<T[]> result = null;
			Spliterator<int[]> prefix = mixedRadixIntervalSplor.trySplit();
			if (prefix != null) {
				result = new CartesianProductSpliterator(prefix);
			}
			return result;
		}

		@Override
		public long estimateSize() {
			return mixedRadixIntervalSplor.estimateSize();
		}

		// NOTE: don't use ordered as we are keeping set semantics
		@Override
		public int characteristics() {
			int characteristics = DISTINCT | NONNULL | IMMUTABLE;
			if (mixedRadixIntervalSplor.getExactSizeIfKnown() != -1) {
				characteristics = characteristics | Spliterator.SIZED | Spliterator.SUBSIZED;
			}
			return characteristics;
		}

		// END-Spliterator
		//
		private void setCurrentValues(int[] numeralValues) {
			for (int i = 0; i < numeralValues.length; i++) {
				currentValues[i] = dimensions[i][numeralValues[i]];
			}
		}
	}
	
	private static <T> List<List<T>> nDimensions(List<T> dimension, int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("n must be > 0");
		}
		List<List<T>> dimensions = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			dimensions.add(dimension);
		}		
		return dimensions;
	}
}
