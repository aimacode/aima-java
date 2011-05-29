package aima.core.util.math;

/**
 * Basic supports for Intervals, see:
 * 
 * http://en.wikipedia.org/wiki/Interval_%28mathematics%29
 * 
 * for quick overview of notation.
 * 
 * @author Ciaran O'Reilly
 */
public class Interval<C> {
	private Comparable<C> lower = null;
	private boolean lowerInclusive = true;
	private Comparable<C> upper = null;
	private boolean upperInclusive = true;

	public Interval() {

	}

	public Interval(Comparable<C> lower, Comparable<C> upper) {
		setLower(lower);
		setUpper(upper);
	}

	public Interval(Comparable<C> lower, boolean lowerInclusive,
			Comparable<C> upper, boolean upperInclusive) {
		setLower(lower);
		setLowerInclusive(lowerInclusive);
		setUpper(upper);
		setUpperInclusive(upperInclusive);
	}

	public boolean isInInterval(C o) {
		if (null == lower || null == upper) {
			return false;
		}

		boolean in = true;

		if (isLowerInclusive()) {
			in = lower.compareTo(o) <= 0;
		} else {
			in = lower.compareTo(o) < 0;
		}

		if (in) {
			if (isUpperInclusive()) {
				in = upper.compareTo(o) >= 0;
			} else {
				in = upper.compareTo(o) > 0;
			}
		}

		return in;
	}

	public boolean isLowerInclusive() {
		return lowerInclusive;
	}

	public boolean isLowerExclusive() {
		return !lowerInclusive;
	}

	public void setLowerInclusive(boolean inclusive) {
		this.lowerInclusive = inclusive;
	}

	public void setLowerExclusive(boolean exclusive) {
		this.lowerInclusive = !exclusive;
	}

	public Comparable<C> getLower() {
		return lower;
	}

	public void setLower(Comparable<C> lower) {
		this.lower = lower;
	}

	public boolean isUpperInclusive() {
		return upperInclusive;
	}

	public boolean isUpperExclusive() {
		return !upperInclusive;
	}

	public void setUpperInclusive(boolean inclusive) {
		this.upperInclusive = inclusive;
	}

	public void setUpperExclusive(boolean exclusive) {
		this.upperInclusive = !exclusive;
	}

	public Comparable<C> getUpper() {
		return upper;
	}

	public void setUpper(Comparable<C> upper) {
		this.upper = upper;
	}

	public String toString() {
		return (lowerInclusive ? "[" : "(") + lower + ", " + upper
				+ (upperInclusive ? "]" : ")");
	}
}
