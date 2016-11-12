package aimax.osm.routing;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.util.CancelableThread;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

/**
 * Implements a search engine for shortest path calculations. Modified versions
 * can be implemented quite easily by overriding the various factory methods.
 * 
 * @author Ruediger Lunde
 */
public class RouteCalculator {

	/** Returns the names of all supported way selection options. */
	public String[] getTaskSelectionOptions() {
		return new String[] { "Distance", "Distance (Car)", "Distance (Bike)" };
	}

	/**
	 * Template method, responsible for shortest path generation between two map
	 * nodes. It searches for way nodes in the vicinity of the given nodes which
	 * comply with the specified way selection, searches for a suitable paths,
	 * and adds the paths as tracks to the provided <code>map</code>. Various
	 * factory methods can be used to override aspects of the default behavior
	 * in subclasses if needed.
	 * 
	 * @param markers
	 *            Nodes, not necessarily way nodes. The first node is used as
	 *            start, last node as finish, all others as via nodes.
	 * @param map
	 *            The information source.
	 * @param taskSelection
	 *            Number, indicating which kinds of ways are relevant.
	 */
	public List<Position> calculateRoute(List<MapNode> markers, OsmMap map,
			int taskSelection) {
		List<Position> result = new ArrayList<Position>();
		try {
			MapWayFilter wayFilter = createMapWayFilter(map, taskSelection);
			boolean ignoreOneways = (taskSelection == 0);
			List<MapNode[]> pNodeList = subdivideProblem(markers, map, wayFilter);
			for (int i = 0; i < pNodeList.size()
					&& !CancelableThread.currIsCanceled(); i++) {
				Problem problem = createProblem(pNodeList.get(i), map, wayFilter,
						ignoreOneways, taskSelection);
				HeuristicFunction hf = createHeuristicFunction(pNodeList.get(i),
						taskSelection);
				SearchForActions search = createSearch(hf, taskSelection);
				List<Action> actions = search.findActions(problem);
				if (actions.isEmpty())
					break;
				for (Object action : actions) {
					if (action instanceof OsmMoveAction) {
						OsmMoveAction a = (OsmMoveAction) action;
						for (MapNode node : a.getNodes())
							if (result.isEmpty()
									|| result.get(result.size() - 1) != node)
								result.add(new Position(node.getLat(), node
										.getLon()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/** Factory method, responsible for way filter creation. */
	protected MapWayFilter createMapWayFilter(OsmMap map, int taskSelection) {
		if (taskSelection == 1)
			return MapWayAttFilter.createCarWayFilter();
		else if (taskSelection == 2)
			return MapWayAttFilter.createBicycleWayFilter();
		else
			return MapWayAttFilter.createAnyWayFilter();
	}

	/**
	 * Factory method, responsible for subdividing the overall problem which is
	 * specified by a list of marker nodes. It returns arrays of way nodes.
	 * The arrays will be used to define problems to be solved one after
	 * another. This implementation returns pairs of from and to way nodes.
	 */
	protected List<MapNode[]> subdivideProblem(List<MapNode> markers,
			OsmMap map, MapWayFilter wayFilter) {
		List<MapNode[]> result = new ArrayList<MapNode[]>();
		MapNode fromNode = map.getNearestWayNode(new Position(markers.get(0)),
				wayFilter);
		for (int i = 1; i < markers.size(); i++) {
			MapNode toNode = map.getNearestWayNode(
					new Position(markers.get(i)), wayFilter);
			result.add(new MapNode[] { fromNode, toNode });
			fromNode = toNode;
		}
		return result;
	}

	/** Factory method, responsible for problem creation. */
	protected Problem createProblem(MapNode[] pNodes, OsmMap map,
			MapWayFilter wayFilter, boolean ignoreOneways, int taskSelection) {
		return new RouteFindingProblem(pNodes[0], pNodes[1], wayFilter,
				ignoreOneways);
	}

	/** Factory method, responsible for heuristic function creation. */
	protected HeuristicFunction createHeuristicFunction(MapNode[] pNodes,
			int taskSelection) {
		return new OsmSldHeuristicFunction(pNodes[1]);
	}
	
	/** Factory method, responsible for search creation. */
	protected SearchForActions createSearch(HeuristicFunction hf, int taskSelection) {
		return new AStarSearch(new GraphSearch(), hf);
	}
}
