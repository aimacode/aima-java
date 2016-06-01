package aima.core.logic.basic.propositional.visitors;

import aima.core.logic.basic.propositional.parsing.ast.Sentence;

/**
 * Convert a Sentence into an equivalent Conjunctive Normal Form (CNF) Sentence.
 * A Sentence is in CNF if it is a conjunction of disjunction of literals.
 * 
 * @author Ciaran O'Reilly
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
	public static Sentence convert(Sentence s) {
		Sentence result = null;

		Sentence nnfSentence = ConvertToNNF.convert(s);
		Sentence cnfSentence = DistributeOrOverAnd.distribute(nnfSentence);
		
		result = cnfSentence;

		return result;
	}
}