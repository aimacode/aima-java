package aima.core.search.uninformed.tree;

import aima.core.api.search.uninformed.tree.RecursiveDepthLimitedTreeSearch;
import aima.core.search.BasicSearchFunction;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 */
public class BasicRecursiveDepthLimitedTreeSearch<A, S> extends BasicSearchFunction<A, S> implements RecursiveDepthLimitedTreeSearch<A, S> {

    private int limit;

    public BasicRecursiveDepthLimitedTreeSearch(int defaultLimit) {
        this.limit = defaultLimit;
    }

    @Override
    public int limit() {
        return limit;
    }

    @Override
    public List<A> cutoff() {
        return null;
    }
}
