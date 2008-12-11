package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import aima.logic.fol.kb.data.Chain;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepChainCancellation extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Chain cancellation = null;
	private Chain cancellationOf = null;
	private Map<Variable, Term> subst = null;

	public ProofStepChainCancellation(Chain cancellation, Chain cancellationOf,
			Map<Variable, Term> subst) {
		this.cancellation = cancellation;
		this.cancellationOf = cancellationOf;
		this.subst = subst;
		this.predecessors.add(cancellationOf.getProofStep());
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return cancellation.toString();
	}

	public String getJustification() {
		return "Cancellation: " + cancellationOf.getProofStep().getStepNumber()
				+ " " + subst;
	}
	// END-ProofStep
	//
}
