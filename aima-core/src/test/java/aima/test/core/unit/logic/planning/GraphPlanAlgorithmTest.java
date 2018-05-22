package aima.test.core.unit.logic.planning;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.GraphPlanAlgorithm;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

/**
 * @author samagra
 */
public class GraphPlanAlgorithmTest {
    @Test
    public void test() {
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();
        Problem spareTireProblem = PlanningProblemFactory.spareTireProblem();
        List<List<ActionSchema>> solution = algorithm.graphPlan(spareTireProblem);
        Collections.reverse(solution);
        ActionSchema removeSpareTrunk = new ActionSchema("Remove", null,
                "At(Spare,Trunk)",
                "~At(Spare,Trunk)^At(Spare,Ground)");
        ActionSchema removeFlatAxle = new ActionSchema("Remove", null,
                "At(Flat,Axle)",
                "~At(Flat,Axle)^At(Flat,Ground)");
        ActionSchema putOnSpareAxle = new ActionSchema("PutOn", null,
                "Tire(Spare)^At(Spare,Ground)^~At(Flat,Axle)",
                "~At(Spare,Ground)^At(Spare,Axle)");
        Assert.assertEquals(2, solution.size());
        Assert.assertTrue(solution.get(0).contains(removeFlatAxle));
        Assert.assertTrue(solution.get(0).contains(removeSpareTrunk));
        Assert.assertTrue(solution.get(1).contains(putOnSpareAxle));
    }
}
