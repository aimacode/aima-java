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
import aima.logic.fol.parsing.ast.Predicate;
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

		List<Predicate> posLits = new ArrayList<Predicate>();
		List<Predicate> negLits = new ArrayList<Predicate>();

		for (Predicate pl : clause.getPositiveLiterals()) {
			posLits.add((Predicate) substVisitor.subst(renameSubstitution, pl));
		}
		for (Predicate nl : clause.getNegativeLiterals()) {
			negLits.add((Predicate) substVisitor.subst(renameSubstitution, nl));
		}

		return new Clause(posLits, negLits);
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
			Predicate p = (Predicate) substVisitor.subst(renameSubstitution, l
					.getPredicate());
			lits.add(l.newInstance(p));
		}

		return new Chain(lits);
	}
	
	public void standardizeApart(List<Predicate> positiveLiterals,
			List<Predicate> negativeLiterals,
			StandardizeApartIndexical standardizeApartIndexical) {
		Set<Variable> toRename = new HashSet<Variable>();

		for (Predicate pl : positiveLiterals) {
			toRename.addAll(variableCollector.collectAllVariables(pl));
		}
		for (Predicate nl : negativeLiterals) {
			toRename.addAll(variableCollector.collectAllVariables(nl));
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

		List<Predicate> posLits = new ArrayList<Predicate>();
		List<Predicate> negLits = new ArrayList<Predicate>();

		for (Predicate pl : positiveLiterals) {
			posLits.add((Predicate) substVisitor.subst(renameSubstitution, pl));
		}
		for (Predicate nl : negativeLiterals) {
			negLits.add((Predicate) substVisitor.subst(renameSubstitution, nl));
		}

		positiveLiterals.clear();
		positiveLiterals.addAll(posLits);
		negativeLiterals.clear();
		negativeLiterals.addAll(negLits);
	}	
}
