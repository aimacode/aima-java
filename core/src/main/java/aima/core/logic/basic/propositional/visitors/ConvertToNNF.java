package aima.core.logic.basic.propositional.visitors;

import aima.core.logic.basic.propositional.parsing.ast.Sentence;

/**
 * Convert a Sentence into an equivalent Negation Normal Form (NNF) Sentence. A
 * Sentence is in NNF if negation is allowed only over atoms, and conjunction,
 * disjunction, and negation are the only allowed boolean connectives
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class ConvertToNNF {

	/**
	 * Returns the specified sentence in its logically equivalent negation
	 * normal form.
	 * 
	 * @param s
	 *            a propositional logic sentence
	 * 
	 * @return the input sentence converted to it logically equivalent
	 *         negation normal form.
	 */
	public static Sentence convert(Sentence s) {
		Sentence result = null;

		Sentence biconditionalsRemoved = BiconditionalElimination.eliminate(s);
		Sentence implicationsRemoved = ImplicationElimination
				.eliminate(biconditionalsRemoved);
		Sentence notsMovedIn = MoveNotInwards
				.moveNotsInward(implicationsRemoved);

		result = notsMovedIn;

		return result;
	}
}
