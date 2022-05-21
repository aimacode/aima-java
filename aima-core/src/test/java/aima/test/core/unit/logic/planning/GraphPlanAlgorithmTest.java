package aima.test.core.unit.logic.planning;

import aima.core.logic.planning.*;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

/**
 * @author samagra
 */
public class GraphPlanAlgorithmTest {
    @Test
    public void spareTireTest() {
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();
        Problem spareTireProblem = PlanningProblemFactory.spareTireProblem();
        List<List<ActionSchema>> solution = algorithm.graphPlan(spareTireProblem);
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

    @Test
    public void levelOffTest() {
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();
        Problem stProblem = PlanningProblemFactory.spareTireProblem();
        State initialState = new State("Tire(Flat)^Tire(Spare)^At(Flat,Axle)"); //^At(Spare,Trunk)");
        Problem modifiedProblem = new Problem(initialState, stProblem.getGoalState(), stProblem.getActionSchemas());
        List<List<ActionSchema>> solution = algorithm.graphPlan(modifiedProblem);
        Assert.assertEquals(4, algorithm.getGraph().numLevels());
        Assert.assertNull(solution);

    }
}
