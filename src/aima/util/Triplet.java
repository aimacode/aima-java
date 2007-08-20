package aima.util;

/**
 * @author Ravi Mohan
 * 
 */

public class Triplet<X, Y, Z> {
	private final X x;

	private final Y y;

	private final Z z;

	public Triplet(X x, Y y, Z z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public X getFirst() {
		return x;
	}

	public Y getSecond() {
		return y;
	}

	public Z getThird() {
		return z;
	}

	public boolean equals(Object o) {
		Triplet<X, Y, Z> other = (Triplet<X, Y, Z>) o;
		return (x.equals(other.x)) && (y.equals(other.y))
				&& (z.equals(other.z));
	}

	public int hashCode() {
		return x.hashCode() + 31 * y.hashCode() + 31 * z.hashCode();
	}

	public String toString() {
		return "< " + x.toString() + " , " + y.toString() + " , "
				+ z.toString() + " >";
	}

}
