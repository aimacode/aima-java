package aima.core.logic.fol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.inference.proof.ProofStepRenaming;
import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class StandardizeApart {
	private VariableCollector variableCollector = null;
	private SubstVisitor substVisitor = null;

	public StandardizeApart() {
		variableCollector = new VariableCollector();
		substVisitor = new SubstVisitor();
	}

	public StandardizeApart(VariableCollector variableCollector,
			SubstVisitor substVisitor) {
		this.variableCollector = variableCollector;
		this.substVisitor = substVisitor;
	}

	// Note: see page 327.
	public StandardizeApartResult standardizeApart(Sentence aSentence,
			StandardizeApartIndexical standardizeApartIndexical) {
		Set<Variable> toRename = variableCollector
				.collectAllVariables(aSentence);
		Map<Variable, Term> renameSubstitution = new HashMap<Variable, Term>();
		Map<Variable, Term> reverseSubstitution = new HashMap<Variable, Term>();

		for (Variable var : toRename) {
			Variable v = null;
			do {
				v = new Variable(standardizeApartIndexical.getPrefix()
						+ standardizeApartIndexical.getNextIndex());
				// Ensure the new variable name is not already
				// accidentally used in the sentence
			} while (toRename.contains(v));

			renameSubstitution.put(var, v);
			reverseSubstitution.put(v, var);
		}

		Sentence standardized = substVisitor.subst(renameSubstitution,
				aSentence);

		return new StandardizeApartResult(aSentence, standardized,
				renameSubstitution, reverseSubstitution);
	}

	public Clause standardizeApart(Clause clause,
			StandardizeApartIndexical standardizeApartIndexical) {

		Set<Variable> toRename = variableCollector.collectAllVariables(clause);
		Map<Variable, Term> renameSubstitution = new HashMap<Variable, Term>();

		for (Variable var : toRename) {
			Variable v = null;
			do {
				v = new Variable(standardizeApartIndexical.getPrefix()
						+ standardizeApartIndexical.getNextIndex());
				// Ensure the new variable name is not already
				// accidentally used in the sentence
			} while (toRename.contains(v));

			renameSubstitution.put(var, v);
		}

		if (renameSubstitution.size() > 0) {
			List<Literal> literals = new ArrayList<Literal>();

			for (Literal l : clause.getLiterals()) {
				literals.add(substVisitor.subst(renameSubstitution, l));
			}
			Clause renamed = new Clause(literals);
			renamed.setProofStep(new ProofStepRenaming(renamed, clause
					.getProofStep()));
			return renamed;
		}

		return clause;
	}

	public Chain standardizeApart(Chain chain,
			StandardizeApartIndexical standardizeApartIndexical) {

		Set<Variable> toRename = variableCollector.collectAllVariables(chain);
		Map<Variable, Term> renameSubstitution = new HashMap<Variable, Term>();

		for (Variable var : toRename) {
			Variable v = null;
			do {
				v = new Variable(standardizeApartIndexical.getPrefix()
						+ standardizeApartIndexical.getNextIndex());
				// Ensure the new variable name is not already
				// accidentally used in the sentence
			} while (toRename.contains(v));

			renameSubstitution.put(var, v);
		}

		if (renameSubstitution.size() > 0) {
			List<Literal> lits = new ArrayList<Literal>();

			for (Literal l : chain.getLiterals()) {
				AtomicSentence atom = (AtomicSentence) substVisitor.subst(
						renameSubstitution, l.getAtomicSentence());
				lits.add(l.newInstance(atom));
			}

			Chain renamed = new Chain(lits);

			renamed.setProofStep(new ProofStepRenaming(renamed, chain
					.getProofStep()));

			return renamed;
		}

		return chain;
	}

	public Map<Variable, Term> standardizeApart(List<Literal> l1Literals,
			List<Literal> l2Literals,
			StandardizeApartIndexical standardizeApartIndexical) {
		Set<Variable> toRename = new HashSet<Variable>();

		for (Literal pl : l1Literals) {
			toRename.addAll(variableCollector.collectAllVariables(pl
					.getAtomicSentence()));
		}
		for (Literal nl : l2Literals) {
			toRename.addAll(variableCollector.collectAllVariables(nl
					.getAtomicSentence()));
		}

		Map<Variable, Term> renameSubstitution = new HashMap<Variable, Term>();

		for (Variable var : toRename) {
			Variable v = null;
			do {
				v = new Variable(standardizeApartIndexical.getPrefix()
						+ standardizeApartIndexical.getNextIndex());
				// Ensure the new variable name is not already
				// accidentally used in the sentence
			} while (toRename.contains(v));

			renameSubstitution.put(var, v);
		}

		List<Literal> posLits = new ArrayList<Literal>();
		List<Literal> negLits = new ArrayList<Literal>();

		for (Literal pl : l1Literals) {
			posLits.add(substVisitor.subst(renameSubstitution, pl));
		}
		for (Literal nl : l2Literals) {
			negLits.add(substVisitor.subst(renameSubstitution, nl));
		}

		l1Literals.clear();
		l1Literals.addAll(posLits);
		l2Literals.clear();
		l2Literals.addAll(negLits);

		return renameSubstitution;
	}
}
