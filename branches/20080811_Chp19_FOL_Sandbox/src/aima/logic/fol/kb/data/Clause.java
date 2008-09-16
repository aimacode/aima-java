package aima.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.parsing.ast.Predicate;

/**
 * A Clause: A disjunction of literals.
 * 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class Clause {
	
	private final List<Predicate> positiveLiterals = new ArrayList<Predicate>();
	private final List<Predicate> negativeLiterals = new ArrayList<Predicate>();

	public Clause(List<Predicate> positiveLiterals,
			List<Predicate> negativeLiterals) {
		this.positiveLiterals.addAll(positiveLiterals);
		this.negativeLiterals.addAll(negativeLiterals);
	}

	public boolean isHornClause() {
		// A Horn clause is a disjunction of literals of which at most one is
		// positive.
		return positiveLiterals.size() <= 1;
	}

	public boolean isDefiniteClause() {
		// A Definite Clause is a disjunction of literals of which exactly 1 is
		// positive.
		return positiveLiterals.size() == 1;
	}

	public List<Predicate> getPositiveLiterals() {
		return Collections.unmodifiableList(positiveLiterals);
	}
	
	public List<Predicate> getNegativeDisjuncts() {
		return Collections.unmodifiableList(negativeLiterals);
	}	
}