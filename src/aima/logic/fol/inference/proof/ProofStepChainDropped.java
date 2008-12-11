package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.kb.data.Chain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepChainDropped extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Chain dropped = null;
	private Chain droppedOff = null;

	public ProofStepChainDropped(Chain dropped, Chain droppedOff) {
		this.dropped = dropped;
		this.droppedOff = droppedOff;
		this.predecessors.add(droppedOff.getProofStep());
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return dropped.toString();
	}

	public String getJustification() {
		return "Dropped: " + droppedOff.getProofStep().getStepNumber();
	}
	// END-ProofStep
	//
}
