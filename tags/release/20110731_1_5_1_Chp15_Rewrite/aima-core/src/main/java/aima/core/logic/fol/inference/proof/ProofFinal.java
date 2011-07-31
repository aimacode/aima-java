package aima.core.logic.fol.inference.proof;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofFinal implements Proof {
	private Map<Variable, Term> answerBindings = new LinkedHashMap<Variable, Term>();
	private ProofStep finalStep = null;
	private List<ProofStep> proofSteps = null;

	public ProofFinal(ProofStep finalStep, Map<Variable, Term> answerBindings) {
		this.finalStep = finalStep;
		this.answerBindings.putAll(answerBindings);
	}

	//
	// START-Proof
	public List<ProofStep> getSteps() {
		// Only calculate if the proof steps are actually requested.
		if (null == proofSteps) {
			calcualteProofSteps();
		}
		return proofSteps;
	}

	public Map<Variable, Term> getAnswerBindings() {
		return answerBindings;
	}

	public void replaceAnswerBindings(Map<Variable, Term> updatedBindings) {
		answerBindings.clear();
		answerBindings.putAll(updatedBindings);
	}

	// END-Proof
	//

	@Override
	public String toString() {
		return answerBindings.toString();
	}

	//
	// PRIVATE METHODS
	//
	private void calcualteProofSteps() {
		proofSteps = new ArrayList<ProofStep>();
		addToProofSteps(finalStep);

		// Move all premises to the front of the
		// list of steps
		int to = 0;
		for (int i = 0; i < proofSteps.size(); i++) {
			if (proofSteps.get(i) instanceof ProofStepPremise) {
				ProofStep m = proofSteps.remove(i);
				proofSteps.add(to, m);
				to++;
			}
		}

		// Move the Goals after the premises
		for (int i = 0; i < proofSteps.size(); i++) {
			if (proofSteps.get(i) instanceof ProofStepGoal) {
				ProofStep m = proofSteps.remove(i);
				proofSteps.add(to, m);
				to++;
			}
		}

		// Assign the step #s now that all the proof
		// steps have been unwound
		for (int i = 0; i < proofSteps.size(); i++) {
			proofSteps.get(i).setStepNumber(i + 1);
		}
	}

	private void addToProofSteps(ProofStep step) {
		if (!proofSteps.contains(step)) {
			proofSteps.add(0, step);
		} else {
			proofSteps.remove(step);
			proofSteps.add(0, step);
		}
		List<ProofStep> predecessors = step.getPredecessorSteps();
		for (int i = predecessors.size() - 1; i >= 0; i--) {
			addToProofSteps(predecessors.get(i));
		}
	}
}
