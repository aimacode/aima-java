package aima.core.learning.knowledge;

import java.util.List;

import aima.core.logic.fol.kb.FOLKnowledgeBase;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 19.2, page 681.
 * 
 * <code>
 * function CURRENT-BEST-LEARNING(examples) returns a hypothesis
 * 
 *   H <- any hypothesis consistent with the first example in examples.
 *   for each remaining example in examples do
 *     if e is false positive for H then
 *       H <- choose a specialization of H consistent with examples
 *     else if e is false negative for H then
 *       H <- choose a generalization of H consistent with examples
 *     if no consistent specialization/generalization can be found then fail
 *   return H
 * </code>
 * 
 * Figure 19.2 The current-best-hypothesis learning algorithm. It searches for a consistent
 * hypothesis and backtracks when no consistent specialization/generalization can be found.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CurrentBestLearning {
	private FOLDataSetDomain folDSDomain = null;
	private FOLKnowledgeBase kbForLearning = null;

	//
	// PUBLIC METHODS
	//
	public CurrentBestLearning(FOLDataSetDomain folDSDomain,
			FOLKnowledgeBase kbForLearning) {
		this.folDSDomain = folDSDomain;
		this.kbForLearning = kbForLearning;
	}

	// * function CURRENT-BEST-LEARNING(examples) returns a hypothesis
	public Hypothesis currentBestLearning(List<FOLExample> examples) {

		// TODO-use the default from pg 679 for now.
		String c1 = "patrons(v,Some)";
		String c2 = "patrons(v,Full) AND (hungry(v) AND type(v,French))";
		String c3 = "patrons(v,Full) AND (hungry(v) AND (type(v,Thai) AND fri_sat(v)))";
		String c4 = "patrons(v,Full) AND (hungry(v) AND type(v,Burger))";
		String sh = "FORALL v (will_wait(v) <=> (" + c1 + " OR (" + c2
				+ " OR (" + c3 + " OR (" + c4 + ")))))";

		Hypothesis H = new Hypothesis(kbForLearning.tell(sh));

		// TODO
		// * H <- any hypothesis consistent with the first example in examples.
		// * for each remaining example in examples do
		// * if e is false positive for H then
		// * H <- choose a specialization of H consistent with examples
		// * else if e is false negative for H then
		// * H <- choose a generalization of H consistent with examples
		// * if no consistent specialization/generalization can be found then
		// fail
		// * return H
		return H;
	}

	//
	// PRIVATE METHODS
	//
}
