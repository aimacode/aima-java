package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.uninformed.UniformCostSearch;
import aima.extra.search.pqueue.uninformed.GraphPriorityQueueSearch;
import aima.extra.search.pqueue.uninformed.GraphRLPriorityQueueSearch;
import aima.extra.search.pqueue.uninformed.TreePriorityQueueSearch;
import aima.extra.search.pqueue.uninformed.UniformCostQueueSearch;

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
        return Arrays.asList(new Object[][]{
        	{"core.UniformCostSearch"}, 
        	{"extra.UniformCostQueueSearch.graph"}, 
         	{"extra.UniformCostQueueSearch.graphrl"}, 
        	{"extra.UniformCostQueueSearch.tree"}
        });
    }

    @Parameterized.Parameter
    public String searchFunctionName;

    public <A, S> List<A> searchForActions(Problem<A, S> problem) {
        SearchForActionsFunction<A, S> searchForActionsFunction;
        if (searchFunctionName.equals("core.UniformCostSearch")) {
        	searchForActionsFunction = new UniformCostSearch<>();
        }
        else if (searchFunctionName.equals("extra.UniformCostQueueSearch.graph")){
        	searchForActionsFunction = new UniformCostQueueSearch<>(new GraphPriorityQueueSearch<>());
        }
        else if (searchFunctionName.equals("extra.UniformCostQueueSearch.graphrl")) {
        	searchForActionsFunction = new UniformCostQueueSearch<>(new GraphRLPriorityQueueSearch<>());
        }
        else if (searchFunctionName.equals("extra.UniformCostQueueSearch.tree")) {
        	searchForActionsFunction = new UniformCostQueueSearch<>(new TreePriorityQueueSearch<>());
        }
        else {
        	throw new UnsupportedOperationException();
        }
        
        return searchForActionsFunction.apply(problem);
    }

    @Test
    public void testUniformCostSearch() {
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.RIMNICU_VILCEA),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.PITESTI),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST)),
                searchForActions(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                        SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST)));
    }
}