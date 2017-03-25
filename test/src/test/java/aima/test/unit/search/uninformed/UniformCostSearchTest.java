package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.uninformed.UniformCostSearch;
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
public class UniformCostSearchTest {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> implementations() {
        return Arrays.asList(new Object[][]{{"UniformCostSearch"}});
    }

    @Parameterized.Parameter
    public String searchFunctionName;

    public <A, S> List<A> searchForActions(Problem<A, S> problem) {
        SearchForActionsFunction<A, S> searchForActionsFunction = new UniformCostSearch<>();
        return searchForActionsFunction.apply(problem);
    }

    @Test
    public void testBidirectionalSearch() {
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.RIMNICU_VILCEA),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.PITESTI),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST)),
                searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                        SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST)));
    }

}
