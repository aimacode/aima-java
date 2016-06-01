package aima.core.logic.basic.propositional.visitors;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.kb.data.Literal;
import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;

/**
 * Utility class for collecting clauses from CNF Sentences.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class ClauseCollector extends BasicGatherer<Clause> {

	/**
	 * Collect a set of clauses from a list of given sentences.
	 * 
	 * @param cnfSentences
	 *            a list of CNF sentences from which to collect clauses.
	 * @return a set of all contained clauses.
	 * @throws IllegalArgumentException
	 *             if any of the given sentences are not in CNF.
	 */
	public static Set<Clause> getClausesFrom(Sentence... cnfSentences) {
		Set<Clause> result = new LinkedHashSet<Clause>();

		ClauseCollector clauseCollector = new ClauseCollector();
		for (Sentence cnfSentence : cnfSentences) {			
			result = cnfSentence.accept(clauseCollector, result);
		}	

		return result;
	}
	
	@Override
	public Set<Clause> visitPropositionSymbol(PropositionSymbol s, Set<Clause> arg) {
		// a positive unit clause
		Literal positiveLiteral = new Literal(s);
		arg.add(new Clause(positiveLiteral));
		
		return arg;
	}
	
	@Override
	public Set<Clause> visitUnarySentence(ComplexSentence s, Set<Clause> arg) {
		
		if (!s.getSimplerSentence(0).isPropositionSymbol()) {
			throw new IllegalStateException("Sentence is not in CNF: "+s);
		}
		
		// a negative unit clause
		Literal negativeLiteral = new Literal((PropositionSymbol)s.getSimplerSentence(0), false);
		arg.add(new Clause(negativeLiteral));
		
		return arg;
	}
	
	@Override
	public Set<Clause> visitBinarySentence(ComplexSentence s, Set<Clause> arg) {
		
		if (s.isAndSentence()) {
			s.getSimplerSentence(0).accept(this, arg);
			s.getSimplerSentence(1).accept(this, arg);			
		} else if (s.isOrSentence()) {
			List<Literal> literals = new ArrayList<Literal>(LiteralCollector.getLiterals(s));
			arg.add(new Clause(literals));			
		} else {
			throw new IllegalArgumentException("Sentence is not in CNF: "+s);
		}
		
		return arg;
	}

	//
	// PRIVATE
	//
	private static class LiteralCollector extends BasicGatherer<Literal> {
		
		private static Set<Literal> getLiterals(Sentence disjunctiveSentence) {
			Set<Literal> result = new LinkedHashSet<Literal>();
			
			LiteralCollector literalCollector = new LiteralCollector();
			result = disjunctiveSentence.accept(literalCollector, result);
			
			return result;
		}
		
		@Override
		public Set<Literal> visitPropositionSymbol(PropositionSymbol s, Set<Literal> arg) {
			// a positive literal
			Literal positiveLiteral = new Literal(s);
			arg.add(positiveLiteral);
			
			return arg;
		}
		
		@Override
		public Set<Literal> visitUnarySentence(ComplexSentence s, Set<Literal> arg) {
			
			if (!s.getSimplerSentence(0).isPropositionSymbol()) {
				throw new IllegalStateException("Sentence is not in CNF: "+s);
			}
			
			// a negative literal
			Literal negativeLiteral = new Literal((PropositionSymbol)s.getSimplerSentence(0), false);

			arg.add(negativeLiteral);
			
			return arg;
		}
		
		@Override
		public Set<Literal> visitBinarySentence(ComplexSentence s, Set<Literal> arg) {
			if (s.isOrSentence()) {
				s.getSimplerSentence(0).accept(this, arg);
				s.getSimplerSentence(1).accept(this, arg);
			} else {
				throw new IllegalArgumentException("Sentence is not in CNF: "+s);
			}
			return arg;
		}
	}
}
