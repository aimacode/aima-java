package aima.core.logic.fol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class StandardizeApartInPlace {
	//
	private static CollectAllVariables _collectAllVariables = new CollectAllVariables();

	public static int standardizeApart(Chain c, int saIdx) {
		List<Variable> variables = new ArrayList<Variable>();
		for (Literal l : c.getLiterals()) {
			collectAllVariables(l.getAtomicSentence(), variables);
		}

		return standardizeApart(variables, c, saIdx);
	}

	public static int standardizeApart(Clause c, int saIdx) {
		List<Variable> variables = new ArrayList<Variable>();
		for (Literal l : c.getLiterals()) {
			collectAllVariables(l.getAtomicSentence(), variables);
		}

		return standardizeApart(variables, c, saIdx);
	}

	//
	// PRIVATE METHODS
	//
	private static int standardizeApart(List<Variable> variables, Object expr,
			int saIdx) {
		Map<String, Integer> indexicals = new HashMap<String, Integer>();
		for (Variable v : variables) {
			if (!indexicals.containsKey(v.getIndexedValue())) {
				indexicals.put(v.getIndexedValue(), saIdx++);
			}
		}
		for (Variable v : variables) {
			Integer i = indexicals.get(v.getIndexedValue());
			if (null == i) {
				throw new RuntimeException("ERROR: duplicate var=" + v
						+ ", expr=" + expr);
			} else {
				v.setIndexical(i);
			}
		}

		return saIdx;
	}

	private static void collectAllVariables(Sentence s, List<Variable> vars) {
		s.accept(_collectAllVariables, vars);
	}
}

class CollectAllVariables implements FOLVisitor {
	public CollectAllVariables() {

	}

	@SuppressWarnings("unchecked")
	public Object visitVariable(Variable var, Object arg) {
		List<Variable> variables = (List<Variable>) arg;
		variables.add(var);
		return var;
	}

	@SuppressWarnings("unchecked")
	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		// Ensure I collect quantified variables too
		List<Variable> variables = (List<Variable>) arg;
		variables.addAll(sentence.getVariables());

		sentence.getQuantified().accept(this, arg);

		return sentence;
	}

	public Object visitPredicate(Predicate predicate, Object arg) {
		for (Term t : predicate.getTerms()) {
			t.accept(this, arg);
		}
		return predicate;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		equality.getTerm1().accept(this, arg);
		equality.getTerm2().accept(this, arg);
		return equality;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		for (Term t : function.getTerms()) {
			t.accept(this, arg);
		}
		return function;
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		sentence.getNegated().accept(this, arg);
		return sentence;
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		sentence.getFirst().accept(this, arg);
		sentence.getSecond().accept(this, arg);
		return sentence;
	}
}
