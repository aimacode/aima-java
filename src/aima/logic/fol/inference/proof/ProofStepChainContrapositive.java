package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.kb.data.Chain;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepChainContrapositive extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Chain contrapositive = null;
	private Chain contrapositiveOf = null;

	public ProofStepChainContrapositive(Chain contrapositive,
			Chain contrapositiveOf) {
		this.contrapositive = contrapositive;
		this.contrapositiveOf = contrapositiveOf;
		this.predecessors.add(contrapositiveOf.getProofStep());
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return contrapositive.toString();
	}

	public String getJustification() {
		return "Contrapositive: "
				+ contrapositiveOf.getProofStep().getStepNumber();
	}
	// END-ProofStep
	//
}
