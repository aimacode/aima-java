package aimax.osm.routing;

import aima.core.search.framework.Problem;
import aima.core.search.framework.StepCostFunction;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.entities.MapNode;

/**
 * Implements a route finding problem whose representation is
 * directly based on an <code>MapDataStore</code> object. States
 * are represented by <code>MapNode</code> objects and actions
 * as references to linked nodes. True path lengths
 * (in kilometers) are used as default cost values.
 * @author R. Lunde
 */
public class RouteFindingProblem extends Problem {
	/**
	 * Creates a new route planning problem.
	 * @param from A way node complying to the filter.
	 * @param to A way node complying to the filter.
	 * @param mapData The map representation.
	 * @param filter A filter for ways constraining routing results.
	 */
	public RouteFindingProblem(MapNode from, MapNode to,
			MapDataStore mapData, MapWayFilter filter, boolean ignoreOneWays) {
		initialState = from;
		actionsFunction = new OsmActionsFunction(mapData, filter, ignoreOneWays);
		resultFunction = new OsmResultFunction();
		goalTest = new SimpleGoalTest(to);
		stepCostFunction = new OsmDistanceStepCostFunction();
	}
	
	/**
	 * Creates a new route planning problem.
	 * @param from A way node complying to the filter.
	 * @param to A way node complying to the filter.
	 * @param mapData The map representation.
	 * @param filter A filter for ways constraining routing results.
	 * @param setCostFunction Maps <code>OsmMoveAction</code>s to costs.
	 */
	public RouteFindingProblem(MapNode from, MapNode to,
			MapDataStore mapData, MapWayFilter filter, boolean ignoreOneWays,
			StepCostFunction costs) {
		initialState = from;
		actionsFunction = new OsmActionsFunction(mapData, filter, ignoreOneWays);
		resultFunction = new OsmResultFunction();
		goalTest = new SimpleGoalTest(to);
		stepCostFunction = costs;
	}
}
