package aima.core.logic.propositional.visitors;

import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * Convert a Sentence into an equivalent Disjunctive Normal Form (DNF) Sentence.
 * A Sentence is in DNF if it is a disjunction of conjunction of literals.
 * 
 * @author Ciaran O'Reilly
 */
public class ConvertToDNF {
	/**
	 * Returns the specified sentence in its logically equivalent disjunction
	 * normal form.
	 * 
	 * @param s
	 *            a propositional logic sentence
	 * 
	 * @return the input sentence converted to it logically equivalent
	 *         disjunction normal form.
	 */
	public static Sentence convert(Sentence s) {
		Sentence result = null;

		Sentence nnfSentence = ConvertToNNF.convert(s);
		Sentence dnfSentence = DistributeAndOverOr.distribute(nnfSentence);

		result = dnfSentence;

		return result;
	}
}
