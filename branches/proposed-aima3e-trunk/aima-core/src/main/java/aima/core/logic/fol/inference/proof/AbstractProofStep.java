package aima.core.logic.fol.inference.proof;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public abstract class AbstractProofStep implements ProofStep {
	private int step = 0;

	public AbstractProofStep() {

	}

	//
	// START-ProofStep
	public int getStepNumber() {
		return step;
	}

	public void setStepNumber(int step) {
		this.step = step;
	}

	public abstract List<ProofStep> getPredecessorSteps();

	public abstract String getProof();

	public abstract String getJustification();

	// END-ProofStep
	//
}
