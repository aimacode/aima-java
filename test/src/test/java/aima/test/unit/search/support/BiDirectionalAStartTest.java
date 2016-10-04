package aima.test.unit.search.support;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.search.api.BidirectionalProblem;
import aima.core.search.api.Node;
import aima.core.search.basic.support.BidirectionalSearch;
import aima.core.search.basic.support.StateActionTimeLine;
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
 */
public class BiDirectionalAStartTest {

  private List<GoAction> searchSolution(BidirectionalProblem<GoAction, InState> problem,
                                        ToDoubleFunction<Node<GoAction, InState>> forwardHf,
                                        ToDoubleFunction<Node<GoAction, InState>> backwardhf,
                                        StateActionTimeLine stateActionTimeLine) {
    BidirectionalSearch<GoAction, InState> search = new BidirectionalSearch<>(
        BidirectionalSearch.Strategy.ASTAR, forwardHf, backwardhf);
    if (stateActionTimeLine != null) {
      search.setTimeLine(stateActionTimeLine);
    }
    try {
      return search.findSolution(problem);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException((e));
    }
  }

  private List<GoAction> searchSolution(BidirectionalProblem<GoAction, InState> problem,
                                        ToDoubleFunction<Node<GoAction, InState>> forwardHf,
                                        ToDoubleFunction<Node<GoAction, InState>> backwardHf) {
    return searchSolution(problem, forwardHf, backwardHf, null);
  }

  @Test
  public void arad2arad() {
    Assert.assertEquals(Arrays.asList((String) null),
        searchSolution(romaniaRoadMapProblem(ARAD, ARAD), getHeuristicFunction(ARAD),
            getHeuristicFunction(ARAD)));
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
        searchSolution(romaniaRoadMapProblem(start, goal), getHeuristicFunction(goal),
            getHeuristicFunction(start)));
  }

  @Test
  public void arad2fagaras() {
    final String start = ARAD;
    final String goal = FAGARAS;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS)),
        searchSolution(romaniaRoadMapProblem(start, goal), getHeuristicFunction(goal),
            getHeuristicFunction(start)));
  }

  @Test
  public void sibiu2bucharest() {
    StateActionTimeLine timeLine = new StateActionTimeLine();
    final String start = SIBIU;
    final String goal = BUCHAREST;
    final List<GoAction> actual = searchSolution(
        romaniaRoadMapProblem(start, goal),
        getHeuristicFunction(goal), getHeuristicFunction(start), timeLine);
    final List<GoAction> expected;
    if (actual.contains(new GoAction(FAGARAS))) {
      expected = Arrays.asList(
          new GoAction(FAGARAS),
          new GoAction(BUCHAREST));
    } else {
      expected = Arrays.asList(
          new GoAction(RIMNICU_VILCEA),
          new GoAction(PITESTI),
          new GoAction(BUCHAREST));
    }
    printTimeLine(timeLine);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void arad2neamt() {
    StateActionTimeLine timeLine = new StateActionTimeLine();
    final String start = ARAD;
    final String goal = NEAMT;
    List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goal),
        getHeuristicFunction(goal), getHeuristicFunction(start), timeLine);
    final List<GoAction> expected;
    if (actual.contains(new GoAction(FAGARAS))) {
      expected = Arrays.asList(
          new GoAction(SIBIU),
          new GoAction(FAGARAS),
          new GoAction(BUCHAREST),
          new GoAction(URZICENI),
          new GoAction(VASLUI),
          new GoAction(IASI),
          new GoAction(NEAMT));
    } else {
      expected = Arrays.asList(
          new GoAction(SIBIU),
          new GoAction(RIMNICU_VILCEA),
          new GoAction(PITESTI),
          new GoAction(BUCHAREST),
          new GoAction(URZICENI),
          new GoAction(VASLUI),
          new GoAction(IASI),
          new GoAction(NEAMT));
    }

    printTimeLine(timeLine);
    Assert.assertEquals(
        expected,
        actual);
  }

  @Test
  public void arad2urziceni() {
    final String start = ARAD;
    final String goal = URZICENI;
    final List<GoAction> actual = searchSolution(
        romaniaRoadMapProblem(start, goal), getHeuristicFunction(goal),getHeuristicFunction(start)
    );
    final List<GoAction> expected;
    if (actual.contains(new GoAction(FAGARAS))) {
      expected = Arrays.asList(
          new GoAction(SIBIU),
          new GoAction(FAGARAS),
          new GoAction(BUCHAREST),
          new GoAction(URZICENI)
      );
    } else {
      expected = Arrays.asList(
          new GoAction(SIBIU),
          new GoAction(RIMNICU_VILCEA),
          new GoAction(PITESTI),
          new GoAction(BUCHAREST),
          new GoAction(URZICENI)
      );
    }
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void timisoara2aradOrZerind() {
    final String start = TIMISOARA;
    final String[] goals = {ARAD, ZERIND};
    List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goals),
        getHeuristicFunction(goals), getHeuristicFunction(start));
    List<GoAction> expected = Arrays.asList(new GoAction(ARAD));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void timisoara2bucharestOrCraiova() {
    StateActionTimeLine timeLine = new StateActionTimeLine();
    final String start = TIMISOARA;
    final String[] goals = {BUCHAREST, CRAIOVA};
    List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goals),
        getHeuristicFunction(goals), getHeuristicFunction(start), timeLine);
    List<GoAction> expected;
    if (actual.contains(new GoAction(LUGOJ))) {
      expected = Arrays.asList(
          new GoAction(LUGOJ),
          new GoAction(MEHADIA),
          new GoAction(DOBRETA),
          new GoAction(CRAIOVA)
      );
    } else {
      expected = Arrays.asList(
          new GoAction(ARAD),
          new GoAction(SIBIU),
          new GoAction(RIMNICU_VILCEA),
          new GoAction(CRAIOVA)
      );
    }
    printTimeLine(timeLine);
    Assert.assertEquals(expected, actual);
  }


  @Test
  public void arad2oreda() {

    final String start = ARAD;
    final String goal = ORADEA;
    List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goal),
        getHeuristicFunction(goal), getHeuristicFunction(start));

    List<GoAction> expected = Arrays.asList(
        new GoAction(ZERIND),
        new GoAction(ORADEA)
    );
    Assert.assertEquals(expected, actual);
  }


  private void printTimeLine(StateActionTimeLine timeLine) {
    StringBuilder sb = new StringBuilder();
    for (StateActionTimeLine.MyEntry key : timeLine) {
      sb.append(key.time).append(",\t")
          .append("process-").append(key.processId).append(",\t")
          .append(key.message).append("\n");
    }

    System.out.print(sb.toString());
  }
}
