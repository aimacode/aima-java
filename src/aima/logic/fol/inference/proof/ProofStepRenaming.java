package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepRenaming extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();	
	private String proof = "";

	public ProofStepRenaming(String proof, ProofStep predecessor) {
		this.proof = proof;
		this.predecessors.add(predecessor);
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return proof.toString();
	}

	public String getJustification() {
		return "Renaming of " + predecessors.get(0).getStepNumber();
	}
	// END-ProofStep
	//
}
