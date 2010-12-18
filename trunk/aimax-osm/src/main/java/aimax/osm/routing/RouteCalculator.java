package aimax.osm.routing;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.informed.AStarSearch;
import aima.core.util.CancelableThread;
import aimax.osm.data.OsmMap;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

/**
 * Implements a search engine for shortest path calculations.
 * 
 * @author Ruediger Lunde
 */
public class RouteCalculator {

	/** Returns the names of all supported way selection options. */
	public String[] getWaySelectionOptions() {
		return new String[] { "Distance", "Distance (Car)", "Distance (Bike)" };
	}

	/**
	 * Template method, responsible for shortest path generation between two map
	 * nodes. It searches for way nodes in the vicinity of the given nodes which
	 * comply with the specified way selection, searches for a suitable paths,
	 * and adds the paths as tracks to the provided <code>mapData</code>. The
	 * three factory methods can be used to override aspects of the default
	 * behavior in subclasses if needed.
	 * 
	 * @param locs
	 *            Nodes, not necessarily way nodes. The first node is used as
	 *            start, last node as finish, all others as via nodes.
	 * @param mapData
	 *            The information source.
	 * @param waySelection
	 *            Number, indicating which kinds of ways are relevant.
	 */
	public List<Position> calculateRoute(List<MapNode> locs, OsmMap mapData,
			int waySelection) {
		List<Position> result = new ArrayList<Position>();
		try {
			MapWayFilter wayFilter = createMapWayFilter(mapData, waySelection);
			boolean ignoreOneways = (waySelection == 0);
			MapNode fromRNode = mapData.getNearestWayNode(new Position(locs
					.get(0)), wayFilter);
			result.add(new Position(fromRNode.getLat(), fromRNode.getLon()));
			for (int i = 1; i < locs.size()
					&& !CancelableThread.currIsCanceled(); i++) {
				MapNode toRNode = mapData.getNearestWayNode(new Position(locs
						.get(i)), wayFilter);
				HeuristicFunction hf = createHeuristicFunction(toRNode,
						waySelection);
				Problem problem = createProblem(fromRNode, toRNode, mapData,
						wayFilter, ignoreOneways, waySelection);
				Search search = new AStarSearch(new GraphSearch(), hf);
				List<Action> actions = search.search(problem);
				if (actions.isEmpty())
					break;
				for (Object action : actions) {
					if (action instanceof OsmMoveAction) {
						OsmMoveAction a = (OsmMoveAction) action;
						for (MapNode node : a.getNodes())
							if (!node.equals(a.getFrom()))
								result.add(new Position(node.getLat(), node
										.getLon()));
					}
				}
				fromRNode = toRNode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/** Factory method, responsible for way filter creation. */
	protected MapWayFilter createMapWayFilter(OsmMap map, int waySelection) {
		if (waySelection == 1)
			return MapWayAttFilter.createCarWayFilter();
		else if (waySelection == 2)
			return MapWayAttFilter.createBicycleWayFilter();
		else
			return MapWayAttFilter.createAnyWayFilter();
	}

	/** Factory method, responsible for heuristic function creation. */
	protected HeuristicFunction createHeuristicFunction(MapNode toRNode,
			int waySelection) {
		return new OsmSldHeuristicFunction(toRNode);
	}

	/** Factory method, responsible for problem creation. */
	protected Problem createProblem(MapNode fromRNode, MapNode toRNode,
			OsmMap map, MapWayFilter wayFilter, boolean ignoreOneways,
			int waySelection) {
		return new RouteFindingProblem(fromRNode, toRNode, wayFilter,
				ignoreOneways);
	}
}
