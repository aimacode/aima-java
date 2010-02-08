package aimax.osm.routing;

import aima.core.agent.Action;
import aima.core.search.framework.StepCostFunction;

public class OsmDistanceStepCostFunction implements StepCostFunction {
	@Override
	public double c(Object s, Action a, Object prime) {
		return ((OsmMoveAction) a).getTravelDistance();
	}
}
