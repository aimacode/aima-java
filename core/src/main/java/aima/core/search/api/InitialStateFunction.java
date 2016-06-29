package aima.core.search.api;

/**
 * A function for returning the initial state that an agent starts in.
 * 
 * @param <S>
 *            the type of the initial state that the agent starts in.
 * 
 * @author Ciaran O'Reilly
 */
@FunctionalInterface
public interface InitialStateFunction<S> {
	/**
	 * @return the initial state that the agent starts in.
	 */
	S initialState();
}
