package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.basic.uninformed.GenericSearch;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class GenericSearchTest {
    GenericSearch bfs = new GenericSearch() {
        @Override
        public Queue<Node> addToFrontier(Node child, Queue frontier) {
            ((LinkedList<Node>) frontier).add(child);
            return frontier;
        }

        @Override
        public boolean canImprove(HashMap reached, Node solution) {
            return solution==null;
        }
    };

    @Test
    public void romaniabfsTest(){
        System.out.println(bfs.genericSearch(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD)));
    }
}
