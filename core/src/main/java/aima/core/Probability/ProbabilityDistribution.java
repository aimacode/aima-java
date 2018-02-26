package aima.core.Probability;

import aima.core.Probability.Proposition.AssignmentProposition;

import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * A probability distribution is a function that assigns probabilities to events
 * (sets of possible worlds).<br>
 * <br>
 *
 * @author Ciaran O'Reilly
 * @author Samagra Sharma
 * @see <a href="http://en.wikipedia.org/wiki/Probability_distribution"
 * >Probability Distribution</a>
 */

public interface ProbabilityDistribution {

    /**
     * @return a consistent ordered Set (e.g. LinkedHashSet)
     * of the random variables this probability distribution is for.
     */
    Set<RandomVariable> getFor();

    /**
     * @param rv
     * @return true if this Distribution is for the passed in Random Variable,
     * false otherwise.
     */
    boolean contains(RandomVariable rv);

    /**
     * @param eventValues the values for the random variables comprising the
     *                    Distribution
     * @return the value for the possible worlds associated with the assignments
     * for the random variables comprising the Distribution
     */
    double getValue(Object... eventValues);

    /**
     * Get the value for the provided set of AssignmentPropositions for the
     * random variables comprising the Distribution (size of each must equal and
     * their random variables must match).
     *
     * @param eventValues the assignment propositions for the random variables
     *                    comprising the Distribution
     * @return the value for the possible worlds associated with the assignments
     * for the random variables comprising the Distribution.
     */
    double getValue(AssignmentProposition... eventValues);
}
