package aima.logic.fol.inference;

import aima.logic.fol.inference.proof.Proof;
import aima.logic.fol.inference.proof.ProofPrinter;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class InferenceResultPrinter {
	/**
	 * Utility method for outputting InferenceResults in a formatted textual
	 * representation.
	 * 
	 * @param an
	 *            InferenceResult
	 * @return a String representation of the InferenceResult.
	 */
	public static String printInferenceResult(InferenceResult ir) {
		StringBuilder sb = new StringBuilder();

		sb.append("InferenceResult.isTrue=" + ir.isTrue());
		sb.append("InferenceResult.isPossiblyFalse=" + ir.isPossiblyFalse());
		sb.append("InferenceResult.isUnknownDueToTimeout="
				+ ir.isUnknownDueToTimeout());
		sb.append("InferenceResult.isPartialResultDueToTimeout="
				+ ir.isPartialResultDueToTimeout());
		sb.append("InferenceResult.#Proofs=" + ir.getProofs().size());
		int proofNo = 0;
		for (Proof p : ir.getProofs()) {
			proofNo++;
			sb.append("InferenceResult.Proof#"+proofNo+"=\n" + ProofPrinter.printProof(p));
		}

		return sb.toString();
	}
}
