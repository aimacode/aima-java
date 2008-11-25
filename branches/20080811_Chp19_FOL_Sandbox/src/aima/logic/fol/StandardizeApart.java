package aima.logic.fol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.data.Chain;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
import aima.logic.fol.parsing.ast.AtomicSentence;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

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

	// Note: see page 277.
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

		List<Literal> literals = new ArrayList<Literal>();

		for (Literal l : clause.getLiterals()) {
			literals.add(substVisitor.subst(renameSubstitution, l));
		}

		return new Clause(literals);
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

		List<Literal> lits = new ArrayList<Literal>();

		for (Literal l : chain.getLiterals()) {
			AtomicSentence atom = (AtomicSentence) substVisitor.subst(
					renameSubstitution, l.getAtomicSentence());
			lits.add(l.newInstance(atom));
		}

		return new Chain(lits);
	}
	
	public void standardizeApart(List<Literal> positiveLiterals,
			List<Literal> negativeLiterals,
			StandardizeApartIndexical standardizeApartIndexical) {
		Set<Variable> toRename = new HashSet<Variable>();

		for (Literal pl : positiveLiterals) {
			toRename.addAll(variableCollector.collectAllVariables(pl
					.getAtomicSentence()));
		}
		for (Literal nl : negativeLiterals) {
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

		for (Literal pl : positiveLiterals) {
			posLits.add(substVisitor.subst(renameSubstitution, pl));
		}
		for (Literal nl : negativeLiterals) {
			negLits.add(substVisitor.subst(renameSubstitution, nl));
		}

		positiveLiterals.clear();
		positiveLiterals.addAll(posLits);
		negativeLiterals.clear();
		negativeLiterals.addAll(negLits);
	}	
}
