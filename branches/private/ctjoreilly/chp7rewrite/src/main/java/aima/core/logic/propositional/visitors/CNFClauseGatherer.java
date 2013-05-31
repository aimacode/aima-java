package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */
public class CNFClauseGatherer extends BasicTraverser {
	AndDetector detector;

	public CNFClauseGatherer() {
		detector = new AndDetector();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitBinarySentence(ComplexSentence bs, Object args) {

		Set<Sentence> soFar = (Set<Sentence>) args;

		if (detector.containsEmbeddedAnd(bs)) {
			processSubTerm(bs.getSimplerSentence(1), processSubTerm(bs.getSimplerSentence(0), soFar));
		} else {
			soFar.add(bs);
		}

		return soFar;
	}

	@SuppressWarnings("unchecked")
	public Set<Sentence> getClausesFrom(Sentence sentence) {
		Set<Sentence> set = new HashSet<Sentence>();
		if (sentence.isPropositionSymbol() || sentence.isUnarySentence()) {
			set.add(sentence);
		} else {
			set = (Set<Sentence>) sentence.accept(this, set);
		}
		return set;
	}

	//
	// PRIVATE METHODS
	//
	@SuppressWarnings("unchecked")
	private Set<Sentence> processSubTerm(Sentence s, Set<Sentence> soFar) {
		if (detector.containsEmbeddedAnd(s)) {
			return (Set<Sentence>) s.accept(this, soFar);
		} else {
			soFar.add(s);
			return soFar;
		}
	}
}