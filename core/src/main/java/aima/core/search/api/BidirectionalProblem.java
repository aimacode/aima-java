package aima.core.search.api;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

/**
 * An interface describing a problem that can be tackled from both directions at
 * once (i.e InitialState<->Goal).
 *
 * @author Ciaran O'Reilly
 *
 */
public interface BidirectionalProblem<A, S> extends Problem {
  Problem<A, S> getOriginalProblem();

  List<Problem<A, S>> getReverseProblems();


  @Override
  default Object initialState() {
    throw new NotImplementedException();
  }

  @Override
  default boolean isGoalState(Object state) {
    throw new NotImplementedException();
  }

  @Override
  default Object result(Object o, Object o2) {
    throw new NotImplementedException();
  }

  @Override
  default double stepCost(Object o, Object o2, Object sPrime) {
    throw new NotImplementedException();
  }

  @Override
  default List actions(Object o) {
    return null;
  }
}
