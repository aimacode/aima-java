package aima.logic.fol.inference;

import java.util.List;

import aima.logic.fol.inference.proof.Proof;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface InferenceResult {
	/**
	 * 
	 * @return true, if the query did not find a proof but found that the
	 *         background knowledge says nothing about the query.
	 */
	boolean isFalse();

	/**
	 * 
	 * @return true, if the query obtained at least 1 answer (can get partial
	 *         results if the original query contains variables indicating that
	 *         there can be more than 1 true answer).
	 */
	boolean isTrue();

	/**
	 * 
	 * @return true, if the inference procedure ran for a length of time and
	 *         found no answers one way or the other before it timed out.
	 */
	boolean isUnknownDueToTimeout();

	/**
	 * 
	 * @return true, if the inference procedure found a true proof for a query
	 *         containing variables (i.e. possibly more than 1 answer can be
	 *         returned) and the inference procedure was still looking for other
	 *         possible answers before it timed out.
	 */
	boolean isPartialResultDueToTimeout();

	/**
	 * 
	 * @return a list of 1 or more proofs (multiple proofs can be returned if
	 *         the original query contains variables) if the inference procedure
	 *         did not return isUnknown()=true.
	 */
	List<Proof> getProofs();
}
