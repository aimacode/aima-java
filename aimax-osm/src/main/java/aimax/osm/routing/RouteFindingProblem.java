package aimax.osm.routing;

import aima.core.search.framework.problem.BidirectionalProblem;
import aima.core.search.framework.problem.DefaultGoalTest;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.problem.StepCostFunction;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.entities.MapNode;
import aimax.osm.routing.OsmActionsFunction.OneWayMode;

/**
 * Implements a route finding problem whose representation is directly based on
 * an <code>OsmMap</code>. States are represented by <code>MapNode</code>
 * objects and actions as references to linked nodes. True path lengths (in
 * kilometers) are used as default cost values.
 * 
 * @author Ruediger Lunde
 */
public class RouteFindingProblem extends Problem implements BidirectionalProblem {

	Problem reverseProblem;

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
	public RouteFindingProblem(MapNode from, MapNode to, MapWayFilter filter, boolean ignoreOneWays) {
		this(from, to, filter, ignoreOneWays, new OsmDistanceStepCostFunction());
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
	public RouteFindingProblem(MapNode from, MapNode to, MapWayFilter filter, boolean ignoreOneWays,
			StepCostFunction costs) {
		OneWayMode fMode = ignoreOneWays ? OneWayMode.IGNORE : OneWayMode.TRAVEL_FORWARD;
		OneWayMode rMode = ignoreOneWays ? OneWayMode.IGNORE : OneWayMode.TRAVEL_BACKWARDS;

		initialState = from;
		actionsFunction = new OsmActionsFunction(filter, fMode, to);
		resultFunction = new OsmResultFunction();
		goalTest = new DefaultGoalTest(to);
		stepCostFunction = costs;

		reverseProblem = new Problem(to, new OsmActionsFunction(filter, rMode, from), resultFunction,
				new DefaultGoalTest(from), costs);
	}

	public Problem getOriginalProblem() {
		return this;
	}

	public Problem getReverseProblem() {
		return reverseProblem;
	}
}
