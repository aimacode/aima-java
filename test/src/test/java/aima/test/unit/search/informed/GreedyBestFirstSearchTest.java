package aima.test.unit.search.informed;

import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.*;
import static org.junit.Assert.assertEquals;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2D;
import aima.core.environment.map2d.Map2DFunctionFactory;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.informed.GreedyBestFirstSearch;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.ToDoubleFunction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class GreedyBestFirstSearchTest {

  private static final String GREEDYBFS = "GreedyBestFirstSearch";

  @Parameters
  public static Collection<Object[]> implementations() {
    return Arrays.asList(new Object[][]{{GREEDYBFS}});
  }

  @Parameter
  public String searchFunctionName;

  @Test
  public void testSimplifiedRoadMapOfPartOfRomania() {
    Map2D map = new SimplifiedRoadMapOfPartOfRomania();
    String initialLocation = ARAD;

    String finalLocation = initialLocation;
    Problem<GoAction, InState> problem = ProblemFactory
        .getSimplifiedRoadMapOfPartOfRomaniaProblem(initialLocation, finalLocation);
    List<GoAction> goal = Arrays.asList((GoAction) null);
    assertEquals(goal, searchForActions(problem,
        new Map2DFunctionFactory.StraightLineDistanceHeuristic(map, finalLocation)));

    finalLocation = BUCHAREST;
    problem = ProblemFactory
        .getSimplifiedRoadMapOfPartOfRomaniaProblem(initialLocation, finalLocation);
    goal = Arrays.asList(new GoAction(SIBIU), new GoAction(FAGARAS), new GoAction(BUCHAREST));
    assertEquals(goal, searchForActions(problem,
        new Map2DFunctionFactory.StraightLineDistanceHeuristic(map, finalLocation)));
  }

  private <A, S> List<A> searchForActions(Problem<A, S> problem, ToDoubleFunction<Node<A, S>> h) {
    SearchForActionsFunction<A, S> searchForActionsFunction;

    if (GREEDYBFS.equals(searchFunctionName)) {
      searchForActionsFunction = new GreedyBestFirstSearch<>(h);
    } else {
      throw new UnsupportedOperationException();
    }

    return searchForActionsFunction.apply(problem);
  }
}
