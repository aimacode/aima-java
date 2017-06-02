package aimax.osm.routing;

import aima.core.search.framework.problem.ActionsFunction;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.WayRef;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains useful functions for OSM routing problems.
 *
 * @author Ruediger Lunde
 */
public class OsmFunctions {

    public enum OneWayMode {IGNORE, TRAVEL_FORWARD, TRAVEL_BACKWARDS}


    public static ActionsFunction<MapNode, OsmMoveAction> createActionFunction
            (MapWayFilter filter, OneWayMode oneWayMode, MapNode goal) {
        return new OsmActionsFunction(filter, oneWayMode, goal);
    }

    public static MapNode getResult(MapNode state, OsmMoveAction action) {
        return action.getTo();
    }

    public static double getDistanceStepCosts(MapNode state, OsmMoveAction action, MapNode statePrimed) {
        return action.getTravelDistance();
    }


    /**
     * Generates {@link aimax.osm.routing.OsmMoveAction}s for states. If a goal is
     * specified, all generated actions lead to road crossings, road ends, or the
     * specified goal. Otherwise, they lead to directly linked neighbor nodes.
     */
    public static class OsmActionsFunction implements ActionsFunction<MapNode, OsmMoveAction> {

        protected MapWayFilter filter;
        private OneWayMode oneWayMode;
        /**
         * Goal node, possibly null. If a goal is specified, travel actions will
         * include paths with size greater one.
         */
        protected MapNode goal;

        public OsmActionsFunction(MapWayFilter filter, OneWayMode oneWayMode, MapNode goal) {
            this.filter = filter;
            this.oneWayMode = oneWayMode;
            this.goal = goal;
        }

        @Override
        public List<OsmMoveAction> apply(MapNode state) {
            List<OsmMoveAction> result = new ArrayList<>();
            MapNode from = (MapNode) state;
            for (WayRef wref : from.getWayRefs()) {
                if (filter == null || filter.isAccepted(wref.getWay())) {
                    MapWay way = wref.getWay();
                    int nodeIdx = wref.getNodeIdx();
                    List<MapNode> wayNodes = way.getNodes();
                    MapNode to;
                    if (oneWayMode != OneWayMode.TRAVEL_BACKWARDS || !way.isOneway())
                        for (int idx = nodeIdx + 1; idx < wayNodes.size(); idx++) {
                            to = wayNodes.get(idx);
                            if (goal == null || goal == to
                                    || to.getWayRefs().size() > 1
                                    || idx == wayNodes.size() - 1) {
                                result.add(new OsmMoveAction(way, nodeIdx, idx));
                                break;
                            }
                        }
                    if (oneWayMode != OneWayMode.TRAVEL_FORWARD || !way.isOneway()) {
                        for (int idx = nodeIdx - 1; idx >= 0; idx--) {
                            to = wayNodes.get(idx);
                            if (goal == null || goal == to
                                    || to.getWayRefs().size() > 1 || idx == 0) {
                                result.add(new OsmMoveAction(way, nodeIdx, idx));
                                break;
                            }
                        }
                    }
                }
            }
            return result;
        }
    }
}
