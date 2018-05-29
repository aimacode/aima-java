package aima.test.core.unit.logic.planning.hierarchicalsearch;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.hierarchicalsearch.HierarchicalSearchAlgorithm;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author samagra
 */
public class HierarchicalSearchTest {
    @Test
    public void hierarchicalSearchTest() {
        HierarchicalSearchAlgorithm algo = new HierarchicalSearchAlgorithm();
        ActionSchema taxiAction = new ActionSchema("Taxi", null,
                "At(Home)",
                "~At(Home)^At(SFO)");
        for (ActionSchema action :
                algo.heirarchicalSearch(PlanningProblemFactory.goHomeToSFOProblem())) {
            Assert.assertEquals(action, taxiAction);
        }
    }
}
