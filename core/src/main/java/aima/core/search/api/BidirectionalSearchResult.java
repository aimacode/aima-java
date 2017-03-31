package aima.core.search.api;

import java.util.List;

/**
 * @author manthan.
 */
public interface BidirectionalSearchResult<A> {
    /**
     * Path from initial node to meeting node.
     *
     * @return List of actions that from initial state to meeting of two frontiers.
     */
    List<A> fromInitialStateToMeeting();
    /**
     * Path from goal node to meeting node.
     *
     * @return List of actions that from goal state to meeting of two frontiers.
     */
    List<A> fromGoalStateToMeeting();
    /**
     * Path from initial node to goal node.
     *
     * @return List of actions that from initial state to goal state.
     */
    List<A> fromInitialStateToGoalState();
    /**
     * Path from goal node to initial node.
     *
     * @return List of actions that from goal state to initial state.
     */
    List<A> fromGoalStateToInitialState();
}
