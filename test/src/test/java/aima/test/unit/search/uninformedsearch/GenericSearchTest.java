package aima.test.unit.search.uninformedsearch;

import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Node;
import aima.core.search.basic.uninformedsearch.GenericSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author samagra
 */
public class GenericSearchTest {
    // Generic Search as breadth first search
    GenericSearch bfs = new GenericSearch() {
        @Override
        public Queue<Node> addToFrontier(Node child, Queue frontier) {
            ((LinkedList<Node>) frontier).addLast(child);
            return frontier;
        }

        @Override
        public boolean canImprove(HashMap reached, Node solution) {
            return solution == null;
        }
    };

    // GenericSearch as depth first search
    GenericSearch dfs = new GenericSearch() {
        @Override
        public Queue<Node> addToFrontier(Node child, Queue frontier) {
            ((LinkedList<Node>) frontier).addFirst(child);
            return frontier;
        }

        @Override
        public boolean canImprove(HashMap reached, Node solution) {
            return solution == null;
        }
    };

    @Test
    public void romaniabfsTest() {
        Node solution = bfs.genericSearch(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD));
        Assert.assertEquals("In(Arad)", solution.state().toString());
        System.out.println(bfs.reached.toString());
    }

    @Test
    public void romaniadfsTest() {
        Node solution = dfs.genericSearch(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD));
        Assert.assertEquals("In(Arad)", solution.state().toString());
        System.out.println(dfs.reached.toString());
    }
}
