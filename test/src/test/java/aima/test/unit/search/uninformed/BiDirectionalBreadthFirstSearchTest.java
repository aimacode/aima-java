package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.support.StateActionTimeLine;
import aima.core.search.basic.uninformed.BiDirectionalBreadthFirstSearch;
import org.junit.Assert;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.*;
import static aima.core.environment.support.ProblemFactory.romaniaRoadMapProblem;
import static org.junit.Assert.assertEquals;


public class BiDirectionalBreadthFirstSearchTest {

  @Test
  public void fifoTest() throws Exception {
    InState s0 = new InState("0");
    InState s1 = new InState("1");
    InState s2 = new InState("2");
    Node node0 = new MyTestNode(s0);
    Node node1 = new MyTestNode(s1);
    Node node2 = new MyTestNode(s2);
    Queue<Node<InState, GoAction>> fifoQueue = new BiDirectionalBreadthFirstSearch<InState, GoAction>()
        .newFIFOQueue();
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

    BiDirectionalBreadthFirstSearch<GoAction, InState> biBFS = new BiDirectionalBreadthFirstSearch<>();
    Node<GoAction, InState> node = biBFS.buildResultPath(asr, pr, romaniaRoadMapProblem(ARAD,
        RIMNICU_VILCEA));
    assertEquals(new InState(PITESTI), node.state());
    assertEquals(new InState(RIMNICU_VILCEA), node.parent().state());
    assertEquals(new InState(SIBIU), node.parent().parent().state());
    assertEquals(new InState(ARAD), node.parent().parent().parent().state());
    assertEquals(null, node.parent().parent().parent().parent());
  }

  private <A, S> List<A> searchSolutions(Problem<A, S> problem) {
    SearchForActionsFunction<A, S> searchForActionsFunction = new BiDirectionalBreadthFirstSearch<>();
    return searchForActionsFunction.apply(problem);
  }

  private <A, S> List<A> searchSolution(Problem<A, S> problem,
                                        StateActionTimeLine<String, String> timeLine) {
    BiDirectionalBreadthFirstSearch<A, S> biBfs= new BiDirectionalBreadthFirstSearch<>();
    biBfs.register(timeLine);
    return biBfs.apply(problem);
  }

  @Test
  public void arad2arad() {
    Assert.assertEquals(Arrays.asList((String) null),
        searchSolutions(romaniaRoadMapProblem(ARAD, ARAD)));
  }

  @Test
  public void arad2sibiu() {
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU)),
        searchSolutions(romaniaRoadMapProblem(ARAD, SIBIU)));
  }

  @Test
  public void arad2fagaras() {
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS)),
        searchSolutions(romaniaRoadMapProblem(ARAD, FAGARAS)));
  }

  @Test
  public void arad2bucharest() {
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST)),
        searchSolutions(romaniaRoadMapProblem(ARAD, BUCHAREST)));
  }

  @Test
  public void arad2neamt() {

    List<GoAction> actual;
    // enable to run with "logging"
    boolean printDebug = true;
    if (printDebug) {
      StateActionTimeLine<String, String> timeLine = new StateActionTimeLine<>();
      actual = searchSolution(romaniaRoadMapProblem(ARAD, NEAMT), timeLine);
      printTimeLine(timeLine);
    } else {
      actual = searchSolutions(romaniaRoadMapProblem(ARAD, NEAMT));
    }


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
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST),
            new GoAction(URZICENI)
        ),
        searchSolutions(romaniaRoadMapProblem(ARAD, URZICENI)));
  }

  @Test
  public void timisoara2aradOrZerind() {
    List<GoAction> actual = searchSolutions(romaniaRoadMapProblem(TIMISOARA, ARAD, ZERIND));
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

    List<GoAction> actual;
    // enable to run with "logging"
    boolean printDebug = false;
    if (printDebug) {
      StateActionTimeLine<String, String> timeLine = new StateActionTimeLine<>();
      actual = searchSolution(romaniaRoadMapProblem(TIMISOARA, BUCHAREST, CRAIOVA), timeLine);
      printTimeLine(timeLine);
    } else {
      actual = searchSolutions(romaniaRoadMapProblem(TIMISOARA, BUCHAREST, CRAIOVA));
    }

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
    List<GoAction> actual = searchSolutions(romaniaRoadMapProblem(ARAD, ORADEA));

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

  private void printTimeLine(StateActionTimeLine<String, String> timeLine) {
    StringBuilder sb = new StringBuilder();
    for (StateActionTimeLine.MyEntry key : timeLine) {
      sb.append(key.time).append(",\t")
          .append("process-").append(key.processId).append(",\t")
          .append(key.state).append(",\t")
          .append(key.action).append("\n");
    }

    System.out.print(sb.toString());
  }

}