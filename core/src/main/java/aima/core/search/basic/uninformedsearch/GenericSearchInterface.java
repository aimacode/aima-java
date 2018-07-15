package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;

public interface GenericSearchInterface<A,S> {
    Node<A,S> search(Problem<A,S> problem);
}
