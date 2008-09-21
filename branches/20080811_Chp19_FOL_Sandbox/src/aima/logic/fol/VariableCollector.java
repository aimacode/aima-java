/*
 * Created on Sep 20, 2004
 *
 */
package aima.logic.fol;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.logic.fol.parsing.AbstractFOLVisitor;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.QuantifiedSentence;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class VariableCollector extends AbstractFOLVisitor {

	public VariableCollector(FOLParser parser) {
		super(parser);
	}
	
	public Set<Variable> collectAllVariables(Sentence sentence) {
		Set<Variable> variables = new LinkedHashSet<Variable>();
		
		sentence.accept(this, variables);

		return variables;
	}
	
	public List<String> getAllVariableNames(Sentence sentence) {
		Set<Variable> variables = collectAllVariables(sentence);
		List<String> names = new ArrayList<String>();
		for (Variable var : variables) {
			names.add(var.getValue());
		}
		return names;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visitVariable(Variable var, Object arg) {
		Set<Variable> variables = (Set<Variable>) arg;
		variables.add(var);
		return var;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		// Ensure I collect quantified variables too
		Set<Variable> variables = (Set<Variable>) arg;
		variables.addAll(sentence.getVariables());
		
		sentence.getQuantified().accept(this, arg);
		
		return sentence;
	}
}