package aima.core.environment.map;

import aima.core.agent.impl.DynamicPercept;
import aima.core.search.framework.Node;
import aima.core.search.framework.problem.StepCostFunction;
import aima.core.util.math.geom.shapes.Point2D;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 * @author Ruediger Lunde
 */
public class MapFunctions {

    public static Function<String, List<MoveToAction>> createActionsFunction(Map map) {
        return (state) -> map.getPossibleNextLocations(state).stream().
                map(MoveToAction::new).collect(Collectors.toList());
    }

    public static Function<String, List<MoveToAction>> createReverseActionsFunction(Map map) {
        return (state) -> map.getPossiblePrevLocations(state).stream().
                map(MoveToAction::new).collect(Collectors.toList());
    }

    public static BiFunction<String, MoveToAction, String> createResultFunction() {
        return (state, action) -> action.getToLocation();
    }

    public static StepCostFunction<String, MoveToAction> createDistanceStepCostFunction(Map map) {
        return (state, action, statePrimed) -> {
            Double distance = map.getDistance(state, statePrimed);
            // Used by Uniform-cost search to ensure every step is greater than or equal
            // to some small positive constant
            return (distance != null && distance > 0) ? distance : 0.1;
        };
    }

    public static Function<DynamicPercept, String> createPerceptToStateFunction() {
        return  p -> (String) p.getAttribute(AttNames.PERCEPT_IN);
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
}
