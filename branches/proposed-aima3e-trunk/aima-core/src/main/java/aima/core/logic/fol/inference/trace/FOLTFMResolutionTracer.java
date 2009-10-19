package aima.core.logic.fol.inference.trace;

import java.util.Set;

import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.kb.data.Clause;

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

	void stepFinished(Set<Clause> clauses, InferenceResult result);
}
