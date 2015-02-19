package aima.core.search.uninformed;

import aima.core.api.search.uninformed.DepthLimitedSearch;
import aima.core.api.search.uninformed.IterativeDeepeningSearch;

/**
 * @author Ciaran O'Reilly
 */
public class BasicIterativeDeepeningSearch<S> extends BasicSearchFunction<S> implements IterativeDeepeningSearch<S> {

    private DepthLimitedSearch<S> dls;

    public BasicIterativeDeepeningSearch() {
        dls = new BasicDepthLimitedSearch<>(0);
    }

    @Override
    public DepthLimitedSearch<S> dls() {
        return dls;
    }
}
