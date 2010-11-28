package aimax.osm.routing;

import aima.core.search.framework.DefaultGoalTest;
import aima.core.search.framework.Problem;
import aima.core.search.framework.StepCostFunction;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.entities.MapNode;

/**
 * Implements a route finding problem whose representation is directly based on
 * an <code>MapDataStore</code> object. States are represented by
 * <code>MapNode</code> objects and actions as references to linked nodes. True
 * path lengths (in kilometers) are used as default cost values.
 * 
 * @author Ruediger Lunde
 */
public class RouteFindingProblem extends Problem {
	/**
	 * Creates a new route planning problem.
	 * 
	 * @param from
	 *            A way node complying to the filter.
	 * @param to
	 *            A way node complying to the filter.
	 * @param filter
	 *            A filter for ways constraining routing results.
	 */
	public RouteFindingProblem(MapNode from, MapNode to, MapWayFilter filter,
			boolean ignoreOneWays) {
		initialState = from;
		actionsFunction = new OsmActionsFunction(filter, ignoreOneWays, to);
		resultFunction = new OsmResultFunction();
		goalTest = new DefaultGoalTest(to);
		stepCostFunction = new OsmDistanceStepCostFunction();
	}

	/**
	 * Creates a new route planning problem.
	 * 
	 * @param from
	 *            A way node complying to the filter.
	 * @param to
	 *            A way node complying to the filter.
	 * @param filter
	 *            A filter for ways constraining routing results.
	 * @param costs
	 *            Maps <code>OsmMoveAction</code>s to costs.
	 */
	public RouteFindingProblem(MapNode from, MapNode to, MapWayFilter filter,
			boolean ignoreOneWays, StepCostFunction costs) {
		initialState = from;
		actionsFunction = new OsmActionsFunction(filter, ignoreOneWays, to);
		resultFunction = new OsmResultFunction();
		goalTest = new DefaultGoalTest(to);
		stepCostFunction = costs;
	}
}
