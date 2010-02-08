package aima.core.logic.fol.inference.proof;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface ProofStep {
	int getStepNumber();

	void setStepNumber(int step);

	List<ProofStep> getPredecessorSteps();

	String getProof();

	String getJustification();
}
