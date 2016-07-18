package aima.core.search.api;

import java.util.List;

/**
 * A domain consists of a set of allowable values {v1, ... , vk} for a variable.
 * 
 * NOTE: We restrict ourselves to discrete/finite domains.

 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface Domain {
	/**
	 * 
	 * @return the set of allowed values in the domain.
	 */
	List<Object> getValues();

	/**
	 * Remove a value from the set of allowable values from the domain.
	 * 
	 * @param value
	 *            the value to be removed.
	 * @return true if the value was removed (i.e. was an allowable value),
	 *         false otherwise.
	 */
	boolean delete(Object value);

	/**
	 * 
	 * @return the size of the domain.
	 */
	default int size() {
		return getValues().size();
	}
}