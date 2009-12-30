package aimax.osm.routing;

import aima.core.search.framework.HeuristicFunction;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

class OsmSldHeuristicFunction implements HeuristicFunction {
	MapNode goalState;
	
	public OsmSldHeuristicFunction(MapNode goalState) {
		this.goalState = goalState;
	}
	
	@Override
	public double h(Object s) {
		MapNode currState = (MapNode) s;
		return (new Position(currState)).getDistKM(goalState);
	}
}
