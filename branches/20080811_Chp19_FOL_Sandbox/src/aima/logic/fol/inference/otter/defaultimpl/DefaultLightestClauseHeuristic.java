package aima.logic.fol.inference.otter.defaultimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import aima.logic.fol.inference.otter.LightestClauseHeuristic;
import aima.logic.fol.kb.data.Clause;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class DefaultLightestClauseHeuristic implements LightestClauseHeuristic {

	private List<Clause> sos = new ArrayList<Clause>();
	private LightestClauseSorter lightestClauseSorter = new LightestClauseSorter();

	public DefaultLightestClauseHeuristic() {

	}

	//
	// START-LightestClauseHeuristic
	public Clause getLightestClause() {
		Clause lightest = null;

		if (sos.size() > 0) {
			lightest = sos.get(0);
		}

		return lightest;
	}

	public void initialSOS(Set<Clause> clauses) {
		sos.addAll(clauses);
		Collections.sort(sos, lightestClauseSorter);
	}

	public void addedClauseToSOS(Clause clause) {
		sos.add(clause);
		Collections.sort(sos, lightestClauseSorter);
	}

	public void removedClauseFromSOS(Clause clause) {
		sos.remove(clause);
	}

	// END-LightestClauseHeuristic
	//
}

class LightestClauseSorter implements Comparator<Clause> {
	public int compare(Clause c1, Clause c2) {
		return (new Integer(c1.getNumberLiterals())).compareTo(new Integer(c2
				.getNumberLiterals()));
	}
}
