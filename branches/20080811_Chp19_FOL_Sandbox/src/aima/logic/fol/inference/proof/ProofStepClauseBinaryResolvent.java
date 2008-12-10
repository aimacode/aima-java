package aima.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepClauseBinaryResolvent extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Clause resolvent = null;
	private Clause parent1, parent2 = null;
	private Map<Variable, Term> subst = null;
	private Map<Variable, Term> renameSubst = null;

	public ProofStepClauseBinaryResolvent(Clause resolvent, Clause parent1,
			Clause parent2, Map<Variable, Term> subst,
			Map<Variable, Term> renameSubst) {
		this.resolvent = resolvent;
		this.parent1 = parent1;
		this.parent2 = parent2;
		this.subst = subst;
		this.renameSubst = renameSubst;
		this.predecessors.add(parent1.getProofStep());
		this.predecessors.add(parent2.getProofStep());
	}

	//
	// START-ProofStep
	public List<ProofStep> getPredecessorSteps() {
		return Collections.unmodifiableList(predecessors);
	}

	public String getProof() {
		return resolvent.toString();
	}

	public String getJustification() {
		int lowStep = parent1.getProofStep().getStepNumber();
		int highStep = parent2.getProofStep().getStepNumber();

		if (lowStep > highStep) {
			lowStep = highStep;
			highStep = parent1.getProofStep().getStepNumber();
		}

		return "Resolution: " + lowStep + "," + highStep + " " + subst + ", "
				+ renameSubst;
	}
	// END-ProofStep
	//
}
