package aima.core.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import aima.core.logic.fol.inference.proof.ProofStep;
import aima.core.logic.fol.inference.proof.ProofStepChainContrapositive;
import aima.core.logic.fol.inference.proof.ProofStepPremise;

/**
 * @see http://logic.stanford.edu/classes/cs157/2008/lectures/lecture13.pdf
 * 
 * A Chain is a sequence of literals (while a clause is a set) - order is important for a chain.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Chain {
	private static List<Literal> _emptyLiteralsList = Collections
			.unmodifiableList(new ArrayList<Literal>());
	//
	private List<Literal> literals = new ArrayList<Literal>();
	private ProofStep proofStep = null;

	public Chain() {
		// i.e. the empty chain
	}

	public Chain(List<Literal> literals) {
		this.literals.addAll(literals);
	}

	public Chain(Set<Literal> literals) {
		this.literals.addAll(literals);
	}

	public ProofStep getProofStep() {
		if (null == proofStep) {
			// Assume was a premise
			proofStep = new ProofStepPremise(this);
		}
		return proofStep;
	}

	public void setProofStep(ProofStep proofStep) {
		this.proofStep = proofStep;
	}

	public boolean isEmpty() {
		return literals.size() == 0;
	}

	public void addLiteral(Literal literal) {
		literals.add(literal);
	}

	public Literal getHead() {
		if (0 == literals.size()) {
			return null;
		}
		return literals.get(0);
	}

	public List<Literal> getTail() {
		if (0 == literals.size()) {
			return _emptyLiteralsList;
		}
		return Collections.unmodifiableList(literals
				.subList(1, literals.size()));
	}

	public int getNumberLiterals() {
		return literals.size();
	}

	public List<Literal> getLiterals() {
		return Collections.unmodifiableList(literals);
	}

	/**
	 * A contrapositive of a chain is a permutation in which a different literal
	 * is placed at the front. The contrapositives of a chain are logically
	 * equivalent to the original chain.
	 * 
	 * @return a list of contrapositives for this chain.
	 */
	public List<Chain> getContrapositives() {
		List<Chain> contrapositives = new ArrayList<Chain>();
		List<Literal> lits = new ArrayList<Literal>();

		for (int i = 1; i < literals.size(); i++) {
			lits.clear();
			lits.add(literals.get(i));
			lits.addAll(literals.subList(0, i));
			lits.addAll(literals.subList(i + 1, literals.size()));
			Chain cont = new Chain(lits);
			cont.setProofStep(new ProofStepChainContrapositive(cont, this));
			contrapositives.add(cont);
		}

		return contrapositives;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<");

		for (int i = 0; i < literals.size(); i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append(literals.get(i).toString());
		}

		sb.append(">");

		return sb.toString();
	}
}
