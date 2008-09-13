package aima.logic.fol.kb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.DefiniteClauseVisitor;
import aima.logic.fol.FOLDomain;
import aima.logic.fol.Unifier;
import aima.logic.fol.inference.InferenceProcedure;
import aima.logic.fol.kb.data.DefiniteClause;
import aima.logic.fol.parsing.ast.Sentence;

/**
 * A First Order Logic (FOL) Knowledge Base that only accepts Definite Clauses.
 * see: Artificial Intelligence A Modern Approach (2nd Edition): page 280.
 *
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class DefiniteClauseKnowledgeBase extends FOLKnowledgeBase {

	private List<DefiniteClause> allDefiniteClauses = new ArrayList<DefiniteClause>();
	private List<DefiniteClause> implDefiniteClauses = new ArrayList<DefiniteClause>();
	
	private DefiniteClauseVisitor definiteClauseVisitor;
	
	
	public DefiniteClauseKnowledgeBase(FOLDomain domain,
			InferenceProcedure inferenceProcedure) {
		this(domain, inferenceProcedure, new Unifier());
	}

	public DefiniteClauseKnowledgeBase(FOLDomain domain,
			InferenceProcedure inferenceProcedure, Unifier unifier) {
		super(domain, inferenceProcedure, unifier);
		
		definiteClauseVisitor = new DefiniteClauseVisitor(getParser());
	}
	
	public void tell(Sentence aSentence) {
		// TODO: Want to coordinate standardizing apart with calls to the
		// underlying store(), i.e. I want to get the internal representation
		// and then assign it to the definite clause.
		
		// Standardize apart by default
		Sentence standardizedApart = this.standardizeApart(aSentence);

		// Ensure is a definite clause
		DefiniteClause dc = definiteClauseVisitor
				.definiteClause(standardizedApart);
		
		if (null != dc) {
			allDefiniteClauses.add(dc);
			if (dc.isImplication()) {
				implDefiniteClauses.add(dc);
			}
			super.tell(standardizedApart);
		} else {
			throw new IllegalArgumentException(
					"Can only add definite clauses to this Knowledge Base:"
							+ aSentence);
		}
	}
	
	public List<DefiniteClause> getStandardizedApartDefiniteClauses() {
		return Collections.unmodifiableList(allDefiniteClauses);
	}

	public List<DefiniteClause> getStandardizedApartImplications() {
		return Collections.unmodifiableList(implDefiniteClauses);
	}
}
