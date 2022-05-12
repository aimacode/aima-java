package aima.core.util.datastructure;

import java.util.Objects;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 * 
 */
public class Pair<X, Y> {
	private final X a;

	private final Y b;

	/**
	 * Constructs a Pair from two given elements
	 * 
	 * @param a
	 *            the first element
	 * @param b
	 *            the second element
	 */
	public Pair(X a, Y b) {
		this.a = a;
		this.b = b;
	}

	/**
	 * Returns the first element of the pair
	 * 
	 * @return the first element of the pair
	 */
	public X getFirst() {
		return a;
	}

	/**
	 * Returns the second element of the pair
	 * 
	 * @return the second element of the pair
	 */
	public Y getSecond() {
		return b;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && getClass() == o.getClass()) {
			Pair<?, ?> p = (Pair<?, ?>) o;
			return Objects.equals(a, p.a) && Objects.equals(b, p.b);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (a == null ? 0 : 7 * a.hashCode()) + 31 * (b == null ? 0 : b.hashCode());
	}

	@Override
	public String toString() {
		return "< " + getFirst().toString() + " , " + getSecond().toString() + " > ";
	}
}
