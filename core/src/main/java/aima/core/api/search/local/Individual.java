package aima.core.api.search.local;

import java.util.Collections;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * A state in a genetic algorithm is represented as an individual from the
 * population.
 *
 * @author Ciaran O'Reilly
 *
 * @param <A>
 *            the type of the alphabet used in the representation of the
 *            individuals in the population (this is to provide flexibility in
 *            terms of how a problem can be encoded).
 */
public interface Individual<A> {

    List representation = null;

    /**
     *
     * @return the individual's representation.
     */
    default List<A> representation() {
        return representation;
    }

    /**
     *
     * @return the length of the individual's representation.
     */
    default int length() {
        return representation.size();
    }
}
