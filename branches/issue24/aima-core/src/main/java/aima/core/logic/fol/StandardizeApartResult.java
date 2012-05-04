package aima.core.logic.fol;

import java.util.Map;

import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class StandardizeApartResult {
	private Sentence originalSentence = null;
	private Sentence standardized = null;
	private Map<Variable, Term> forwardSubstitution = null;
	private Map<Variable, Term> reverseSubstitution = null;

	public StandardizeApartResult(Sentence originalSentence,
			Sentence standardized, Map<Variable, Term> forwardSubstitution,
			Map<Variable, Term> reverseSubstitution) {
		this.originalSentence = originalSentence;
		this.standardized = standardized;
		this.forwardSubstitution = forwardSubstitution;
		this.reverseSubstitution = reverseSubstitution;
	}

	public Sentence getOriginalSentence() {
		return originalSentence;
	}

	public Sentence getStandardized() {
		return standardized;
	}

	public Map<Variable, Term> getForwardSubstitution() {
		return forwardSubstitution;
	}

	public Map<Variable, Term> getReverseSubstitution() {
		return reverseSubstitution;
	}
}
