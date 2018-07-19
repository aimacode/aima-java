package aima.test.unit.search.uninformedsearch;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.basic.uninformedsearch.BreadthFirstSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BreadthFirstSearchTest {
    BreadthFirstSearch breadthFirstSearch = new BreadthFirstSearch();

    @Test
    public void testSimplifiedRomania(){
        Problem problem = ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
        Node result = breadthFirstSearch.search(problem);
        List actions = Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS),
                new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST));
        Assert.assertEquals(actions,breadthFirstSearch.apply(problem));
    }
}
