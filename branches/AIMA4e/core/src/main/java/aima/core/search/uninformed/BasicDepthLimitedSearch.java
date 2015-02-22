package aima.core.search.uninformed;

import aima.core.api.agent.Action;
import aima.core.api.search.uninformed.DepthLimitedSearch;
import aima.core.search.BasicSearchFunction;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 */
public class BasicDepthLimitedSearch<S> extends BasicSearchFunction<S> implements DepthLimitedSearch<S> {

    private int limit;

    public BasicDepthLimitedSearch(int defaultLimit) {
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
