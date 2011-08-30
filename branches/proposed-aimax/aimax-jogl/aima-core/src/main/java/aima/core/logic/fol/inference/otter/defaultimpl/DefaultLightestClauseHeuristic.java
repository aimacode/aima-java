package aima.core.logic.fol.inference.otter.defaultimpl;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import aima.core.logic.fol.inference.otter.LightestClauseHeuristic;
import aima.core.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class DefaultLightestClauseHeuristic implements LightestClauseHeuristic {

	private LightestClauseSorter c = new LightestClauseSorter();
	private SortedSet<Clause> sos = new TreeSet<Clause>(c);

	public DefaultLightestClauseHeuristic() {

	}

	//
	// START-LightestClauseHeuristic
	public Clause getLightestClause() {
		Clause lightest = null;

		if (sos.size() > 0) {
			lightest = sos.first();
		}

		return lightest;
	}

	public void initialSOS(Set<Clause> clauses) {
		sos.clear();
		sos.addAll(clauses);
	}

	public void addedClauseToSOS(Clause clause) {
		sos.add(clause);
	}

	public void removedClauseFromSOS(Clause clause) {
		sos.remove(clause);
	}

	// END-LightestClauseHeuristic
	//
}

class LightestClauseSorter implements Comparator<Clause> {
	public int compare(Clause c1, Clause c2) {
		if (c1 == c2) {
			return 0;
		}
		int c1Val = c1.getNumberLiterals();
		int c2Val = c2.getNumberLiterals();
		return (c1Val < c2Val ? -1
				: (c1Val == c2Val ? (compareEqualityIdentities(c1, c2)) : 1));
	}

	private int compareEqualityIdentities(Clause c1, Clause c2) {
		int c1Len = c1.getEqualityIdentity().length();
		int c2Len = c2.getEqualityIdentity().length();

		return (c1Len < c2Len ? -1 : (c1Len == c2Len ? c1.getEqualityIdentity()
				.compareTo(c2.getEqualityIdentity()) : 1));
	}
}