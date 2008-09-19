package aima.logic.fol;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

public class StandardizeApart {
	private VariableCollector variableCollector = null;
	private SubstVisitor substVisitor = null;

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
			Variable v = new Variable(standardizeApartIndexical
					.getPrefix()
					+ standardizeApartIndexical.getNextIndex());
			renameSubstitution.put(var, v);
			reverseSubstitution.put(v, var);
		}

		Sentence standardized = substVisitor.subst(renameSubstitution,
				aSentence);

		return new StandardizeApartResult(aSentence, standardized,
				renameSubstitution, reverseSubstitution);
	}
}
