package aimax.osm.routing;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.MapNode.WayRef;

/**
 * Generates {@link aimax.osm.routing.OsmMoveAction}s for states which are
 * assumed to be of class {@link aimax.osm.data.entities.MapNode}. If a goal
 * is specified, all generated actions lead to road crossings, road ends, or
 * the specified goal. Otherwise, they lead to directly linked neighbor nodes.
 * @author R. Lunde
 *
 */
class OsmActionsFunction implements ActionsFunction {
	private MapDataStore mapData;
	private MapWayFilter filter;
	private boolean ignoreOneWays;
	/**
	 * Goal node, possibly null. If a goal is specified, travel
	 * actions will include paths with size greater one.
	 */
	private MapNode goal;
	
	public OsmActionsFunction(MapDataStore mapData, MapWayFilter filter, boolean ignoreOneWays,
			MapNode goal) {
		this.mapData = mapData;
		this.filter = filter;
		this.ignoreOneWays = ignoreOneWays;
		this.goal = goal;
	}
	
	@Override
	public Set<Action> actions(Object s) {
		Set<Action> result = new LinkedHashSet<Action>();
		MapNode from = (MapNode) s;
		for (WayRef wref : from.getWays()) {
			if (filter == null || filter.isAccepted(wref.wayId)) {
				MapWay way = mapData.getWay(wref.wayId);
				int nodeIdx = wref.nodeIdx;
				List<MapNode> wayNodes = way.getNodes();
				MapNode to;
				for (int idx = nodeIdx+1; idx < wayNodes.size(); idx++) {
					to = wayNodes.get(idx);
					if (goal == null || goal == to ||
							to.getWays().size() > 1 || idx == wayNodes.size()-1) {
						result.add(new OsmMoveAction(way, from, to));
						break;
					}
				}
				if (!way.isOneway() || ignoreOneWays) {
					for (int idx = nodeIdx-1; idx >= 0; idx--) {
						to = wayNodes.get(idx);
						if (goal == null || goal == to ||
								to.getWays().size() > 1 || idx == 0 ) {
							result.add(new OsmMoveAction(way, from, to));
							break;
						}
					}
				}
			}
		}
		return result;
	}
}
