package aima.core.search.api;

/**
 * An interface describing a problem that can be tackled from both directions at
 * once (i.e InitialState<->Goal).
 *
 * @author Ciaran O'Reilly
 *
 */
public interface BidirectionalProblem<A, S> {
  Problem<A, S> getOriginalProblem();

  Problem<A, S> getReverseProblem();
}
