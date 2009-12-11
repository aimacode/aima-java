package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.ast.TermEquality;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepClauseParamodulation extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Clause paramodulated = null;
	private Clause topClause = null;
	private Clause equalityClause = null;
	private TermEquality assertion = null;

	public ProofStepClauseParamodulation(Clause paramodulated,
			Clause topClause, Clause equalityClause, TermEquality assertion) {
		this.paramodulated = paramodulated;
		this.topClause = topClause;
		this.equalityClause = equalityClause;
		this.assertion = assertion;
		this.predecessors.add(topClause.getProofStep());
		this.predecessors.add(equalityClause.getProofStep());
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return paramodulated.toString();
	}

	public String getJustification() {
		return "Paramodulation: " + topClause.getProofStep().getStepNumber()
				+ ", " + equalityClause.getProofStep().getStepNumber() + ", ["
				+ assertion + "]";

	}
	// END-ProofStep
	//
}
