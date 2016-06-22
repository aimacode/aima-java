package aima.core.search.api;

import java.util.function.Function;

/**
 * Description of a Search function that looks for a state.
 *
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the state space
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
@FunctionalInterface
public interface SearchForStateFunction<A, S> extends Function<Problem<A, S>, S> {
	@Override
	S apply(Problem<A, S> problem);
}
