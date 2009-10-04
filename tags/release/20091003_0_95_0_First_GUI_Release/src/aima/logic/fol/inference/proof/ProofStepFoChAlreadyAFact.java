package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.kb.data.Literal;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepFoChAlreadyAFact extends AbstractProofStep {
	//
	private static final List<ProofStep> _noPredecessors = new ArrayList<ProofStep>();
	//
	private Literal fact = null;

	public ProofStepFoChAlreadyAFact(Literal fact) {
		this.fact = fact;
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(_noPredecessors);
	}

	public String getProof() {
		return fact.toString();
	}

	public String getJustification() {
		return "Already a known fact in the KB.";
	}
	// END-ProofStep
	//
}
