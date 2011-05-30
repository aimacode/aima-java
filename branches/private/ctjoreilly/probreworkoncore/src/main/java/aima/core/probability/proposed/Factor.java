package aima.core.probability.proposed;

import java.util.Set;

public interface Factor {

	/**
	 * @return a consistent ordered Set (e.g. LinkedHashSet) of the random
	 *         variables represented by this Factor.
	 */
	Set<RandomVariable> getFor();

	/**
	 * 
	 * @param rv
	 *            the Random Variable to be checked.
	 * @return true if this Factor contains the passed in Random Variable, false
	 *         otherwise.
	 */
	boolean contains(RandomVariable rv);

	/**
	 * <b>Note:</b> Do not modify the double[] returned by this method directly
	 * as it is intended to be read only.
	 * 
	 * @return the double[] used to represent the Factor.
	 */
	double[] getValues();

	/**
	 * Sum out the provided variables from this Factor creating a new Factor of
	 * the remaining variables with their values updated with the summed out
	 * random variables.<br>
	 * <br>
	 * see: AIMA3e page 527.<br>
	 * <br>
	 * 
	 * @param vars
	 *            the random variables to sum out.
	 * @return a new Factor containing any remaining random variables not summed
	 *         out and a new set of values updated with the summed out values.
	 */
	Factor sumOut(RandomVariable... vars);

	/**
	 * Pointwise multiplication of this Factor by a given multiplier, creating a
	 * new Factor representing the product of the two.<br>
	 * <br>
	 * see: AIMA3e Figure 14.10 page 527.<br>
	 * <br>
	 * Note: Default Factor multiplication is not commutative. The reason is
	 * because the order of the variables comprising a Factor dictate the
	 * ordering of the values for that factor. The default order of the
	 * variables of the Factor returned is the order of the variables as they
	 * are seen, as read from the left to right term, for e.g.: <br>
	 * <br>
	 * <b>f1</b>(Y)<b>f2</b>(X, Y)<br>
	 * <br>
	 * would give a Factor of the following form: <br>
	 * Y, X<br>
	 * <br>
	 * i.e. an ordered union of the variables from the two factors. <br>
	 * To override the default order of the product use pointwiseProductPOS().
	 * 
	 * @param multiplier
	 * 
	 * @return a new Factor representing the pointwise product of this and the
	 *         passed in multiplier. The order of the variables comprising the
	 *         product factor is the ordered union of the left term (this) and
	 *         the right term (multiplier).
	 * 
	 * @see Factor#pointwiseProductPOS(Factor, RandomVariable...)
	 */
	Factor pointwiseProduct(Factor multiplier);

	/**
	 * Pointwise Multiplication - Product Order Specified (POS).<br>
	 * <br>
	 * see: AIMA3e Figure 14.10 page 527.<br>
	 * <br>
	 * Pointwise multiplication of this Factor by a given multiplier, creating a
	 * new Factor representing the product of the two. The order of the
	 * variables comprising the product will match those specified.
	 * 
	 * @param multiplier
	 * @param prodVarOrder
	 *            the order the variables comprising the product are to be in.
	 * 
	 * @return a new Factor representing the pointwise product of this and the
	 *         passed in multiplier. The order of the variables comprising the
	 *         product distribution is the order specified.
	 * 
	 * @see Factor#pointwiseProduct(Factor)
	 */
	Factor pointwiseProductPOS(Factor multiplier,
			RandomVariable... prodVarOrder);
}
