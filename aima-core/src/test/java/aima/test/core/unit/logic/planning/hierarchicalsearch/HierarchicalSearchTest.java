package aima.test.core.unit.logic.planning.hierarchicalsearch;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.hierarchicalsearch.HierarchicalSearchAlgorithm;
import org.junit.Test;

public class HierarchicalSearchTest {
    @Test
    public void hierarchicalSearchTest(){
        HierarchicalSearchAlgorithm algo = new HierarchicalSearchAlgorithm();
        for (ActionSchema action :
                algo.heirarchicalSearch(PlanningProblemFactory.spareTireProblem())) {
            System.out.println(action.toString());
        }
    }
}
