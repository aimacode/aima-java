package aima.core.search.api;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @author manthan
 */
@FunctionalInterface
public interface SearchForActionsBidirectionallyFunction<A, S> extends BiFunction<Problem<A, S>, Problem<A, S>, BidirectionalActions<A>> {
    @Override
    BidirectionalActions<A> apply(Problem<A, S> originalProblem, Problem<A, S> reverseProblem);
}
