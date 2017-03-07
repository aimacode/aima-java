package aima.core.search.framework;

import aima.core.search.framework.evalfunc.HeuristicFunction;

/**
 * Search algorithms which make use of heuristics to guide the search
 * are expected to implement this interface.
 * @author Ruediger Lunde
 */
public interface Informed {
    public void setHeuristicFunction(HeuristicFunction hf);
}
