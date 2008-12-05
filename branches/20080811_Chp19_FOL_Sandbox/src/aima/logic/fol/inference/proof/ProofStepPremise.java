package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepPremise extends AbstractProofStep {
	//
	private static final List<ProofStep> _noPredecessors = new ArrayList<ProofStep>();
	//
	private String proof = "";

	public ProofStepPremise(String proof) {
		this.proof = proof;
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(_noPredecessors);
	}

	public String getProof() {
		return proof.toString();
	}

	public String getJustification() {
		return "Premise";
	}
	// END-ProofStep
	//
}
