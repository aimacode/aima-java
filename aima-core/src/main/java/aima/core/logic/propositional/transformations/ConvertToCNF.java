package aima.core.logic.propositional.transformations;

import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * Convert a Sentence into an equivalent Conjunctive Normal Form (CNF) Sentence.
 * A Sentence is in CNF if it is a conjunction of disjunction of literals.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class ConvertToCNF {

	/**
	 * Returns the specified sentence in its logically equivalent conjunctive
	 * normal form.
	 * 
	 * @param s
	 *            a propositional logic sentence
	 * 
	 * @return the input sentence converted to it logically equivalent
	 *         conjunctive normal form.
	 */
	public static Sentence apply(Sentence s) {
		Sentence nnfSentence = ConvertToNNF.apply(s);
		return DistributeOrOverAnd.apply(nnfSentence);
	}
}