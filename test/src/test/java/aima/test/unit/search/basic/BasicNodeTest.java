package aima.test.unit.search.basic;

import aima.core.search.basic.support.BasicNode;
import org.junit.Test;
import org.junit.Assert;

public class BasicNodeTest {

  /**
   * this is most likely not the best format for all applications but for now it's enough to
   * print debug short nodes
   */
  @Test
  public void printJsonTest() {
    BasicNode<String, String> root = new BasicNode<>("root", null, null, 0.0);
    BasicNode<String, String> branch = new BasicNode<>("branch", root, "getBranch", 0.0);
    BasicNode<String, String> leaf = new BasicNode<>("leaf", branch, "getLeaf", 0.0);
    Assert.assertEquals(
        "{" +
            "parent: {" +
              "parent: {" +
                "parent: null, " +
                "state: 'root', " +
                "fromAction: null, " +
                "withPathCost: 0.0" +
                "}, "  +
              "state: 'branch', " +
              "fromAction: 'getBranch', " +
              "withPathCost: 0.0" +
            "}, " +
            "state: 'leaf', " +
            "fromAction: 'getLeaf', " +
            "withPathCost: 0.0" +
        "}",
        leaf.toString()
    );
  }
}
