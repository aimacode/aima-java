package aima.core.search.csp;

import java.util.List;
import java.util.Map;

/**
 * @author Ravi Mohan
 * 
 */

public class MapColoringConstraint implements Constraint {
	private Map neighbors;

	MapColoringConstraint(Map neighbors) {
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