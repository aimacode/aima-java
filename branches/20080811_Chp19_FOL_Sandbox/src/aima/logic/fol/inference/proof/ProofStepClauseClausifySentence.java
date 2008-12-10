package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.ast.Sentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepClauseClausifySentence extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Clause clausified = null;

	public ProofStepClauseClausifySentence(Clause clausified,
			Sentence origSentence) {
		this.clausified = clausified;
		this.predecessors.add(new ProofStepPremise(origSentence.toString()));
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return clausified.toString();
	}

	public String getJustification() {
		return "Clausified " + predecessors.get(0).getStepNumber();
	}
	// END-ProofStep
	//
}
