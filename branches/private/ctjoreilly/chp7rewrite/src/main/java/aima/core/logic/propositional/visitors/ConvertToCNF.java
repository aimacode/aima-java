package aima.core.logic.propositional.visitors;

import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 253.<br>
 * <br>
 * A sentence expression as a conjunction of clauses is said to be in
 * <b>conjunctive normal form</b> or <b>CNF</b>.<br>
 * 
 * <pre>
 * CNFSentence -> Clause_1 & ... & Clause_n
 *      Clause -> Literal_1 | ... | Literal_m
 *     Literal -> Symbol : ~Symbol
 *      Symbol -> P : Q : R : ... // (1)
 * </pre>
 * 
 * Figure 7.14 A grammar for conjunctive normal form.<br>
 * <br>
 * Note (1): While the book states 'We use symbols that start with an upper case
 * letter and may contain other letters or subscripts' in this implementation we
 * allow any legal java identifier to stand in for a proposition symbol.<br>
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class ConvertToCNF {

	/**
	 * Returns the specified sentence in its logically equivalent conjunction of
	 * clauses.
	 * 
	 * @param s
	 *            a propositional logic sentence
	 * 
	 * @return the input sentence converted to it logically equivalent
	 *         conjunction of clauses.
	 */
	public Sentence convert(Sentence s) {
		Sentence result = s;

		result = BiconditionalElimination.eliminate(result);
		result = ImplicationElimination.eliminate(result);
		result = MoveNotInwards.moveNotsInward(result);
		result = DistributeOrOverAnd.distribute(result);

		return result;
	}
}