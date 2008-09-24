package aima.logic.fol.kb.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.logic.fol.parsing.ast.Predicate;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): page 280.
 * A First-order definite clause.
 * 
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class DefiniteClause {
	private List<Predicate> premises = new ArrayList<Predicate>();
	private Predicate conclusion;

	public DefiniteClause(List<Predicate> premises, Predicate conclusion) {
		this.premises.addAll(premises);
		this.conclusion = conclusion;
	}

	public boolean isAtomic() {
		return premises.size() == 0;
	}

	public boolean isImplication() {
		return premises.size() > 0;
	}

	public List<Predicate> getPremises() {
		return Collections.unmodifiableList(premises);
	}
	
	public Predicate getConclusion() {
		return conclusion;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (premises.size() > 0) {
			for (int i = 0; i < premises.size(); i++) {
				if (i != 0) {
					sb.append(" AND ");
				}
				sb.append(premises.get(i).toString());
			}
			sb.append(" => ");
		}

		sb.append(conclusion.toString());

		return sb.toString();
	}
}
