package aima.core.probability.proposed.model.domain;

import java.util.Set;

public interface FiniteDiscreteDomain extends Domain {
	/**
	 * 
	 * @return a consistent ordered Set (e.g. LinkedHashSet) of the possible
	 *         values this domain can take on.
	 */
	Set<? extends Object> getPossibleValues();
}
