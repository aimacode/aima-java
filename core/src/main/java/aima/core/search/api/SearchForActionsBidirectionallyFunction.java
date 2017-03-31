package aima.core.search.api;

import aima.core.util.datastructure.Pair;

import java.util.List;
import java.util.function.Function;

/**
 * @author manthan
 */
@FunctionalInterface
public interface SearchForActionsBidirectionallyFunction<A, S> extends Function<Pair<Problem<A,
    S>, Problem<A, S>>, List<A>> {
}
