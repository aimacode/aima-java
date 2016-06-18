package aimax.osm.routing;

import aima.core.agent.Action;
import aima.core.search.framework.problem.StepCostFunction;

/**
 * Assumes actions of type <code>OsmMoveAction<code> and gets the
 * corresponding travel distance.
 * @author Ruediger Lunde
 */
public class OsmDistanceStepCostFunction implements StepCostFunction {
	@Override
	public double c(Object s, Action a, Object prime) {
		return ((OsmMoveAction) a).getTravelDistance();
	}
}
