package aima.logic.fol.inference;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.Connectors;
import aima.logic.fol.inference.trace.FOLTFMResolutionTracer;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.parsing.ast.ConnectedSentence;
import aima.logic.fol.parsing.ast.NotSentence;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 297.
 * 
 * The algorithmic approach is very close to the propositional case, described
 * in Figure 7.12. However, this implementation will use the T)wo F)inger M)ethod 
 * for looking for resolvents between clauses. 
 * Note: very inefficient, 
 * see: http://logic.stanford.edu/classes/cs157/2008/lectures/lecture04.pdf,
 * slide 21 for the propositional case.  
 * In addition, an Answer literal will be used so that queries with Variables 
 * may be answered (see pg. 300 of AIMA).
 * 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLTFMResolution implements InferenceProcedure {

	private long maxQueryTime = 0; // <= 0 indicates infinity
	private FOLTFMResolutionTracer tracer = null;

	public FOLTFMResolution() {

	}

	public FOLTFMResolution(long maxQueryTime) {
		setMaxQueryTime(maxQueryTime);
	}

	public FOLTFMResolution(FOLTFMResolutionTracer tracer) {
		setTracer(tracer);
	}

	public long getMaxQueryTime() {
		return maxQueryTime;
	}

	public void setMaxQueryTime(long maxQueryTime) {
		this.maxQueryTime = maxQueryTime;
	}

	public FOLTFMResolutionTracer getTracer() {
		return tracer;
	}

	public void setTracer(FOLTFMResolutionTracer tracer) {
		this.tracer = tracer;
	}

	//
	// START-InferenceProcedure
	public Set<Map<Variable, Term>> ask(FOLKnowledgeBase KB, Sentence alpha) {
		Set<Map<Variable, Term>> result = new LinkedHashSet<Map<Variable, Term>>();

		// clauses <- the set of clauses in CNF representation of KB ^ ~alpha
		Set<Clause> clauses = new LinkedHashSet<Clause>();
		for (Clause c : KB.getAllClauses()) {
			c = KB.standardizeApart(c);
			c.setStandardizedApartCheckNotRequired();
			clauses.addAll(c.getFactors());
		}
		Sentence notAlpha = new NotSentence(alpha);
		// Want to use an answer literal to pull
		// query variables where necessary
		Literal answerLiteral = KB.createAnswerLiteral(notAlpha);
		Set<Variable> answerLiteralVariables = KB
				.collectAllVariables(answerLiteral.getAtomicSentence());
		Clause answerClause = new Clause();

		if (answerLiteralVariables.size() > 0) {
			Sentence notAlphaWithAnswer = new ConnectedSentence(Connectors.OR,
					notAlpha, answerLiteral.getAtomicSentence());
			for (Clause c : KB.convertToClauses(notAlphaWithAnswer)) {
				c = KB.standardizeApart(c);
				c.setStandardizedApartCheckNotRequired();
				clauses.addAll(c.getFactors());
			}

			answerClause.addLiteral(answerLiteral);
		} else {
			for (Clause c : KB.convertToClauses(notAlpha)) {
				c = KB.standardizeApart(c);
				c.setStandardizedApartCheckNotRequired();
				clauses.addAll(c.getFactors());
			}
		}

		// Track maxQueryTime
		long finishTime = -1;
		if (maxQueryTime > 0) {
			finishTime = System.currentTimeMillis() + maxQueryTime;
		}

		// new <- {}
		Set<Clause> newClauses = new LinkedHashSet<Clause>();
		Set<Clause> toAdd = new LinkedHashSet<Clause>();
		// loop do
		int noOfPrevClauses = clauses.size();
		do {
			if (null != tracer) {
				tracer.stepStartWhile(clauses, clauses.size(), newClauses
						.size());
			}

			newClauses.clear();

			// for each Ci, Cj in clauses do
			Clause[] clausesA = new Clause[clauses.size()];
			clauses.toArray(clausesA);
			// Basically, using the simple T)wo F)inger M)ethod here.
			for (int i = 0; i < clausesA.length; i++) {
				Clause cI = clausesA[i];
				if (null != tracer) {
					tracer.stepOuterFor(cI);
				}
				for (int j = i; j < clausesA.length; j++) {
					Clause cJ = clausesA[j];

					if (null != tracer) {
						tracer.stepInnerFor(cI, cJ);
					}

					// resolvent <- FOL-RESOLVE(Ci, Cj)
					Set<Clause> resolvents = cI.binaryResolvents(cJ);

					if (resolvents.size() > 0) {
						toAdd.clear();
						// new <- new <UNION> resolvent
						for (Clause rc : resolvents) {							
							toAdd.addAll(rc.getFactors());
						}

						if (null != tracer) {
							tracer.stepResolved(cI, cJ, toAdd);
						}

						newClauses.addAll(toAdd);

						if (checkAndHandleFinalAnswer(resolvents, result,
								answerClause, answerLiteralVariables)) {
							if (null != tracer) {
								tracer.stepFinished(clauses, result);
							}
							return result;
						}
					}

					if (-1 != finishTime) {
						if (System.currentTimeMillis() > finishTime) {
							break;
						}
					}
				}
				if (-1 != finishTime) {
					if (System.currentTimeMillis() > finishTime) {
						break;
					}
				}
			}

			noOfPrevClauses = clauses.size();

			// clauses <- clauses <UNION> new
			clauses.addAll(newClauses);

			if (-1 != finishTime) {
				if (System.currentTimeMillis() > finishTime) {
					break;
				}
			}

			// if new is a <SUBSET> of clauses then finished
			// searching for an answer
			// (i.e. when they were added the # clauses
			// did not increase).
		} while (noOfPrevClauses < clauses.size());

		if (null != tracer) {
			tracer.stepFinished(clauses, result);
		}

		if (-1 != finishTime) {
			if (System.currentTimeMillis() > finishTime) {
				// If have run out of query time and no result
				// found yet (i.e. partial results via answer literal
				// bindings are allowed.)
				// return null to indicate answer unknown.
				if (0 == result.size()) {
					result = null;
				}
			}
		}

		return result;
	}

	// END-InferenceProcedure
	// 

	//
	// PRIVATE METHODS
	//
	private boolean checkAndHandleFinalAnswer(Set<Clause> resolvents,
			Set<Map<Variable, Term>> result, Clause answerClause,
			Set<Variable> answerLiteralVariables) {

		// If no bindings being looked for, then
		// is just a true false query.
		if (answerClause.isEmpty()) {
			// Is true if the empty clause detected.
			if (resolvents.contains(answerClause)) {
				Map<Variable, Term> answerBindings = new HashMap<Variable, Term>();
				result.add(answerBindings);
				return true;
			}
		} else {
			Literal answerLiteral = answerClause.getPositiveLiterals().get(0);
			for (Clause resolvent : resolvents) {
				if (resolvent.isUnitClause() && resolvent.isDefiniteClause()) {
					Literal resolvedLiteral = resolvent.getPositiveLiterals()
							.get(0);
					if (answerLiteral.getAtomicSentence().getSymbolicName()
							.equals(
									resolvedLiteral.getAtomicSentence()
											.getSymbolicName())) {
						Map<Variable, Term> answerBindings = new HashMap<Variable, Term>();
						if (!answerClause.isEmpty()) {
							Literal fact = resolvent.getPositiveLiterals().get(
									0);
							List<Term> answerTerms = fact.getAtomicSentence()
									.getArgs();
							int idx = 0;
							for (Variable v : answerLiteralVariables) {
								answerBindings.put(v, answerTerms.get(idx));
								idx++;
							}
						}
						result.add(answerBindings);
					}
				}
			}
		}

		return false;
	}
}
