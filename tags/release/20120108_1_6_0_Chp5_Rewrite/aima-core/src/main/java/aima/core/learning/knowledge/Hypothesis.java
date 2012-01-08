package aima.core.learning.knowledge;

import aima.core.logic.fol.parsing.ast.Sentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Hypothesis {
	private Sentence hypothesis = null;

	public Hypothesis(Sentence hypothesis) {
		this.hypothesis = hypothesis;
	}

	/**
	 * <pre>
	 * FORALL v (Classification(v) &lt;=&gt; ((Description1(v) AND Description2(v, Constant1))
	 *                                 OR
	 *                                  (Description1(v) AND Description3(v))
	 *                                 )
	 *          )
	 * </pre>
	 */
	public Sentence getHypothesis() {
		return hypothesis;
	}
}
