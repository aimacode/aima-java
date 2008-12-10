package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.kb.data.Chain;
import aima.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepChainFromClause extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Chain chain = null;
	private Clause fromClause = null;

	public ProofStepChainFromClause(Chain chain, Clause fromClause) {
		this.chain = chain;
		this.fromClause = fromClause;
		this.predecessors.add(fromClause.getProofStep());
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return chain.toString();
	}

	public String getJustification() {
		return "Chain from Clause: "
				+ fromClause.getProofStep().getStepNumber();
	}
	// END-ProofStep
	//
}
