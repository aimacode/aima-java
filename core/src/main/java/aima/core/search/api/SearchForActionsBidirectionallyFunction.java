package aima.core.search.api;

import java.util.List;
import java.util.function.Function;

/**
 * @author manthan
 */
@FunctionalInterface
public interface SearchForActionsBidirectionallyFunction<A, S> extends Function<BidirectionalProblem<A, S>, List<A>> {
    @Override
    List<A> apply(BidirectionalProblem<A, S> problem);
}
