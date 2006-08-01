/*
 * Created on Sep 21, 2004
 *
 */
package aima.search.csp;

import java.util.Hashtable;
import java.util.List;

/**
 * @author Ravi Mohan
 *  
 */
public class MapColoringConstraint implements Constraint {
	private Hashtable neighbors;

	MapColoringConstraint(Hashtable neighbors) {
		this.neighbors = neighbors;
	}

	public boolean isSatisfiedWith(Assignment assignment, String variable,
			Object value) {
		List variableNeighbors = (List) neighbors.get(variable);
		for (int i = 0; i < variableNeighbors.size(); i++) {
			String neighbor = (String) variableNeighbors.get(i);
			if (assignment.hasAssignmentFor(neighbor)) {
				String neighborColor = (String) assignment
						.getAssignment(neighbor);
				if (neighborColor.equals(value)) {

					return false;
				}
			}
		}
		return true;
	}

}