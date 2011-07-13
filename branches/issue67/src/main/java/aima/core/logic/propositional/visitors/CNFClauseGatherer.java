package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.UnarySentence;

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
	public Object visitBinarySentence(BinarySentence bs, Object args) {

		Set<Sentence> soFar = (Set<Sentence>) args;

		if (detector.containsEmbeddedAnd(bs)) {
			processSubTerm(bs.getSecond(), processSubTerm(bs.getFirst(), soFar));
		} else {
			soFar.add(bs);
		}

		return soFar;

	}

	@SuppressWarnings("unchecked")
	public Set<Sentence> getClausesFrom(Sentence sentence) {
		Set<Sentence> set = new HashSet<Sentence>();
		if (sentence instanceof Symbol) {
			set.add(sentence);
		} else if (sentence instanceof UnarySentence) {
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