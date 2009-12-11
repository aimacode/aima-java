package aima.core.search.framework;

/**
 * An interface describing a problem that can be tackled from both directions 
 * at once (i.e InitialState<->Goal).
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface BidirectionalProblem {
	Problem getOriginalProblem();

	Problem getReverseProblem();
}
