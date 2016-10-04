package aima.core.search.basic.support;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2D;
import aima.core.search.api.Node;
import aima.core.util.datastructure.Point2D;

import java.util.Arrays;
import java.util.function.ToDoubleFunction;

/**
 * @author wormi
 */
public class StraightLineDistanceHeuristic implements ToDoubleFunction<Node<GoAction, InState>> {

  private final Map2D map;
  private final String[] goals;

  public StraightLineDistanceHeuristic(Map2D map, String... goals) {
    this.map = map;
    this.goals = goals;
  }

  @Override
  public double applyAsDouble(Node<GoAction, InState> node) {
    return h(node.state());
  }

  private double h(InState state) {
    return Arrays.stream(goals).map(goal -> {
      Point2D currentPosition = map.getPosition(state.getLocation());
      Point2D goalPosition = map.getPosition(goal);
      return distanceOf(currentPosition, goalPosition);
    }).min(Double::compareTo).orElse(Double.MAX_VALUE);
  }

  private double distanceOf(Point2D p1, Point2D p2) {
    return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
        + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
  }
}
