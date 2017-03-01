package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.uninformed.BidirectionalSearch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by manthan on 1/3/17.
 */
@RunWith(Parameterized.class)
public class BidirectionalSearchTest {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> implementations() {
        return Arrays.asList(new Object[][]{{"BidirectionalSearch"}});
    }

    @Parameterized.Parameter
    public String searchFunctionName;

    public <A, S> List<A> searchForActions(Problem<A, S> problem) {
        SearchForActionsFunction<A, S> searchForActionsFunction = new BidirectionalSearch<A, S>();
        return searchForActionsFunction.apply(problem);
    }

    @Test
    public void testBidirectionalSearch() {
        Assert.assertEquals(Arrays.asList((String) null),
                searchForActions(ProblemFactory.getSimpleBidirectionalSearchProblem(
                        SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD)));
        //Result will contain all intermediate nodes excluding initial and final nodes.
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS)),
                searchForActions(ProblemFactory.getSimpleBidirectionalSearchProblem(
                        SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST)));
    }

}