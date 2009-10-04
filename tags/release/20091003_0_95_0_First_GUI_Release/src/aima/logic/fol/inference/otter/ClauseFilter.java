package aima.logic.fol.inference.otter;

import java.util.Set;

import aima.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface ClauseFilter {
	Set<Clause> filter(Set<Clause> clauses);
}
