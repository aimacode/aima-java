package aima.core.search.informed;

import aima.core.search.framework.evalfunc.HeuristicFunction;

/**
 * Search algorithms which make use of heuristics to guide the search
 * are expected to implement this interface.
 * @author Ruediger Lunde
 */
public interface Informed {
    void setHeuristicFunction(HeuristicFunction hf);
}
