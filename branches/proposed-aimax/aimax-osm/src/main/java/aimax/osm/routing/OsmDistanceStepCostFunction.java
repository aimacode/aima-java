package aimax.osm.routing;

import aima.core.agent.Action;
import aima.core.search.framework.StepCostFunction;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

public class OsmDistanceStepCostFunction implements StepCostFunction {
	@Override
	public double c(Object s, Action a, Object prime) {
		MapNode from = (MapNode) s;
		MapNode to = (MapNode) prime;
		return (new Position(from)).getDistKM(to);
	}
}
