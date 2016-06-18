package aima.core.search.framework;

/**
 * Interface for all search algorithms which store at least a part of the
 * exploration history as search tree and return a list of actions which lead
 * from the initial state to a goal state.
 * 
 * @author Ruediger Lunde
 *
 */
public interface SearchForActions extends Search {
	NodeExpander getNodeExpander();
}
