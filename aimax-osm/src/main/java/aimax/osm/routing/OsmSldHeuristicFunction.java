package aimax.osm.routing;

import aima.core.search.framework.Node;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

import java.util.function.ToDoubleFunction;

/**
 * Implements the straight-line-distance heuristic.
 * @author Ruediger Lunde
 */
public class OsmSldHeuristicFunction implements ToDoubleFunction<Node<MapNode, OsmMoveAction>> {
	private MapNode goalState;
	
	public OsmSldHeuristicFunction(MapNode goalState) {
		this.goalState = goalState;
	}
	
	/**
	 * Assumes a <code>MapNode</code> as state and returns the
	 * straight-line-distance to the goal in KM.
	 */
	@Override
	public double applyAsDouble(Node<MapNode, OsmMoveAction> node) {
		return (new Position(node.getState())).getDistKM(goalState);
	}
}
