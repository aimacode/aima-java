package aimax.osm.routing;

import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

import java.util.function.Function;

/**
 * Implements the straight-line-distance heuristic.
 * @author Ruediger Lunde
 */
public class OsmSldHeuristicFunction implements Function<Object, Double> {
	MapNode goalState;
	
	public OsmSldHeuristicFunction(MapNode goalState) {
		this.goalState = goalState;
	}
	
	/**
	 * Assumes a <code>MapNode</code> as state and returns the
	 * straight-line-distance to the goal in KM.
	 */
	@Override
	public Double apply(Object s) {
		MapNode currState = (MapNode) s;
		return (new Position(currState)).getDistKM(goalState);
	}
}
