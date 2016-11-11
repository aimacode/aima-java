package aimax.osm.routing;

import aima.core.search.framework.evalfunc.HeuristicFunction;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

/**
 * Implements the straight-line-distance heuristic.
 * @author Ruediger Lunde
 */
public class OsmSldHeuristicFunction implements HeuristicFunction {
	MapNode goalState;
	
	public OsmSldHeuristicFunction(MapNode goalState) {
		this.goalState = goalState;
	}
	
	/**
	 * Assumes a <code>MapNode</code> as state and returns the
	 * straight-line-distance to the goal in KM.
	 */
	@Override
	public double h(Object s) {
		MapNode currState = (MapNode) s;
		return (new Position(currState)).getDistKM(goalState);
	}
}
