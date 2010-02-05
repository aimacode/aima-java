package aimax.osm.routing;

import java.util.Collection;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.informed.AStarSearch;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.MapWayAttFilter;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;

/**
 * Implements a search engine for shortest path calculations.
 * @author R. Lunde
 */
public class RouteCalculator {
	
	public final static String ROUTE_TRACK_NAME = "Route";
	
	/**
	 * Template method, responsible for shortest path generation between
	 * two map nodes. It searches for way nodes in the vicinity of the
	 * given nodes which comply with the specified way selection, searches
	 * for a suitable paths, and adds the paths as tracks to the provided
	 * <code>mapData</code>. The three factory methods can be used to
	 * override aspects of the default behavior in subclasses if needed.
	 * @param locs Nodes, not necessarily way nodes. The first node is used
	 *        as start, last node as finish, all others as via nodes.
	 * @param mapData The information source.
	 * @param waySelection Number, indicating which kinds of ways are relevant.
	 */
	public void calculateRoute(List<MapNode> locs, MapDataStore mapData,
			int waySelection) {
		try {
			MapWayFilter wayFilter = createMapWayFilter(mapData, waySelection);
			boolean ignoreOneways = (waySelection == 0);
			Collection<MapNode> rNodes = mapData.getWayNodes();
			MapNode fromRNode = new Position(locs.get(0)).selectNearest(rNodes, wayFilter);
			mapData.clearTrack(ROUTE_TRACK_NAME);
			mapData.addToTrack(ROUTE_TRACK_NAME, fromRNode.getLat(), fromRNode.getLon());
			for (int i = 1; i < locs.size(); i++) {
				MapNode toRNode = new Position(locs.get(i)).selectNearest(rNodes, wayFilter);
				HeuristicFunction hf = createHeuristicFunction(toRNode, waySelection);
				Problem problem = createProblem
				(fromRNode, toRNode, mapData, wayFilter, ignoreOneways, waySelection);
				Search search = new AStarSearch(new GraphSearch(), hf);
				List<Action> actions = search.search(problem);
				mapData.addToTrack(ROUTE_TRACK_NAME, fromRNode.getLat(), fromRNode.getLon());
				for (Object action : actions) {
					OsmMoveAction a = (OsmMoveAction) action;
					for (MapNode node : a.getNodes())
						if (!node.equals(a.getFrom()))
							mapData.addToTrack
							(ROUTE_TRACK_NAME, node.getLat(), node.getLon());
				}
				fromRNode = toRNode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Factory method, responsible for way filter creation. */
	protected MapWayFilter createMapWayFilter(MapDataStore mapData, int waySelection) {
		if (waySelection == 1)
			return MapWayAttFilter.createCarWayFilter(mapData);
		else if (waySelection == 2)
			return MapWayAttFilter.createBicycleWayFilter(mapData);
		else
			return MapWayAttFilter.createAnyWayFilter(mapData);
	}
	
	/** Factory method, responsible for heuristic function creation. */
	protected HeuristicFunction createHeuristicFunction(MapNode toRNode, int waySelection) {
		return new OsmSldHeuristicFunction(toRNode);
	}
	
	/** Factory method, responsible for problem creation. */
	protected Problem createProblem(MapNode fromRNode, MapNode toRNode,
			MapDataStore mapData, MapWayFilter wayFilter, boolean ignoreOneways,
			int waySelection) {
		return new RouteFindingProblem
		(fromRNode, toRNode, mapData, wayFilter, ignoreOneways);
	}
}
