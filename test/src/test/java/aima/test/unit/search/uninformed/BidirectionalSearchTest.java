package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsBidirectionallyFunction;
import aima.core.search.basic.uninformed.BidirectionalSearch;
import aima.core.util.datastructure.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author manthan.
 */
@RunWith(Parameterized.class)
public class BidirectionalSearchTest {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> implementations() {
        return Arrays.asList(new Object[][]{{"BidirectionalSearch"}});
    }

    @Parameterized.Parameter
    public String searchFunctionName;

    public <A, S> List<A> searchForActions(Pair<Problem<A, S>, Problem<A, S>> problem) {
        SearchForActionsBidirectionallyFunction<A, S> searchForActionsFunction = new BidirectionalSearch<A, S>();
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
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.ARAD)),
                searchForActions(ProblemFactory.getSimpleBidirectionalSearchProblem(
                        SimplifiedRoadMapOfPartOfRomania.SIBIU, SimplifiedRoadMapOfPartOfRomania.TIMISOARA)));

    }

}