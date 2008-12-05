package aima.logic.fol.inference.otter;

import aima.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface ClauseSimplifier {
	Clause simplify(Clause c);
}
