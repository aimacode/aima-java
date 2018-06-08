package aima.core.logic.fol.kb.data;

import aima.core.logic.fol.parsing.ast.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 244.<br>
 * <br>
 * A literal is either an atomic sentence (a positive literal) or a negated
 * atomic sentence (a negative literal).
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class Literal {
	private AtomicSentence atom = null;
	private boolean negativeLiteral = false;
	private String strRep = null;
	private int hashCode = 0;

	public Literal(AtomicSentence atom) {
		this.atom = atom;
	}

	public Literal(AtomicSentence atom, boolean negated) {
		this.atom = atom;
		this.negativeLiteral = negated;
	}

	public Literal newInstance(AtomicSentence atom) {
		return new Literal(atom, negativeLiteral);
	}

	public boolean isPositiveLiteral() {
		return !negativeLiteral;
	}

	public boolean isNegativeLiteral() {
		return negativeLiteral;
	}

	public AtomicSentence getAtomicSentence() {
		return atom;
	}

	@Override
	public String toString() {
		if (null == strRep) {
			StringBuilder sb = new StringBuilder();
			if (isNegativeLiteral()) {
				sb.append("~");
			}
			sb.append(getAtomicSentence().toString());
			strRep = sb.toString();
		}

		return strRep;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (o.getClass() != getClass()) {
			// This prevents ReducedLiterals
			// being treated as equivalent to
			// normal Literals.
			return false;
		}
		if (!(o instanceof Literal)) {
			return false;
		}
		Literal l = (Literal) o;
		return l.isPositiveLiteral() == isPositiveLiteral()
				&& l.getAtomicSentence().getSymbolicName()
						.equals(atom.getSymbolicName())
				&& l.getAtomicSentence().getArgs().equals(atom.getArgs());
	}

	@Override
	public int hashCode() {
		if (0 == hashCode) {
			hashCode = 17;
			hashCode = 37 * hashCode + (getClass().getSimpleName().hashCode())
					+ (isPositiveLiteral() ? "+".hashCode() : "-".hashCode())
					+ atom.getSymbolicName().hashCode();
			for (Term t : atom.getArgs()) {
				hashCode = 37 * hashCode + t.hashCode();
			}
		}
		return hashCode;
	}

	public Literal substitute(List<Constant> constants){
		List<Term> terms = new ArrayList<>();
		for (int i = 0; i < this.getAtomicSentence().getArgs().size(); i++) {
			if (this.getAtomicSentence().getArgs().get(i) instanceof Variable){
				if (constants.size()>i){
					terms.add(constants.get(i));
				}
			}
		}
		List<Term> toAdd = new ArrayList<>(constants);
		return new Literal(new Predicate(this.getAtomicSentence().getSymbolicName(),toAdd),this.isNegativeLiteral());
	}
}
