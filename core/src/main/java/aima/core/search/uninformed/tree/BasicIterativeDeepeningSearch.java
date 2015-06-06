package aima.core.search.uninformed.tree;

import aima.core.api.search.uninformed.tree.RecursiveDepthLimitedTreeSearch;
import aima.core.api.search.uninformed.tree.IterativeDeepeningSearch;
import aima.core.search.BasicSearchFunction;

/**
 * @author Ciaran O'Reilly
 */
public class BasicIterativeDeepeningSearch<S> extends BasicSearchFunction<S> implements IterativeDeepeningSearch<S> {

    private RecursiveDepthLimitedTreeSearch<S> dls;

    public BasicIterativeDeepeningSearch() {
        dls = new BasicRecursiveDepthLimitedTreeSearch<>(0);
    }

    @Override
    public RecursiveDepthLimitedTreeSearch<S> dls() {
        return dls;
    }
}
