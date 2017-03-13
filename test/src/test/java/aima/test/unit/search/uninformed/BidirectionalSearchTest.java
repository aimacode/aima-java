package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
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

    public <A, S> List<A> searchForActions(Problem<A, S> originalProblem, Problem<A, S> reverseProblem) {
        SearchForActionsBidirectionallyFunction<A, S> searchForActionsFunction = new BidirectionalSearch<A, S>();
        return searchForActionsFunction.apply(originalProblem, reverseProblem);
    }

    @Test
    public void testBidirectionalSearch() {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> problemPairtest1 = ProblemFactory.getSimpleBidirectionalSearchProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD);
        Assert.assertEquals(Arrays.asList((String) null), searchForActions(problemPairtest1.getFirst(), problemPairtest1.getSecond()));
        //Result will contain all intermediate nodes excluding initial and final nodes.
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> problemPairtest2 = ProblemFactory.getSimpleBidirectionalSearchProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS)),
                searchForActions(problemPairtest2.getFirst(), problemPairtest2.getSecond()));

        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> problemPairtest3 = ProblemFactory.getSimpleBidirectionalSearchProblem(
                SimplifiedRoadMapOfPartOfRomania.SIBIU, SimplifiedRoadMapOfPartOfRomania.TIMISOARA);
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.ARAD)),
                searchForActions(problemPairtest3.getFirst(), problemPairtest3.getSecond()));
    }

}