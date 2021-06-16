package aima.core.logic.propositional.transformations;

import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * Convert a Sentence into an equivalent Disjunctive Normal Form (DNF) Sentence.
 * A Sentence is in DNF if it is a disjunction of conjunction of literals.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
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
	public static Sentence apply(Sentence s) {
		Sentence nnfSentence = ConvertToNNF.apply(s);
		return DistributeAndOverOr.apply(nnfSentence);
	}
}
