package aima.logic.fol.inference.trace;

import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface FOLTFMResolutionTracer {
	void stepStartWhile(Set<Clause> clauses, int totalNoClauses,
			int totalNoNewCandidateClauses);
	
	void stepOuterFor(Clause i);

	void stepInnerFor(Clause i, Clause j);

	void stepResolved(Clause iFactor, Clause jFactor, Set<Clause> resolvents);

	void stepFinished(Set<Clause> clauses, Set<Map<Variable, Term>> result);
}
