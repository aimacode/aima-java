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

class OsmActionsFunction implements ActionsFunction {
	private MapDataStore mapData;
	private MapWayFilter filter;
	private boolean ignoreOneWays;
	
	public OsmActionsFunction(MapDataStore mapData, MapWayFilter filter, boolean ignoreOneWays) {
		this.mapData = mapData;
		this.filter = filter;
		this.ignoreOneWays = ignoreOneWays;
	}
	
	@Override
	public Set<Action> actions(Object s) {
		Set<Action> result = new LinkedHashSet<Action>();
		MapNode node = (MapNode) s;
		for (WayRef wref : node.getWays()) {
			if (filter == null || filter.isAccepted(wref.wayId)) {
				MapWay way = mapData.getWay(wref.wayId);
				int nodeIdx = wref.nodeIdx;
				List<MapNode> wayNodes = way.getNodes();
				MapNode next;
				if (wayNodes.size() > nodeIdx+1) {
					next = wayNodes.get(nodeIdx+1);
					result.add(new OsmMoveAction(way, next));
				}
				if (nodeIdx > 0 && (!way.isOneway() || ignoreOneWays)) {
					next = wayNodes.get(nodeIdx-1);
					result.add(new OsmMoveAction(way, next));
				}
			}
		}
		return result;
	}
}
