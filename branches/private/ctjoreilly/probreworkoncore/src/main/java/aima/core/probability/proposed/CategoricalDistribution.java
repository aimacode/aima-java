package aima.core.probability.proposed;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 487.<br>
 * 
 * A probability distribution for discrete random variables with a finite set of
 * values.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/Probability_distribution"
 *      >Probability Distribution</a>
 * 
 * @author Ciaran O'Reilly
 */
public interface CategoricalDistribution extends ProbabilityMass {

	/**
	 * <b>Note:</b> Do not modify the double[] returned by this method directly.
	 * Instead use setValue() to ensure values and related values are updated
	 * correctly.
	 * 
	 * @return the double[] used to represent the CategoricalDistribution.
	 * 
	 * @see CategoricalDistribution#setValue(int, double)
	 */
	double[] getValues();

	/**
	 * Set the value at a specified index within the distribution.
	 * 
	 * @param idx
	 * @param value
	 */
	void setValue(int idx, double value);

	/**
	 * 
	 * @return the summation of all of the elements within the Distribution.
	 */
	double getSum();

	/**
	 * Normalize the values comprising this distribution.
	 * 
	 * @return the this instance with its values normalized.
	 */
	CategoricalDistribution normalize();

	/**
	 * Retrieve the index into the CategoricalDistribution for the provided set
	 * of values for the random variables comprising the Distribution.
	 * 
	 * @param values
	 *            an ordered set of values for the random variables comprising
	 *            the Distribution (<b>Node:</b> the order must match the order
	 *            of the random variables describing the distribution)
	 * @return the index within the Distribution for the values specified.
	 * 
	 * @see CategoricalDistribution#getValues()
	 */
	int getIndex(Object... values);

	/**
	 * Divide the dividend (this) CategoricalDistribution by the divisor to
	 * create a new CategoricalDistribution representing the quotient. The
	 * variables comprising the divisor distribution must be a subset of the
	 * dividend. However, ordering of variables does not matter as the quotient
	 * contains the same variables as the dividend and the internal logic
	 * handles iterating through the two distributions correctly, irrespective
	 * of the order of their variables.
	 * 
	 * @param divisor
	 * @return a new Distribution representing the quotient of the dividend
	 *         (this) divided by the divisor.
	 * @throws IllegalArgumentException
	 *             if the variables of the divisor distribution are not a subset
	 *             of the dividend.
	 */
	CategoricalDistribution divideBy(CategoricalDistribution divisor);

	/**
	 * Multiplication of this Distribution by a given multiplier, creating a new
	 * Distribution representing the product of the two. Note: Is identical to
	 * pointwise product calculation.<br>
	 * <br>
	 * see: AIMA3e Figure 14.10 page 527.<br>
	 * <br>
	 * Note: Default Distribution multiplication is not commutative. The reason
	 * is because the order of the variables comprising a Distribution dictate
	 * the ordering of the values for that distribution. For example (the
	 * General case of Baye's rule, AIMA3e pg. 496), using this API method:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(X | Y)<b>P</b>(Y))/<b>P</b>(X)<br>
	 * <br>
	 * is NOT true, due to multiplication of distributions not being
	 * commutative. However:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(Y)<b>P</b>(X | Y))/<b>P</b>(X)<br>
	 * <br>
	 * is true, using this API.<br>
	 * <br>
	 * The default order of the variable of the Distribution returned is the
	 * order of the variables as they are seen, as read from the left to right
	 * term, for e.g.: <br>
	 * <br>
	 * <b>P</b>(Y)<b>P</b>(X | Y)<br>
	 * <br>
	 * would give a Distribution of the following form: <br>
	 * Y, X<br>
	 * <br>
	 * i.e. an ordered union of the variables from the two distributions. <br>
	 * To override the default order of the product use pointwiseProductPOS().
	 * 
	 * @param multiplier
	 * 
	 * @return a new Distribution representing the product of this and the
	 *         passed in multiplier. The order of the variables comprising the
	 *         product distribution is the ordered union of the left term (this)
	 *         and the right term (multiplier).
	 * 
	 * @see CategoricalDistribution#multiplyByPOS(CategoricalDistribution,
	 *      RandomVariable...)
	 */
	CategoricalDistribution multiplyBy(CategoricalDistribution multiplier);

	/**
	 * Multiplication - Product Order Specified (POS). Note: Is identical to
	 * pointwise product calculation.<br>
	 * <br>
	 * <br>
	 * see: AIMA3e Figure 14.10 page 527.<br>
	 * <br>
	 * Multiplication of this Distribution by a given multiplier, creating a new
	 * Distribution representing the product of the two. The order of the
	 * variables comprising the product will match those specified. For example
	 * (the General case of Baye's rule, AIMA3e pg. 496), using this API method:<br>
	 * <br>
	 * <b>P</b>(Y | X) = (<b>P</b>(X | Y)<b>P</b>(Y), [Y, X])/<b>P</b>(X)<br>
	 * <br>
	 * is true when the correct product order is specified.
	 * 
	 * @param multiplier
	 * @param prodVarOrder
	 *            the order the variables comprising the product are to be in.
	 * 
	 * @return a new Distribution representing the product of this and the
	 *         passed in multiplier. The order of the variables comprising the
	 *         product distribution is the order specified.
	 * 
	 * @see Distribution#pointwiseProduct(Distribution)
	 */
	CategoricalDistribution multiplyByPOS(CategoricalDistribution multiplier,
			RandomVariable... prodVarOrder);
}
