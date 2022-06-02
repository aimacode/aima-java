package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.logic.planning.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author Ruediger Lunde
 */
public class GraphPlanAlgorithmTest {
    @Test
    public void spareTireTest() {
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();
        PlanningProblem spareTireProblem = PlanningProblemFactory.spareTireProblem();
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
        PlanningProblem stProblem = PlanningProblemFactory.spareTireProblem();
        State initialState = new State("Tire(Flat)^Tire(Spare)^At(Flat,Axle)");
        PlanningProblem modifiedProblem = new PlanningProblem(initialState,
                stProblem.getGoal(), stProblem.getActionSchemas());
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();
        List<List<ActionSchema>> solution = algorithm.graphPlan(modifiedProblem);
        Assert.assertEquals(4, algorithm.getGraph().numLevels());
        Assert.assertNull(solution);
    }

    @Test
    public void negativeLiteralsNeededInFirstGraphLevel() {
        State initialState = new State("Device(Radio1)");
        State goalState = new State("On(Radio1)");
        Variable d = new Variable("d");
        ActionSchema switchOnAction = new ActionSchema("switch-on", List.of(d),
                "Device(d)^~On(d)",
                "On(d)");
        PlanningProblem problem = new PlanningProblem(initialState, goalState.getFluents(), switchOnAction);
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();
        List<List<ActionSchema>> solution = algorithm.graphPlan(problem);
        Assert.assertEquals(1, solution.size());
    }


}
