package aimax.osm.routing;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.WayRef;

/**
 * Generates {@link aimax.osm.routing.OsmMoveAction}s for states which are
 * assumed to be of class {@link aimax.osm.data.entities.MapNode}. If a goal is
 * specified, all generated actions lead to road crossings, road ends, or the
 * specified goal. Otherwise, they lead to directly linked neighbor nodes.
 * @author Ruediger Lunde
 */
public class OsmActionsFunction implements ActionsFunction {

	protected MapWayFilter filter;
	private boolean ignoreOneWays;
	/**
	 * Goal node, possibly null. If a goal is specified, travel actions will
	 * include paths with size greater one.
	 */
	protected MapNode goal;

	public OsmActionsFunction(MapWayFilter filter, boolean ignoreOneWays,
			MapNode goal) {
		this.filter = filter;
		this.ignoreOneWays = ignoreOneWays;
		this.goal = goal;
	}

	/** Expects a <code>MapNode</code> as argument. */
	@Override
	public Set<Action> actions(Object s) {
		Set<Action> result = new LinkedHashSet<Action>();
		MapNode from = (MapNode) s;
		for (WayRef wref : from.getWayRefs()) {
			if (filter == null || filter.isAccepted(wref.getWay())) {
				MapWay way = wref.getWay();
				int nodeIdx = wref.getNodeIdx();
				List<MapNode> wayNodes = way.getNodes();
				MapNode to;
				for (int idx = nodeIdx + 1; idx < wayNodes.size(); idx++) {
					to = wayNodes.get(idx);
					if (goal == null || goal == to
							|| to.getWayRefs().size() > 1
							|| idx == wayNodes.size() - 1) {
						result.add(new OsmMoveAction(way, from, to));
						break;
					}
				}
				if (!way.isOneway() || ignoreOneWays) {
					for (int idx = nodeIdx - 1; idx >= 0; idx--) {
						to = wayNodes.get(idx);
						if (goal == null || goal == to
								|| to.getWayRefs().size() > 1 || idx == 0) {
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
