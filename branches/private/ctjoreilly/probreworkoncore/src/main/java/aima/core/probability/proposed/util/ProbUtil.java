package aima.core.probability.proposed.util;

import aima.core.probability.proposed.model.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.model.proposition.Proposition;

public class ProbUtil {

	/**
	 * Convenience method for ensure a conjunction of probabilistic
	 * propositions.
	 * 
	 * @param props
	 *            propositions to be combined into a ConjunctiveProposition if
	 *            necessary.
	 * @return a ConjunctivePropositions if more than 1 proposition in 'props',
	 *         otherwise props[0].
	 */
	public static Proposition constructConjunction(Proposition[] props) {
		return constructConjunction(props, 0);
	}

	//
	// PRIVATE METHODS
	//

	private static Proposition constructConjunction(Proposition[] props, int idx) {
		if ((idx + 1) == props.length) {
			return props[idx];
		}

		return new ConjunctiveProposition(props[idx], constructConjunction(
				props, idx + 1));
	}
}
