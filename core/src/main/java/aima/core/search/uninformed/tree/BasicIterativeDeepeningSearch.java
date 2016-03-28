package aima.core.search.uninformed.tree;

import aima.core.api.search.uninformed.tree.RecursiveDepthLimitedTreeSearch;
import aima.core.api.search.uninformed.tree.IterativeDeepeningSearch;
import aima.core.search.BasicSearchFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicIterativeDeepeningSearch<A, S> extends BasicSearchFunction<A, S> implements IterativeDeepeningSearch<A, S> {

    private RecursiveDepthLimitedTreeSearch<A, S> dls;

    public BasicIterativeDeepeningSearch() {
        dls = new BasicRecursiveDepthLimitedTreeSearch<>(0);
    }

    @Override
    public RecursiveDepthLimitedTreeSearch<A, S> dls() {
        return dls;
    }
}
