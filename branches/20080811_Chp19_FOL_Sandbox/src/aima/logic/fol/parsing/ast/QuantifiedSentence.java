/*
 * Created on Sep 18, 2004
 *
 */
package aima.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.List;

import aima.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * 
 */

public class QuantifiedSentence implements Sentence {
	private String quantifier;

	private List<Variable> variables;

	private Sentence quantified;

	public QuantifiedSentence(String quantifier, List<Variable> variables,
			Sentence quantified) {
		this.quantifier = quantifier;
		this.variables = variables;
		this.quantified = quantified;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		QuantifiedSentence cs = (QuantifiedSentence) o;
		return ((cs.quantifier.equals(quantifier))
				&& (variables.equals(variables)) && (quantified
				.equals(quantified)));

	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + quantifier.hashCode();
		for (Variable v : variables) {
			result = 37 * result + v.hashCode();
		}
		result = result * 37 + quantified.hashCode();
		return result;
	}

	@Override
	public String toString() {
		String pre = quantifier + " ";
		for (int i = 0; i < variables.size(); i++) {
			pre = pre + variables.get(i).toString() + " ";
		}
		pre += " ";
		String post = " " + quantified.toString() + "  ";
		return pre + post;
	}

	public Object accept(FOLVisitor v, Object arg) {

		return v.visitQuantifiedSentence(this, arg);

	}

	public QuantifiedSentence copy() {
		List<Variable> copyVars = new ArrayList<Variable>();
		for (int i = 0; i < variables.size(); i++) {
			Variable v = variables.get(i);
			copyVars.add(v.copy());
		}
		return new QuantifiedSentence(quantifier, copyVars,
				(Sentence) quantified.copy());
	}

	public Sentence getQuantified() {
		return quantified;
	}

	public List getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public String getQuantifier() {
		return quantifier;
	}

	public List<String> getVariablesAsString() {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < variables.size(); i++) {
			Variable var = variables.get(i);
			ret.add(var.getValue());
		}
		return ret;
	}
}