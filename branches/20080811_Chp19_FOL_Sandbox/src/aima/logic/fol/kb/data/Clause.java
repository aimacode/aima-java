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

	public Clause() {
		// i.e. the empty clause
	}
	
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
	
	public void addPositiveLiteral(Predicate literal) {
		positiveLiterals.add(literal);
	}

	public List<Predicate> getPositiveLiterals() {
		return Collections.unmodifiableList(positiveLiterals);
	}
	
	public void addNegativeLiteral(Predicate literal) {
		negativeLiterals.add(literal);
	}
	
	public List<Predicate> getNegativeLiterals() {
		return Collections.unmodifiableList(negativeLiterals);
	}	
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		List<Predicate> literals = new ArrayList<Predicate>();
		literals.addAll(negativeLiterals);
		literals.addAll(positiveLiterals);
				
		for (int i = 0; i < literals.size(); i++) {
			if (i > 0) {
				sb.append(" OR ");
			}
			if (i < negativeLiterals.size()) {
				sb.append("NOT(");
			}
			
			sb.append(literals.get(i).toString());
			
			if (i < negativeLiterals.size()) {
				sb.append(")");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
}