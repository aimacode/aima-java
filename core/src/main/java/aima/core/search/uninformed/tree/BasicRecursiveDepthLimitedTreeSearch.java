package aima.core.search.uninformed.tree;

import aima.core.api.agent.Action;
import aima.core.api.search.uninformed.tree.RecursiveDepthLimitedTreeSearch;
import aima.core.search.BasicSearchFunction;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 */
public class BasicRecursiveDepthLimitedTreeSearch<S> extends BasicSearchFunction<S> implements RecursiveDepthLimitedTreeSearch<S> {

    private int limit;

    public BasicRecursiveDepthLimitedTreeSearch(int defaultLimit) {
        this.limit = defaultLimit;
    }

    @Override
    public int limit() {
        return limit;
    }

    @Override
    public List<Action> cutoff() {
        return null;
    }
}
