package aima.core.probability.proposed.domain;

import java.util.Set;

/**
 * A Domain over a countable/discrete and finite set of objects.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface FiniteDomain extends DiscreteDomain {
	/**
	 * 
	 * @return a consistent ordered Set (e.g. LinkedHashSet) of the possible
	 *         values this domain can take on.
	 */
	Set<? extends Object> getPossibleValues();
}
