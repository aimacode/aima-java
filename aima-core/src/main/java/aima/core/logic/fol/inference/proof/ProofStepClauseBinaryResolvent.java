package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofStepClauseBinaryResolvent extends AbstractProofStep {
	private List<ProofStep> predecessors = new ArrayList<ProofStep>();
	private Clause resolvent = null;
	private Literal posLiteral = null;
	private Literal negLiteral = null;
	private Clause parent1, parent2 = null;
	private Map<Variable, Term> subst = new LinkedHashMap<Variable, Term>();
	private Map<Variable, Term> renameSubst = new LinkedHashMap<Variable, Term>();

	public ProofStepClauseBinaryResolvent(Clause resolvent, Literal pl,
			Literal nl, Clause parent1, Clause parent2,
			Map<Variable, Term> subst, Map<Variable, Term> renameSubst) {
		this.resolvent = resolvent;
		this.posLiteral = pl;
		this.negLiteral = nl;
		this.parent1 = parent1;
		this.parent2 = parent2;
		this.subst.putAll(subst);
		this.renameSubst.putAll(renameSubst);
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

		return "Resolution: " + lowStep + ", " + highStep + "  [" + posLiteral
				+ ", " + negLiteral + "], subst=" + subst + ", renaming="
				+ renameSubst;
	}
	// END-ProofStep
	//
}
