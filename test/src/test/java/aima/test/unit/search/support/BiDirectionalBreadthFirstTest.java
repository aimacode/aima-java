package aima.test.unit.search.support;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.search.api.BidirectionalProblem;
import aima.core.search.api.Node;
import aima.core.search.basic.support.BidirectionalSearch;
import aima.core.search.basic.support.StraightLineDistanceHeuristic;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.ToDoubleFunction;

import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.*;
import static aima.core.environment.support.ProblemFactory.romaniaRoadMapProblem;

/**
* @author wormi
* @see <a href="https://to.headissue.net/radar/browse/"></a>
*/
public class BiDirectionalBreadthFirstTest {

  private List<GoAction> searchSolution(BidirectionalProblem<GoAction, InState> problem) {
    BidirectionalSearch<GoAction, InState> search = new BidirectionalSearch<>(
        BidirectionalSearch.Strategy.BFS);
    try {
      return search.findSolution(problem);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException((e));
    }
  }

  @Test
  public void arad2arad() {
    Assert.assertEquals(Arrays.asList((String) null),
        searchSolution(romaniaRoadMapProblem(ARAD, ARAD)));
  }

  private ToDoubleFunction<Node<GoAction, InState>> getHeuristicFunction(String... goals) {
    return new StraightLineDistanceHeuristic(new SimplifiedRoadMapOfPartOfRomania(), goals);
  }

  @Test
  public void arad2sibiu() {
    final String start = ARAD;
    final String goal = SIBIU;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU)),
        searchSolution(romaniaRoadMapProblem(start, goal)));
  }

  @Test
  public void arad2fagaras() {
    final String start = ARAD;
    final String goal = FAGARAS;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS)),
        searchSolution(romaniaRoadMapProblem(start, goal)));
  }

  @Test
  public void arad2bucharest() {
    final String start = ARAD;
    final String goal = BUCHAREST;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST)),
        searchSolution(romaniaRoadMapProblem(start, goal)));
  }

  @Test
  public void arad2neamt() {
    final String start = ARAD;
    final String goal = NEAMT;
    List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goal));

    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST),
            new GoAction(URZICENI),
            new GoAction(VASLUI),
            new GoAction(IASI),
            new GoAction(NEAMT)),
        actual);
  }

  @Test
  public void arad2urziceni() {
    final String start = ARAD;
    final String goal = URZICENI;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST),
            new GoAction(URZICENI)
        ),
        searchSolution(romaniaRoadMapProblem(start, goal)));
  }

  @Test
  public void timisoara2aradOrZerind() {
    final String start = TIMISOARA;
    final String[] goals = {ARAD, ZERIND};
    List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goals));
    List<GoAction> expected = Arrays.asList(new GoAction(ARAD));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void timisoara2bucharestOrCraiova() {

    final String start = TIMISOARA;
    final String[] goals = {BUCHAREST, CRAIOVA};
    List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goals));

    List<GoAction> expected;
    // three solutions valid
    if (actual.contains(new GoAction(FAGARAS))) {
      expected = Arrays.asList(
          new GoAction(ARAD),
          new GoAction(SIBIU),
          new GoAction(FAGARAS),
          new GoAction(BUCHAREST)
      );
    } else if (actual.contains(new GoAction(RIMNICU_VILCEA))) {
      expected = Arrays.asList(
          new GoAction(ARAD),
          new GoAction(SIBIU),
          new GoAction(RIMNICU_VILCEA),
          new GoAction(CRAIOVA)
      );
    } else {
      expected = Arrays.asList(
          new GoAction(LUGOJ),
          new GoAction(MEHADIA),
          new GoAction(DOBRETA),
          new GoAction(CRAIOVA)
      );
    }
    Assert.assertEquals(expected, actual);
  }


  @Test
  public void arad2oreda() {

    List<GoAction> expected;
    final String start = ARAD;
    final String goal = ORADEA;
    List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goal));

    // has two possible results
    if (actual.contains(new GoAction(ZERIND))) {
      expected = Arrays.asList(
          new GoAction(ZERIND),
          new GoAction(ORADEA));
    } else {
      expected = Arrays.asList(
          new GoAction(SIBIU),
          new GoAction(ORADEA));
    }

    Assert.assertEquals(expected, actual);
  }


}
