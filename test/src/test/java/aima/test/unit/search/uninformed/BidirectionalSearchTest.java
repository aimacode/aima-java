package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.BidirectionalActions;
import aima.core.search.api.Problem;
import aima.core.search.basic.uninformed.BidirectionalSearch;
import aima.core.util.datastructure.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

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

    public <A, S> BidirectionalActions<A> searchForActions(Problem<A, S> originalProblem, Problem<A, S> reverseProblem) {
        BidirectionalSearch<A, S> searchForActionsFunction = new BidirectionalSearch<A, S>();
        return searchForActionsFunction.apply(originalProblem, reverseProblem);
    }

    @Test
    public void testBidirectionalSearch() {
        //Result will contain all intermediate nodes excluding initial and final nodes.

        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> problemPairtest1 = ProblemFactory.getSimpleBidirectionalSearchProblem(
                SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.ARAD);
        Assert.assertEquals(Arrays.asList(), searchForActions(problemPairtest1.getFirst(), problemPairtest1.getSecond()).fromInitialStatePart());

        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> problemPairtest2 = ProblemFactory.getSimpleBidirectionalSearchProblem(
                SimplifiedRoadMapOfPartOfRomania.TIMISOARA, SimplifiedRoadMapOfPartOfRomania.URZICENI);
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.ARAD),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS)),
                searchForActions(problemPairtest2.getFirst(), problemPairtest2.getSecond()).fromInitialStatePart());

        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> problemPairtest3 = ProblemFactory.getSimpleBidirectionalSearchProblem(
                SimplifiedRoadMapOfPartOfRomania.TIMISOARA, SimplifiedRoadMapOfPartOfRomania.URZICENI);
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST)),
                searchForActions(problemPairtest3.getFirst(), problemPairtest3.getSecond()).fromGoalStatePart());

        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> problemPairtest4 = ProblemFactory.getSimpleBidirectionalSearchProblem(
                SimplifiedRoadMapOfPartOfRomania.TIMISOARA, SimplifiedRoadMapOfPartOfRomania.URZICENI);
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.ARAD),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST)),
                searchForActions(problemPairtest4.getFirst(), problemPairtest4.getSecond()).fromInitialStateToGoalState());

        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> problemPairtest5 = ProblemFactory.getSimpleBidirectionalSearchProblem(
                SimplifiedRoadMapOfPartOfRomania.TIMISOARA, SimplifiedRoadMapOfPartOfRomania.URZICENI);
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.FAGARAS),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.ARAD)),
                searchForActions(problemPairtest5.getFirst(), problemPairtest5.getSecond()).fromGoalStateToInitialState());
    }

}