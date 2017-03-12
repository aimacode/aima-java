package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.search.api.Node;
import aima.core.search.basic.uninformed.BidirectionalBreadthFirstSearch;
import org.junit.Test;

import java.util.Queue;

import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.*;
import static aima.core.environment.support.ProblemFactory.getBidirectionalRomaniaRoadMapProblem;
import static org.junit.Assert.assertEquals;

public class BiDirectionalSearchTest {

  @Test
  public void fifoTest() throws Exception {
    InState s0 = new InState("0");
    InState s1 = new InState("1");
    InState s2 = new InState("2");
    Node<GoAction, InState> node0 = new MyTestNode(s0);
    Node<GoAction, InState> node1 = new MyTestNode(s1);
    Node<GoAction, InState> node2 = new MyTestNode(s2);
    Queue<Node<GoAction, InState>> fifoQueue = new BidirectionalBreadthFirstSearch<GoAction, InState>().newFIFOQueue();
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

    BidirectionalBreadthFirstSearch<GoAction, InState> biBFS = new BidirectionalBreadthFirstSearch<>();
    Node<GoAction, InState> node = biBFS.buildResultPath(asr, pr, getBidirectionalRomaniaRoadMapProblem(ARAD,
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
      throw new UnsupportedOperationException();
    }

    @Override
    public double pathCost() {
      return 0;
    }
  }
}