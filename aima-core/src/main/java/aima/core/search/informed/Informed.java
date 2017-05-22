package aima.core.search.informed;

import java.util.function.Function;

/**
 * Search algorithms which make use of heuristics to guide the search
 * are expected to implement this interface.
 * @author Ruediger Lunde
 */
public interface Informed {
    void setHeuristicFunction(Function<Object, Double> hf);
}
