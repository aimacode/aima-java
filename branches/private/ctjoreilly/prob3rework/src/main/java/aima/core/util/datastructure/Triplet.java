package aima.core.util.datastructure;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
public class Triplet<X, Y, Z> {
	private final X x;

	private final Y y;

	private final Z z;

	/**
	 * Constructs a triplet with three specified elements.
	 * 
	 * @param x
	 *            the first element of the triplet.
	 * @param y
	 *            the second element of the triplet.
	 * @param z
	 *            the third element of the triplet.
	 */
	public Triplet(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Returns the first element of the triplet.
	 * 
	 * @return the first element of the triplet.
	 */
	public X getFirst() {
		return x;
	}

	/**
	 * Returns the second element of the triplet.
	 * 
	 * @return the second element of the triplet.
	 */
	public Y getSecond() {
		return y;
	}

	/**
	 * Returns the third element of the triplet.
	 * 
	 * @return the third element of the triplet.
	 */
	public Z getThird() {
		return z;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Triplet<?, ?, ?>) {
			Triplet<?, ?, ?> other = (Triplet<?, ?, ?>) o;
			return (x.equals(other.x)) && (y.equals(other.y))
					&& (z.equals(other.z));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return x.hashCode() + 31 * y.hashCode() + 31 * z.hashCode();
	}

	@Override
	public String toString() {
		return "< " + x.toString() + " , " + y.toString() + " , "
				+ z.toString() + " >";
	}
}
