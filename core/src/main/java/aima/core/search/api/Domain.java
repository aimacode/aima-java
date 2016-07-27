package aima.core.search.api;

import java.util.ArrayList;
import java.util.List;

/**
 * A domain consists of a set of allowable values {v1, ... , vk} for a variable.
 * 
 * NOTE: We restrict ourselves to discrete/finite domains.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface Domain {
	interface Listener {
		/**
		 * Called on registered listeners when a value is deleted from the
		 * domain.
		 * 
		 * @param domain
		 *            the domain the value was deleted from.
		 * @param value
		 *            the value deleted from the domain.
		 */
		default void deleted(Domain domain, Object value) {
			// Do nothing - implement if interested in.
		}

		/**
		 * Called on registered listeners when a value is restored to the
		 * domain.
		 * 
		 * @param domain
		 *            the domain the value was restored to.
		 * @param value
		 *            the value restored to the domain.
		 */
		default void restored(Domain domain, Object value) {
			// Do nothing - implement if interested in.
		}
	}

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
	
	default boolean reduceDomainTo(Object value) {
		boolean reducedTo = false;
		
		List<Object> valuesToDelete = new ArrayList<>(getValues());
		if (reducedTo = valuesToDelete.remove(value)) {
			valuesToDelete.forEach(val -> delete(val));
		}
		return reducedTo;
	}

	/**
	 * Restore a value to the domain.
	 * 
	 * @param value
	 *            the value to be restored to the domain.
	 * @return true if the value was restored, false if it already exists in the
	 *         domain.
	 * @throws IllegalArgumentException
	 *             if the value passed was not an original member of the domain.
	 */
	boolean restore(Object value);

	/**
	 * 
	 * @return the size of the domain.
	 */
	default int size() {
		return getValues().size();
	}

	List<Domain.Listener> domainListeners();

	default void addDomainListener(Domain.Listener listener) {
		domainListeners().add(listener);
	}

	default void removeDomainListener(Domain.Listener listener) {
		domainListeners().remove(listener);
	}

	default void notifyDeleted(Object value) {
		domainListeners().forEach(dl -> dl.deleted(this, value));
	}

	default void notifyRestored(Object value) {
		domainListeners().forEach(dl -> dl.restored(this, value));
	}
}