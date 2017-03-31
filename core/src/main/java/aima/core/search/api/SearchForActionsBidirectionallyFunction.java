package aima.core.search.api;


import java.util.function.BiFunction;

/**
 * @author manthan
 */
@FunctionalInterface
public interface SearchForActionsBidirectionallyFunction<A, S> extends BiFunction<Problem<A,S>,
    Problem<A, S>, BidirectionalSearchResult<A>> {
}
