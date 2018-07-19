package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Node;
import aima.core.search.basic.uninformedsearch.BreadthFirstSearch;
import aima.core.search.basic.uninformedsearch.GenericSearch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;


public class TestSearchTest {
    GenericSearch bfs = new GenericSearch() {
        @Override
        public Queue<Node> addToFrontier(Node child, Queue frontier) {
            frontier.add(child);
            return frontier;
        }

        @Override
        public boolean canImprove(HashMap reached, Node solution) {
            return solution==null;
        }

        @Override
        public Object apply(Object o) {
            return new ArrayList<>();
        }
    };
    String s = "sapas";
    BreadthFirstSearch search = new BreadthFirstSearch();
    @Test
    public void testClassTest(){
        Node result = search.search(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD));
        System.out.println(result.toString());
    }
}
