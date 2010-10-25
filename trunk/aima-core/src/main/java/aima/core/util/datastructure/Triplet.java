package aima.core.util.datastructure;

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
