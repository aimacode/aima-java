package aima.core.logic.fol;

import java.util.LinkedHashSet;
import java.util.Set;

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
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class VariableCollector implements FOLVisitor {

	public VariableCollector() {
	}

	// Note: The set guarantees the order in which they were
	// found.
	public Set<Variable> collectAllVariables(Sentence sentence) {
		Set<Variable> variables = new LinkedHashSet<Variable>();

		sentence.accept(this, variables);

		return variables;
	}

	public Set<Variable> collectAllVariables(Term term) {
		Set<Variable> variables = new LinkedHashSet<Variable>();

		term.accept(this, variables);

		return variables;
	}

	public Set<Variable> collectAllVariables(Clause clause) {
		Set<Variable> variables = new LinkedHashSet<Variable>();

		for (Literal l : clause.getLiterals()) {
			l.getAtomicSentence().accept(this, variables);
		}

		return variables;
	}

	public Set<Variable> collectAllVariables(Chain chain) {
		Set<Variable> variables = new LinkedHashSet<Variable>();

		for (Literal l : chain.getLiterals()) {
			l.getAtomicSentence().accept(this, variables);
		}

		return variables;
	}

	@SuppressWarnings("unchecked")
	public Object visitVariable(Variable var, Object arg) {
		Set<Variable> variables = (Set<Variable>) arg;
		variables.add(var);
		return var;
	}

	@SuppressWarnings("unchecked")
	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		// Ensure I collect quantified variables too
		Set<Variable> variables = (Set<Variable>) arg;
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