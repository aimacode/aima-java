package aima.core.logic.fol.parsing.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class QuantifiedSentence implements Sentence {
	private String quantifier;
	private List<Variable> variables = new ArrayList<Variable>();
	private Sentence quantified;
	private List<FOLNode> args = new ArrayList<FOLNode>();
	private String stringRep = null;
	private int hashCode = 0;

	public QuantifiedSentence(String quantifier, List<Variable> variables,
			Sentence quantified) {
		this.quantifier = quantifier;
		this.variables.addAll(variables);
		this.quantified = quantified;
		this.args.addAll(variables);
		this.args.add(quantified);
	}

	public String getQuantifier() {
		return quantifier;
	}

	public List<Variable> getVariables() {
		return Collections.unmodifiableList(variables);
	}

	public Sentence getQuantified() {
		return quantified;
	}

	//
	// START-Sentence
	public String getSymbolicName() {
		return getQuantifier();
	}

	public boolean isCompound() {
		return true;
	}

	public List<FOLNode> getArgs() {
		return Collections.unmodifiableList(args);
	}

	public Object accept(FOLVisitor v, Object arg) {
		return v.visitQuantifiedSentence(this, arg);
	}

	public QuantifiedSentence copy() {
		List<Variable> copyVars = new ArrayList<Variable>();
		for (Variable v : variables) {
			copyVars.add(v.copy());
		}
		return new QuantifiedSentence(quantifier, copyVars, quantified.copy());
	}

	// END-Sentence
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if ((o == null) || (this.getClass() != o.getClass())) {
			return false;
		}
		QuantifiedSentence cs = (QuantifiedSentence) o;
		return cs.quantifier.equals(quantifier)
				&& cs.variables.equals(variables)
				&& cs.quantified.equals(quantified);
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + quantifier.hashCode();
			for (Variable v : variables) {
				hashCode = 37 * hashCode + v.hashCode();
			}
			hashCode = hashCode * 37 + quantified.hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		if (null == stringRep) {
			StringBuilder sb = new StringBuilder();
			sb.append(quantifier);
			sb.append(" ");
			for (Variable v : variables) {
				sb.append(v.toString());
				sb.append(" ");
			}
			sb.append(quantified.toString());
			stringRep = sb.toString();
		}
		return stringRep;
	}
}