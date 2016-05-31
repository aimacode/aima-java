package aima.core.search.api;

import java.util.List;
import java.util.function.Function;

/**
 * Description of a Search Function.
 *
 * @param <A> the type of the actions that can be performed.
 * @param <S> the type of the state space
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
@FunctionalInterface
public interface Search <A, S> extends Function<Problem<A, S>, List<A>> {
	@Override
	List<A> apply(Problem<A, S> problem);
}
