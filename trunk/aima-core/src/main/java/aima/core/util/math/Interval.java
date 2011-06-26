package aima.core.util.math;

/**
 * Basic supports for Intervals.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Interval_%28mathematics%29"
 *      >Interval</a>
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public class Interval<C> {
	private Comparable<C> lower = null;
	private boolean lowerInclusive = true;
	private Comparable<C> upper = null;
	private boolean upperInclusive = true;

	public Interval() {

	}

	/**
	 * Constructs a closed interval from the two specified end points.
	 * 
	 * @param lower
	 *            the lower end point of the interval
	 * @param upper
	 *            the upper end point of the interval
	 */
	public Interval(Comparable<C> lower, Comparable<C> upper) {
		setLower(lower);
		setUpper(upper);
	}

	/**
	 * Constructs an interval from the two specified end points.
	 * 
	 * @param lower
	 *            the lower end point of the interval
	 * @param lowerInclusive
	 *            wether or not the lower end of the interval is inclusive of
	 *            its value.
	 * @param upper
	 *            the upper end point of the interval
	 * @param upperInclusive
	 *            whether or not the upper end of the interval is inclusive of
	 *            its value.
	 */
	public Interval(Comparable<C> lower, boolean lowerInclusive,
			Comparable<C> upper, boolean upperInclusive) {
		setLower(lower);
		setLowerInclusive(lowerInclusive);
		setUpper(upper);
		setUpperInclusive(upperInclusive);
	}

	/**
	 * Returns <code>true</code> if the specified object is between the end
	 * points of this interval.
	 * 
	 * @return <code>true</code> if the specified value is between the end
	 *         points of this interval.
	 */
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

	/**
	 * Returns <code>true</code> if this interval is lower inclusive.
	 * 
	 * @return <code>true</code> if this interval is lower inclusive.
	 */
	public boolean isLowerInclusive() {
		return lowerInclusive;
	}

	/**
	 * Returns <code>true</code> if this interval is not lower inclusive.
	 * 
	 * @return <code>true</code> if this interval is not lower inclusive.
	 */
	public boolean isLowerExclusive() {
		return !lowerInclusive;
	}

	/**
	 * Sets the interval to lower inclusive or lower exclusive.
	 * 
	 * @param inclusive
	 *            <code>true</code> represents lower inclusive and
	 *            <code>false</code> represents lower exclusive.
	 */
	public void setLowerInclusive(boolean inclusive) {
		this.lowerInclusive = inclusive;
	}

	/**
	 * Sets the interval to lower exclusive or lower inclusive.
	 * 
	 * @param exclusive
	 *            <code>true</code> represents lower exclusive and
	 *            <code>false</code> represents lower inclusive.
	 */
	public void setLowerExclusive(boolean exclusive) {
		this.lowerInclusive = !exclusive;
	}

	/**
	 * Returns the lower end point of the interval.
	 * 
	 * @return the lower end point of the interval.
	 */
	public Comparable<C> getLower() {
		return lower;
	}

	/**
	 * Sets the lower end point of the interval.
	 * 
	 * @param lower
	 *            the lower end point of the interval
	 */
	public void setLower(Comparable<C> lower) {
		this.lower = lower;
	}

	/**
	 * Returns <code>true</code> if this interval is upper inclusive.
	 * 
	 * @return <code>true</code> if this interval is upper inclusive.
	 */
	public boolean isUpperInclusive() {
		return upperInclusive;
	}

	/**
	 * Returns <code>true</code> if this interval is upper exclusive.
	 * 
	 * @return <code>true</code> if this interval is upper exclusive.
	 */
	public boolean isUpperExclusive() {
		return !upperInclusive;
	}

	/**
	 * Sets the interval to upper inclusive or upper exclusive.
	 * 
	 * @param inclusive
	 *            <code>true</code> represents upper inclusive and
	 *            <code>false</code> represents upper exclusive.
	 */
	public void setUpperInclusive(boolean inclusive) {
		this.upperInclusive = inclusive;
	}

	/**
	 * Sets the interval to upper exclusive or upper inclusive.
	 * 
	 * @param exclusive
	 *            <code>true</code> represents upper exclusive and
	 *            <code>false</code> represents upper inclusive.
	 */
	public void setUpperExclusive(boolean exclusive) {
		this.upperInclusive = !exclusive;
	}

	/**
	 * Returns the upper end point of the interval.
	 * 
	 * @return the upper end point of the interval.
	 */
	public Comparable<C> getUpper() {
		return upper;
	}

	/**
	 * Sets the upper end point of the interval.
	 * 
	 * @param upper
	 *            the upper end point of the interval.
	 */
	public void setUpper(Comparable<C> upper) {
		this.upper = upper;
	}

	public String toString() {
		return (lowerInclusive ? "[" : "(") + lower + ", " + upper
				+ (upperInclusive ? "]" : ")");
	}
}
