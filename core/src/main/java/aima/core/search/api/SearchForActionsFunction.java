package aima.core.search.api;

import java.util.List;
import java.util.function.Function;

/**
 * Description of a Search function that looks for a sequence of actions from an
 * initial state to a specified goal state.
 *
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the state space
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
@FunctionalInterface
public interface SearchForActionsFunction<A, S> extends Function<Problem<A, S>, List<A>> {
	@Override
	List<A> apply(Problem<A, S> problem);
}
