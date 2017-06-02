package aimax.osm.routing;

import aima.core.search.framework.problem.*;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.entities.MapNode;
import aimax.osm.routing.OsmFunctions.OneWayMode;

/**
 * Implements a route finding problem whose representation is directly based on
 * an <code>OsmMap</code>. States are represented by <code>MapNode</code>
 * objects and actions as references to linked nodes. True path lengths (in
 * kilometers) are used as default cost values.
 * 
 * @author Ruediger Lunde
 */
public class RouteFindingProblem extends GeneralProblem<MapNode, OsmMoveAction>
		implements BidirectionalProblem<MapNode, OsmMoveAction> {

	private Problem<MapNode, OsmMoveAction> reverseProblem;

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
		this(from, to, filter, ignoreOneWays, OsmFunctions::getDistanceStepCosts);
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
			StepCostFunction<MapNode, OsmMoveAction> costs) {
		super(from,
				OsmFunctions.createActionFunction
						(filter, ignoreOneWays ? OneWayMode.IGNORE : OneWayMode.TRAVEL_FORWARD, to),
				OsmFunctions::getResult,
				GoalTest.isEqual(to),
				costs);

		reverseProblem = new GeneralProblem<>(to,
				OsmFunctions.createActionFunction
						(filter, ignoreOneWays ? OneWayMode.IGNORE : OneWayMode.TRAVEL_BACKWARDS, from),
				OsmFunctions::getResult,
				GoalTest.isEqual(from),
				costs);
	}

	public Problem<MapNode, OsmMoveAction> getOriginalProblem() {
		return this;
	}

	public Problem<MapNode, OsmMoveAction> getReverseProblem() {
		return reverseProblem;
	}
}
