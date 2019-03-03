package aima.test.unit.search.uninformedsearch;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.basic.uninformedsearch.UniformCostSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class UniformCostSearchTest {
    UniformCostSearch uniformCostSearch = new UniformCostSearch();

    @Test
    public void testUniformCostSearch() {
        Assert.assertEquals(
                Arrays.asList(new GoAction(SimplifiedRoadMapOfPartOfRomania.SIBIU),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.RIMNICU_VILCEA),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.PITESTI),
                        new GoAction(SimplifiedRoadMapOfPartOfRomania.BUCHAREST)),
                uniformCostSearch.apply(ProblemFactory.getSimplifiedRoadMapOfPartOfRomaniaProblem(
                        SimplifiedRoadMapOfPartOfRomania.ARAD, SimplifiedRoadMapOfPartOfRomania.BUCHAREST)));
    }
}
