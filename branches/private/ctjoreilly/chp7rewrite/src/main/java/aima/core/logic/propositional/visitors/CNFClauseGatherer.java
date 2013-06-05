package aima.core.logic.propositional.visitors;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */
public class CNFClauseGatherer extends BasicGatherer<Sentence> {
	AndDetector detector;

	public CNFClauseGatherer() {
		detector = new AndDetector();
	}

	@Override
	public Set<Sentence> visitBinarySentence(ComplexSentence bs, Set<Sentence> args) {

		if (detector.containsEmbeddedAnd(bs)) {
			processSubTerm(bs.getSimplerSentence(1), processSubTerm(bs.getSimplerSentence(0), args));
		} else {
			args.add(bs);
		}

		return args;
	}

	public Set<Sentence> getClausesFrom(Sentence sentence) {
		Set<Sentence> set = new LinkedHashSet<Sentence>();
		if (sentence.isPropositionSymbol() || sentence.isUnarySentence()) {
			set.add(sentence);
		} else {
			set = sentence.accept(this, set);
		}
		return set;
	}

	//
	// PRIVATE METHODS
	//
	private Set<Sentence> processSubTerm(Sentence s, Set<Sentence> soFar) {
		if (detector.containsEmbeddedAnd(s)) {
			return s.accept(this, soFar);
		} else {
			soFar.add(s);
			return soFar;
		}
	}
}