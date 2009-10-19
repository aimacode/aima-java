package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.parsing.ast.Sentence;

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
		this.predecessors.add(new ProofStepPremise(origSentence));
	}

	//
	// START-ProofStep
	@Override
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	@Override
	public String getProof() {
		return clausified.toString();
	}

	@Override
	public String getJustification() {
		return "Clausified " + predecessors.get(0).getStepNumber();
	}
	// END-ProofStep
	//
}
