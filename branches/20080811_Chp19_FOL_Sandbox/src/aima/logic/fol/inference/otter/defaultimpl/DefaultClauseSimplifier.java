package aima.logic.fol.inference.otter.defaultimpl;

import aima.logic.fol.inference.otter.ClauseSimplifier;
import aima.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class DefaultClauseSimplifier implements ClauseSimplifier {
	public DefaultClauseSimplifier() {

	}

	//
	// START-ClauseSimplifier
	public Clause simplify(Clause c) {
		return c;
	}
	
	// END-ClauseSimplifier
	//
}
