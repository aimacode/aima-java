package aima.test.unit.search.pqueue.informed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2D;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.informed.AStarSearch;
import aima.core.search.basic.informed.RecursiveBestFirstSearch;
import aima.core.util.datastructure.Point2D;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.ToDoubleFunction;

@RunWith(Parameterized.class)
public class InformedSearchTest {

  private static final String A_STAR = "AStarSearch";
  private static final String RBFS = "RecursiveBestFirstSearch";

  @Parameters(name = "{index}: {0}")
  public static Collection<Object[]> implementations() {
    return Arrays.asList(new Object[][] {
        {RBFS},
        {A_STAR}
    });
  }

  @Parameter
  public String searchFunctionName;

  public <A, S> List<A> searchForActions(Problem<A, S> problem, ToDoubleFunction<Node<A, S>>
      hf) {
    SearchForActionsFunction<A, S> searchForActionsFunction;
    if (A_STAR.equals(searchFunctionName)) {
      searchForActionsFunction = new AStarSearch<>(hf);
    } else if (RBFS.equals(searchFunctionName)) {
      searchForActionsFunction = new RecursiveBestFirstSearch<>(hf);
    } else {
      throw new UnsupportedOperationException();
    }
    return searchForActionsFunction.apply(problem);
  }

  @Test
  public void testSimplifiedRoadMapOfPartOfRomania() {

    String initialLocation = SimplifiedRoadMapOfPartOfRomania.ARAD;

    Problem<GoAction, InState> problem = ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
        initialLocation, initialLocation);

    Assert.assertEquals(Arrays.asList((String) null),
      searchForActions(problem, new MyHeuristicFunction(initialLocation)));

    String goalLocation = SimplifiedRoadMapOfPartOfRomania.BUCHAREST;
    problem = ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
        initialLocation, goalLocation);
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
            new GoAction(SimplifiedRoadMapOfPartOfRomania.RIMNICU_VILCEA),
            new GoAction(SimplifiedRoadMapOfPartOfRomania.PITESTI),
            new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST)),
        searchForActions(problem, new MyHeuristicFunction(goalLocation)));
  }

  private class MyHeuristicFunction implements ToDoubleFunction<Node<GoAction, InState>> {

    private final Map2D map = new SimplifiedRoadMapOfPartOfRomania();
    private final String[] goalLocations;

    MyHeuristicFunction(String... goalLocations) {
      this.goalLocations = goalLocations;
    }

    @Override
    public double applyAsDouble(Node<GoAction, InState> node) {
      return g(node) + h(node.state(), map, goalLocations);
    }

    private double g(Node<GoAction, InState> node) {
      return node.pathCost();
    }

    private double h(InState state, Map2D map, String... goalLocations) {
      double nearestGoalDist = Double.MAX_VALUE;
      for (String goalLocation : goalLocations) {
        Point2D currentPosition = map.getPosition(state.getLocation());
        double tmp = evaluateManhattanDistanceOf(currentPosition, map.getPosition(goalLocation));
        if (tmp < nearestGoalDist) {
          nearestGoalDist = tmp;
        }
      }
      return nearestGoalDist;
    }

    private double evaluateManhattanDistanceOf(Point2D p1, Point2D p2) {
      return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getX() - p2.getY());
    }
  }
}

