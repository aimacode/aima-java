package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepRenaming extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Object proof = "";

	public ProofStepRenaming(Object proof, ProofStep predecessor) {
		this.proof = proof;
		this.predecessors.add(predecessor);
	}

	//
	// START-ProofStep
	@Override
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return proof.toString();
	}

	@Override
	public String getJustification() {
		return "Renaming of " + predecessors.get(0).getStepNumber();
	}
	// END-ProofStep
	//
}
