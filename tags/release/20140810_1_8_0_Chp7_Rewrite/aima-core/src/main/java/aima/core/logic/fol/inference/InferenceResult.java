package aima.core.logic.fol.inference;

import java.util.List;

import aima.core.logic.fol.inference.proof.Proof;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface InferenceResult {
	/**
	 * 
	 * @return true, if the query is not entailed from the premises. This just
	 *         means the query is not entailed, the query itself may be true.
	 */
	boolean isPossiblyFalse();

	/**
	 * 
	 * @return true, if the query is entailed from the premises (Note: can get
	 *         partial results if the original query contains variables
	 *         indicating that there can possibly be more than 1 proof/bindings
	 *         for the query, see: isPartialResultDueToTimeout()).
	 */
	boolean isTrue();

	/**
	 * 
	 * @return true, if the inference procedure ran for a length of time and
	 *         found no proof one way or the other before it timed out.
	 */
	boolean isUnknownDueToTimeout();

	/**
	 * 
	 * @return true, if the inference procedure found a proof for a query
	 *         containing variables (i.e. possibly more than 1 proof can be
	 *         returned) and the inference procedure was still looking for other
	 *         possible answers before it timed out.
	 */
	boolean isPartialResultDueToTimeout();

	/**
	 * 
	 * @return a list of 0 or more proofs (multiple proofs can be returned if
	 *         the original query contains variables).
	 */
	List<Proof> getProofs();
}
