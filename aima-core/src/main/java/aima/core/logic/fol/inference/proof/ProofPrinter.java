package aima.core.logic.fol.inference.proof;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofPrinter {

	/**
	 * Utility method for outputting proofs in a formatted textual
	 * representation.
	 * 
	 * @param proof
	 * @return a String representation of the Proof.
	 */
	public static String printProof(Proof proof) {
		StringBuilder sb = new StringBuilder();

		sb.append("Proof, Answer Bindings: ");
		sb.append(proof.getAnswerBindings());
		sb.append("\n");

		List<ProofStep> steps = proof.getSteps();

		int maxStepWidth = "Step".length();
		int maxProofWidth = "Proof".length();
		int maxJustificationWidth = "Justification".length();

		// Calculate the maximum width for each column in the proof
		for (ProofStep step : steps) {
			String sn = "" + step.getStepNumber();
			if (sn.length() > maxStepWidth) {
				maxStepWidth = sn.length();
			}
			if (step.getProof().length() > maxProofWidth) {
				maxProofWidth = step.getProof().length();
			}
			if (step.getJustification().length() > maxJustificationWidth) {
				maxJustificationWidth = step.getJustification().length();
			}
		}

		// Give a little extra padding
		maxStepWidth += 1;
		maxProofWidth += 1;
		maxJustificationWidth += 1;

		String f = "|%-" + maxStepWidth + "s| %-" + maxProofWidth + "s|%-"
				+ maxJustificationWidth + "s|\n";

		int barWidth = 5 + maxStepWidth + maxProofWidth + maxJustificationWidth;
		StringBuilder bar = new StringBuilder();
		for (int i = 0; i < barWidth; i++) {
			bar.append("-");
		}
		bar.append("\n");

		sb.append(bar);
		sb.append(String.format(f, "Step", "Proof", "Justification"));
		sb.append(bar);
		for (ProofStep step : steps) {
			sb.append(String.format(f, "" + step.getStepNumber(),
					step.getProof(), step.getJustification()));
		}
		sb.append(bar);

		return sb.toString();
	}
}
