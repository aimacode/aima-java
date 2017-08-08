package aima.extra.probability.bayes;

import java.util.List;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandomVariable;

/**
 * Each factor is a matrix indexed by its argument variables.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface Factor {

	/**
	 * @return a consistent ordered list of the argument variables for this
	 *         Factor.
	 */
	List<RandomVariable> getArgumentVariables();

	/**
	 * @param var
	 *            is the random variable that is to be checked.
	 * 
	 * @return true if this Factor contains the passed in random variable, false
	 *         otherwise.
	 */
	boolean contains(RandomVariable var);

	/**
	 * <b>Note:</b> An immutable list is returned by this method and thus its
	 * values cannot be changed.
	 * 
	 * @return the array of values used to represent the Factor.
	 */
	List<ProbabilityNumber> getValues();

	/**
	 * The task of extracting the distribution over some subset of variables or
	 * a single variable is called marginalization or summing out. The
	 * probabilities for each possible value of the remaining variables are
	 * summed, thereby taking them out of the equation. This methods sums out
	 * the provided variables from this Factor creating a new Factor of the
	 * remaining variables with their values updated with the summed out random
	 * variables.
	 * 
	 * @param vars
	 *            is the array of random variables to be summed out (need NOT be
	 *            in order).
	 * 
	 * @return a new Factor with updated probability values, excluding the
	 *         summed out random variables. The random variables retain their
	 *         original order.
	 */
	Factor sumOut(RandomVariable... vars);

	/**
	 * Pointwise multiplication of this Factor by a given multiplier, creating a
	 * new Factor representing the product of the two. The pointwise product of
	 * two factors f<sub>1</sub> and f<sub>2</sub> yields a new factor f whose
	 * variables are the union of the variables in f<sub>1</sub> and
	 * f<sub>2</sub> and whose elements are given by the product of the
	 * corresponding elements in the two factors. <br>
	 * <br>
	 * Note: Default Factor multiplication is not commutative. The reason is
	 * because the order of the variables comprising a Factor dictate the
	 * ordering of the values for that factor. The default order of the
	 * variables of the Factor returned is the order of the variables as they
	 * are seen, as read from the left to right term, for e.g.: <br>
	 * <br>
	 * f<sub>1</sub>(Y)f<sub>2</sub>(X, Y)<br>
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
	 * @see Factor#pointwiseProductPOS(Factor, List<RandomVariable>)
	 */
	Factor pointwiseProduct(Factor multiplier);

	/**
	 * Pointwise Multiplication - Product Order Specified (POS).<br>
	 * <br>
	 * Pointwise multiplication of this Factor by a given multiplier, creating a
	 * new Factor representing the product of the two. The order of the
	 * variables comprising the product will match those specified.
	 * 
	 * @param multiplier
	 * @param prodVarOrder
	 *            is the list of random variables comprising the product in a
	 *            given order.
	 * 
	 * @return a new Factor representing the pointwise product of this and the
	 *         passed in multiplier. The order of the variables comprising the
	 *         product distribution is the order specified.
	 * 
	 * @see Factor#pointwiseProduct(Factor)
	 */
	Factor pointwiseProductPOS(Factor multiplier, List<RandomVariable> prodVarOrder);
}