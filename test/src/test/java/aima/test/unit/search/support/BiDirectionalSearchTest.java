package aima.test.unit.search.support;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.search.api.BidirectionalProblem;
import aima.core.search.api.Node;
import aima.core.search.basic.support.BidirectionalSearch;
import aima.core.search.basic.support.BidirectionalSearch.Strategy;
import aima.core.search.basic.support.StraightLineDistanceHeuristic;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.function.ToDoubleFunction;

import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.*;
import static aima.core.environment.support.ProblemFactory.romaniaRoadMapProblem;
import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class BiDirectionalSearchTest {

  public static class Basics {
    @Test
    public void fifoTest() throws Exception {
      InState s0 = new InState("0");
      InState s1 = new InState("1");
      InState s2 = new InState("2");
      Node<GoAction, InState> node0 = new MyTestNode(s0);
      Node<GoAction, InState> node1 = new MyTestNode(s1);
      Node<GoAction, InState> node2 = new MyTestNode(s2);
      Queue<Node<GoAction, InState>> fifoQueue = new BidirectionalSearch<GoAction, InState>(null,
          null, null, null).newFIFOQueue();
      fifoQueue.add(node0);
      fifoQueue.add(node1);
      assertEquals(s0, fifoQueue.peek().state());
      assertEquals(s0, fifoQueue.remove().state());
      fifoQueue.add(node2);
      assertEquals(s1, fifoQueue.remove().state());
      assertEquals(s2, fifoQueue.remove().state());
    }

    @Test
    public void mergeNodesTest() {
      Node<GoAction, InState> a = new MyTestNode(ARAD);
      Node<GoAction, InState> as = new MyTestNode(SIBIU, a);
      Node<GoAction, InState> asr = new MyTestNode(RIMNICU_VILCEA, as);

      Node<GoAction, InState> p = new MyTestNode(PITESTI);
      Node<GoAction, InState> pr = new MyTestNode(RIMNICU_VILCEA, p);

      BidirectionalSearch<GoAction, InState> biBFS = new BidirectionalSearch<>(null, null, null,
          null);
      Node<GoAction, InState> node = biBFS.buildResultPath(asr, pr, romaniaRoadMapProblem(ARAD,
          RIMNICU_VILCEA).getOriginalProblem());
      assertEquals(new InState(PITESTI), node.state());
      assertEquals(new InState(RIMNICU_VILCEA), node.parent().state());
      assertEquals(new InState(SIBIU), node.parent().parent().state());
      assertEquals(new InState(ARAD), node.parent().parent().parent().state());
      assertEquals(null, node.parent().parent().parent().parent());
    }

    private class MyTestNode implements Node<GoAction, InState> {

      private final InState state;
      private final Node<GoAction, InState> parent;

      MyTestNode(String state) {
        this(new InState(state), null);
      }

      MyTestNode(String state, Node<GoAction, InState> parent) {
        this(new InState(state), parent);
      }

      MyTestNode(InState state, Node<GoAction, InState> parent) {
        this.state = state;
        this.parent = parent;
      }

      MyTestNode(InState s) {
        this(s, null);
      }

      @Override
      public InState state() {
        return state;
      }

      @Override
      public Node<GoAction, InState> parent() {
        return parent;
      }

      @Override
      public GoAction action() {
        throw new NotImplementedException();
      }

      @Override
      public double pathCost() {
        return 0;
      }
    }
  }

  @RunWith(Parameterized.class)
  public static class FindSolutions {


    private final String forwardStrategy;
    private final String backwardStrategy;


    public FindSolutions(String forwardStrategy, String backwardStrategy) {
      this.forwardStrategy = forwardStrategy;
      this.backwardStrategy = backwardStrategy;
    }

    @Parameterized.Parameters(name = "{index}: forward={0}, backward={1}")
    public static Collection<Object[]> data() {
      final List<Object[]> objects = new ArrayList<Object[]>();
      objects.add(new String[]{Strategy.BFS, Strategy.BFS});
      objects.add(new String[]{Strategy.ASTAR, Strategy.ASTAR});
      objects.add(new String[]{Strategy.UCS, Strategy.ASTAR});
      return objects;
    }

    private List<GoAction> searchSolution(BidirectionalProblem<GoAction, InState> problem,
                                          ToDoubleFunction<Node<GoAction, InState>> forwardHf,
                                          ToDoubleFunction<Node<GoAction, InState>> backwardhf) {
      BidirectionalSearch<GoAction, InState> search = new BidirectionalSearch<>(
          forwardStrategy, backwardStrategy, forwardHf, backwardhf);
      try {
        return search.findSolution(problem);
      } catch (ExecutionException | InterruptedException e) {
        throw new RuntimeException((e));
      }
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
    public void arad2bucharest() {
      final String start = ARAD;
      final String goal = BUCHAREST;
      Assert.assertEquals(
          Arrays.asList(
              new GoAction(SIBIU),
              new GoAction(FAGARAS),
              new GoAction(BUCHAREST)),
          searchSolution(romaniaRoadMapProblem(start, goal), getHeuristicFunction(goal),
              getHeuristicFunction(start)));
    }

    @Test
    public void arad2neamt() {
      final String start = ARAD;
      final String goal = NEAMT;
      List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goal),
          getHeuristicFunction(goal), getHeuristicFunction(start));

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
          searchSolution(romaniaRoadMapProblem(start, goal), getHeuristicFunction(goal),
              getHeuristicFunction(start)));
    }

    @Test
    public void timisoara2aradOrZerind() {
      final String start = TIMISOARA;
      final String[] goals = {ARAD, ZERIND};
      List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goals),
          getHeuristicFunction(goals), getHeuristicFunction(start));
      List<GoAction> expected;
      if (actual.contains(new GoAction(ZERIND))) {
        expected = Arrays.asList(new GoAction(ZERIND));
      } else {
        expected = Arrays.asList(new GoAction(ARAD));
      }
      Assert.assertEquals(expected, actual);
    }

    @Test
    public void timisoara2bucharestOrCraiova() {

      final String start = TIMISOARA;
      final String[] goals = {BUCHAREST, CRAIOVA};
      List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goals),
          getHeuristicFunction(goals), getHeuristicFunction(start));

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
      List<GoAction> actual = searchSolution(romaniaRoadMapProblem(start, goal),
          getHeuristicFunction(goal), getHeuristicFunction(start));

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

}