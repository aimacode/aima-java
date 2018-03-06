package aima.test.unit.search.uninformed;

import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.*;
import static org.junit.Assert.assertEquals;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.uninformed.DepthFirstSearch;
import aima.extra.search.pqueue.uninformed.DepthFirstQueueSearch;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DepthFirstSearchTest {

  @Parameters(name = "{index}: {0}")
  public static Collection<Object[]> implementations() {
    return Arrays.asList(new Object[][]{{"DepthFirstSearch"}, {"DepthFirstQueueSearch"}});
  }

  @Parameter
  public String searchFunctionName;

  public <A, S> List<A> searchForActions(Problem<A, S> problem) {
    SearchForActionsFunction<A, S> searchForActionsFunction;
    if ("DepthFirstSearch".equals(searchFunctionName)) {
      searchForActionsFunction = new DepthFirstSearch<>();
    } else if ("DepthFirstQueueSearch".equals(searchFunctionName)) {
      searchForActionsFunction = new DepthFirstQueueSearch<>();
    } else {
      throw new UnsupportedOperationException(
          "Unsupported search function name: " + searchFunctionName);
    }
    return searchForActionsFunction.apply(problem);
  }

  @Test
  public void testSimplifiedRoadmapOfPartOfRomania() {
    assertEquals(Arrays.asList((GoAction) null),
        searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(ARAD, ARAD)));

    List<GoAction> goal = Arrays
        .asList(new GoAction(SIBIU), new GoAction(RIMNICU_VILCEA), new GoAction(PITESTI),
            new GoAction(BUCHAREST));
    assertEquals(goal, searchForActions(
        ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(ARAD, BUCHAREST)));

    goal = Arrays.asList(new GoAction(SIBIU), new GoAction(RIMNICU_VILCEA), new GoAction(PITESTI),
        new GoAction(BUCHAREST), new GoAction(URZICENI), new GoAction(VASLUI), new GoAction(IASI),
        new GoAction(NEAMT));
    assertEquals(goal,
        searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(ARAD, NEAMT)));

    goal = Arrays.asList(new GoAction(ZERIND));
    assertEquals(goal, searchForActions(
        ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(ORADEA, ZERIND)));
  }
}
