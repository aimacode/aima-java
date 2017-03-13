package aima.core.search.api;

import java.util.List;

/**
 * we need a defined goal states for bidirectional problems
 * @author wormi
 */
public interface DefinedGoalStatesProblem<A, S> extends Problem<A, S> {
  List<S> goalStates();
}
