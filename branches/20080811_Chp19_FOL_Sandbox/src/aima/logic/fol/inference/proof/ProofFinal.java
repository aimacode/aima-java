package aima.logic.fol.inference.proof;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class ProofFinal implements Proof {
	private Map<Variable, Term> answerBindings = new LinkedHashMap<Variable, Term>();
	
	public ProofFinal(ProofStep finalStep, Map<Variable, Term> answerBindings) {
		this.answerBindings.putAll(answerBindings);
	}

	//
	// START-Proof
	public List<ProofStep> getSteps() {
		// Only calculate if the proof stesp are actually requested.
		// TODO
		return null;
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

	public String toString() {
		return answerBindings.toString();
	}
}
