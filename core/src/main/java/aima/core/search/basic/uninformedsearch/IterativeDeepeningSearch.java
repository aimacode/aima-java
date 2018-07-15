package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;

public class IterativeDeepeningSearch<A,S> implements GenericSearchInterface<A,S>{
    DepthLimitedSearch<A,S> depthLimitedSearch = new DepthLimitedSearch<>();

    @Override
    public Node<A, S> search(Problem<A, S> problem) {
        for(int depth =0;depth>=0;depth++){
            Node<A,S> result = depthLimitedSearch.search(problem,depth);
            if (result!=null)
                return result;
        }
        return null;
    }
}
