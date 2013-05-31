package aima.core.logic.propositional.visitors;

import java.util.HashSet;
import java.util.Set;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

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
			processSubTerm(bs.get(1), processSubTerm(bs.get(0), soFar));
		} else {
			soFar.add(bs);
		}

		return soFar;
	}

	@SuppressWarnings("unchecked")
	public Set<Sentence> getClausesFrom(Sentence sentence) {
		Set<Sentence> set = new HashSet<Sentence>();
		if (sentence instanceof PropositionSymbol) {
			set.add(sentence);
		} else {
			ComplexSentence cs = (ComplexSentence) sentence;
			if (cs.isUnary()) {
				set.add(sentence);
			}
			else if (cs.isBinary()) {
				set = (Set<Sentence>) sentence.accept(this, set);
			}
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