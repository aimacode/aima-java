package aima.logic.fol.inference.otter;

import aima.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface SimplifyClause {
	Clause simplify(Clause c);
}
