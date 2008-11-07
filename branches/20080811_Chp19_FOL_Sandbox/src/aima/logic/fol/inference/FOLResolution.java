package aima.logic.fol.inference;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.Connectors;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.parsing.ast.ConnectedSentence;
import aima.logic.fol.parsing.ast.NotSentence;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 297.
 * 
 * The algorithmic approach is identical to the propositional case, described
 * in Figure 7.12. However, this implementation will use an Answer predicate, 
 * so that queries with Variables may be answered.
 * 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLResolution implements InferenceProcedure {

	//
	// START-InferenceProcedure
	public Set<Map<Variable, Term>> ask(FOLKnowledgeBase KB, Sentence alpha) {
		Set<Map<Variable, Term>> result = new LinkedHashSet<Map<Variable, Term>>();

		// clauses <- the set of clauses in CNF representation of KB ^ ~alpha
		Set<Clause> clauses = new LinkedHashSet<Clause>();
		clauses.addAll(KB.getAllClauses());
		Sentence notAlpha = new NotSentence(alpha);
		// Want to use an answer literal to pull
		// query variables where necessary
		Predicate answerLiteral = KB.createAnswerLiteral(notAlpha);
		Set<Variable> answerLiteralVariables = KB
				.collectAllVariables(answerLiteral);
		Clause answerClause = new Clause();

		if (answerLiteralVariables.size() > 0) {
			Sentence notAlphaWithAnswer = new ConnectedSentence(Connectors.OR,
					notAlpha, answerLiteral);
			clauses.addAll(KB.convertToClauses(notAlphaWithAnswer));

			answerClause.addPositiveLiteral(answerLiteral);
		} else {
			clauses.addAll(KB.convertToClauses(notAlpha));
		}

		// new <- {}
		Set<Clause> newClauses = new LinkedHashSet<Clause>();
		
		// loop do
		do {
			// clauses <- clauses <UNION> new
			clauses.addAll(newClauses);
			newClauses.clear();

			// for each Ci, Cj in clauses do
			Clause[] clausesA = new Clause[clauses.size()];
			clauses.toArray(clausesA);
			// Basically, using the simple T)wo F)inger M)ethod here.
			for (int i = 0; i < clausesA.length; i++) {
				for (int j = i; j < clausesA.length; j++) {
					Clause cI = clausesA[i];
					Clause cJ = clausesA[j];
					
					// Get the Factors for each clause
					Set<Clause> cIFactors = cI.getFactors(KB);

					Set<Clause> cJFactors = cJ.getFactors(KB);
					
					for (Clause cIFac : cIFactors) {
						for (Clause cJFac : cJFactors) {
							// resolvent <- FOL-RESOLVE(Ci, Cj)
							Set<Clause> resolvents = cIFac
									.binaryResolvents(KB,
									cJFac);

							if (resolvents.size() > 0) {
								// new <- new <UNION> resolvent
								for (Clause rc : resolvents) {
									newClauses.addAll(rc.getFactors(KB));								
								}
								
								if (checkAndHandleFinalAnswer(resolvents,
										result, answerClause,
										answerLiteralVariables)) {
									return result;
								}
							}
						}
					}
				}
			}
			// if new is a <SUBSET> of clauses then finished
			// searching for an answer
		} while (!clauses.containsAll(newClauses));

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

		// Can only be the answer if an atomic clause
		if (resolvents.contains(answerClause)) {
			for (Clause resolvent : resolvents) {
				if (resolvent.equals(answerClause)) {
					Map<Variable, Term> answerBindings = new HashMap<Variable, Term>();
					if (!answerClause.isEmpty()) {
						Predicate fact = resolvent.getPositiveLiterals().get(0);
						List<Term> answerTerms = fact.getTerms();
						int idx = 0;
						for (Variable v : answerLiteralVariables) {
							answerBindings.put(v, answerTerms.get(idx));
							idx++;
						}
					}
					result.add(answerBindings);
				}
			}

			// If the answer clause has no bindings
			// then finish processing once the
			// empty clause is detected.
			if (answerClause.isEmpty()) {
				return true;
			}
		}

		return false;
	}
}
