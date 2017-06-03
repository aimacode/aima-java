package aima.core.environment.map;

import aima.core.agent.Percept;
import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.Node;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.problem.StepCostFunction;
import aima.core.util.math.geom.shapes.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 */
public class MapFunctions {

    public static ActionsFunction<String, MoveToAction> createActionsFunction(Map map) {
        return new MapActionsFunction(map, false);
    }

    public static ActionsFunction<String, MoveToAction> createReverseActionsFunction(Map map) {
        return new MapActionsFunction(map, true);
    }

    public static ResultFunction<String, MoveToAction> createResultFunction() {
        return new MapResultFunction();
    }


    public static StepCostFunction<String, MoveToAction> createDistanceStepCostFunction(Map map) {
        return new DistanceStepCostFunction(map);
    }

    public static Function<Percept, String> createPerceptToStateFunction() {
        return  p -> (String) ((DynamicPercept) p).getAttribute(DynAttributeNames.PERCEPT_IN);
    }

    /** Returns a heuristic function based on straight line distance computation. */
    public static ToDoubleFunction<Node<String, MoveToAction>> createSLDHeuristicFunction(String goal, Map map) {
        return node -> getSLD(node.getState(), goal, map);
    }

    public static double getSLD(String loc1, String loc2, Map map) {
        double result = 0.0;
        Point2D pt1 = map.getPosition(loc1);
        Point2D pt2 = map.getPosition(loc2);
        if (pt1 != null && pt2 != null)
            result = pt1.distance(pt2);
        return result;
    }

    private static class MapActionsFunction implements ActionsFunction<String, MoveToAction> {
        private Map map = null;
        private boolean reverseMode;

        private MapActionsFunction(Map map, boolean reverseMode) {
            this.map = map;
            this.reverseMode = reverseMode;
        }

        public List<MoveToAction> apply(String state) {
            List<MoveToAction> actions = new ArrayList<>();

            List<String> linkedLocations = reverseMode ? map.getPossiblePrevLocations(state)
                    : map.getPossibleNextLocations(state);
            actions.addAll(linkedLocations.stream().map(MoveToAction::new).collect(Collectors.toList()));
            return actions;
        }
    }

    private static class MapResultFunction implements ResultFunction<String, MoveToAction> {

        public String apply(String s, MoveToAction a) {
            if (a != null)
                return a.getToLocation();
            // If the action is NoOp the result will be the current state.
            return s;
        }
    }

    /**
     * Implementation of StepCostFunction interface that uses the distance between
     * locations to calculate the cost in addition to a constant cost, so that it
     * may be used in conjunction with a Uniform-cost search.
     */
    private static class DistanceStepCostFunction implements StepCostFunction<String, MoveToAction> {
        private Map map = null;

        // Used by Uniform-cost search to ensure every step is greater than or equal
        // to some small positive constant
        private static double constantCost = 1.0;

        private DistanceStepCostFunction(Map map) {
            this.map = map;
        }

        @Override
        public double applyAsDouble(String state, MoveToAction action, String statePrimed) {
            Double distance = map.getDistance(state, statePrimed);
            if (distance == null || distance <= 0)
                return constantCost;
            return distance;
        }
    }
}
