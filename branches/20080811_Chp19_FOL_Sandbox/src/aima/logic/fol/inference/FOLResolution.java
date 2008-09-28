package aima.logic.fol.inference;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.Connectors;
import aima.logic.fol.VariableCollector;
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
 * in Figure 7.12. However, this implementation will use the full resolution
 * rule, factoring and an Answer predicate, so that queries may be answered.
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
		Sentence notAlphaWithAnswer = new ConnectedSentence(Connectors.OR,
				notAlpha, answerLiteral);
		clauses.addAll(KB.convertToClauses(notAlphaWithAnswer));

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
			for (int i = 0; i < clauses.size(); i++) {
				for (int j = i + 1; j < clauses.size(); j++) {
					// resolvent <- FOL-RESOLVE(Ci, Cj)
					Clause resolvent = clausesA[i].resolvent(clausesA[j]);
					if (null != resolvent) {
						// new <- new <UNION> resolvent
						newClauses.add(resolvent);
						checkAndHandleAnswer(resolvent, result, answerLiteral,
								answerLiteralVariables);
					}
				}
			}
			// if new <SUBSET> clauses then finished
			// searching for an answer
		} while (!clauses.containsAll(newClauses));

		return result;
	}

	// END-InferenceProcedure
	// 

	//
	// PRIVATE METHODS
	//
	private void checkAndHandleAnswer(Clause resolvent,
			Set<Map<Variable, Term>> result, Predicate answerLiteral,
			Set<Variable> answerLiteralVariables) {

		// Can only be the answer if an atomic clause
		if (resolvent.isAtomicClause()) {
			Predicate fact = resolvent.getPositiveLiterals().get(0);
			// Check if is an answer
			if (fact.getPredicateName().equals(answerLiteral.getPredicateName())) {
				List<Term> answerTerms = fact.getTerms();
				Map<Variable, Term> answerBindings = new HashMap<Variable, Term>();
				int idx = 0;
				for (Variable v : answerLiteralVariables) {
					answerBindings.put(v, answerTerms.get(idx));
					idx++;
				}
				
				result.add(answerBindings);
			}
		}
	}
}
