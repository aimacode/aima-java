package aima.core.logic.basic.propositional.kb.data;

import java.util.HashMap;
import java.util.Map;

import aima.core.logic.basic.propositional.parsing.PLVisitor;
import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.Connective;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * <br>
 * Models are mathematical abstractions, each of which simply fixes the truth or
 * falsehood of every relevant sentence. In propositional logic, a model simply
 * fixes the <b>truth value</b> - <em>true</em> or <em>false</em> - for
 * every proposition symbol.<br>
 * <br>
 * Models as implemented here can represent partial assignments 
 * to the set of proposition symbols in a Knowledge Base (i.e. a partial model).
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class Model implements PLVisitor<Boolean, Boolean> {

	private HashMap<PropositionSymbol, Boolean> assignments = new HashMap<PropositionSymbol, Boolean>();

	/**
	 * Default Constructor.
	 */
	public Model() {
	}

	public Model(Map<PropositionSymbol, Boolean> values) {
		assignments.putAll(values);
	}

	public Boolean getValue(PropositionSymbol symbol) {
		return assignments.get(symbol);
	}

	public boolean isTrue(PropositionSymbol symbol) {
		return Boolean.TRUE.equals(assignments.get(symbol));
	}

	public boolean isFalse(PropositionSymbol symbol) {
		return Boolean.FALSE.equals(assignments.get(symbol));
	}

	public Model union(PropositionSymbol symbol, boolean b) {
		Model m = new Model();
		m.assignments.putAll(this.assignments);
		m.assignments.put(symbol, b);
		return m;
	}
		
	public boolean isTrue(Sentence s) {
		return Boolean.TRUE.equals(s.accept(this, null));
	}

	public boolean isFalse(Sentence s) {
		return Boolean.FALSE.equals(s.accept(this, null));
	}

	public boolean isUnknown(Sentence s) {
		return null == s.accept(this, null);
	}

	@Override
	public String toString() {
		return assignments.toString();
	}

	//
	// START-PLVisitor
	@Override
	public Boolean visitPropositionSymbol(PropositionSymbol s, Boolean arg) {
		if (s.isAlwaysTrue()) {
			return Boolean.TRUE;
		}
		if (s.isAlwaysFalse()) {
			return Boolean.FALSE;
		}
		return getValue(s);
	}

	@Override
	public Boolean visitUnarySentence(ComplexSentence fs, Boolean arg) {
		Object negatedValue = fs.getSimplerSentence(0).accept(this, null);
		if (negatedValue != null) {
			return new Boolean(!((Boolean) negatedValue).booleanValue());
		} else {
			return null;
		}
	}

	@Override
	public Boolean visitBinarySentence(ComplexSentence bs, Boolean arg) {
		Boolean firstValue = (Boolean) bs.getSimplerSentence(0).accept(this,
				null);
		Boolean secondValue = (Boolean) bs.getSimplerSentence(1).accept(this,
				null);
		if ((firstValue == null) || (secondValue == null)) {
			// strictly not true for or/and
			// -FIX later
			return null;
		} else {
			Connective connective = bs.getConnective();
			if (connective.equals(Connective.AND)) {
				return firstValue && secondValue;
			} else if (connective.equals(Connective.OR)) {
				return firstValue || secondValue;
			} else if (connective.equals(Connective.IMPLICATION)) {
				return !(firstValue && !secondValue);
			} else if (connective.equals(Connective.BICONDITIONAL)) {
				return firstValue.equals(secondValue);
			}
			return null;
		}
	}
	// END-PLVisitor
	//
}