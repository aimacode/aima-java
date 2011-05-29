package aima.core.probability.proposed.domain;

import java.util.Set;

public interface FiniteDomain extends Domain {
	/**
	 * 
	 * @return a consistent ordered Set (e.g. LinkedHashSet) of the possible
	 *         values this domain can take on.
	 */
	Set<? extends Object> getPossibleValues();
}
