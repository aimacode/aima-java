package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;
import aima.core.logic.planning.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ProblemTest {
    private Problem testProblem;

    @Before
    public void setup(){
        testProblem = PlanningProblemFactory.spareTireProblem();
    }

    @Test
    public void constructorTest(){
        State initState = new State("Tire(Flat)^Tire(Spare)^At(Flat,Axle)^At(Spare,Trunk)");
        State goalState = new State("At(Spare,Axle)");
        Variable obj = new Variable("obj");
        Variable loc = new Variable("loc");
        Variable t = new Variable("t");
        Constant Axle = new Constant("Axle");
        ArrayList removeVars = new ArrayList<>(Arrays.asList(obj, loc));
        ArrayList putOnVars = new ArrayList<>(Arrays.asList(t, Axle));
        ActionSchema removeAction = new ActionSchema("Remove", removeVars,
                "At(obj,loc)",
                "~At(obj,loc)^At(obj,Ground)");
        ActionSchema putOnAction = new ActionSchema("PutOn", putOnVars,
                "Tire(t)^At(t,Ground)^~At(Flat,Axle)",
                "At(t,Ground)^At(t,Axle)");
        ActionSchema leaveOvernightAction = new ActionSchema("LeaveOvernight", null,
                "",
                "~At(Spare,Ground)^~At(Spare,Axle)^At(Spare,Trunk)" +
                        "^~At(Flat,Ground)^~At(Flat,Axle)^~At(Flat,Trunk)");
        Assert.assertEquals(initState,testProblem.getInitialState());
        Assert.assertEquals(goalState,testProblem.getGoalState());
        Assert.assertEquals(3,testProblem.getActionSchemas().size());
        Assert.assertTrue(testProblem.getActionSchemas().contains(removeAction));
        Assert.assertTrue(testProblem.getActionSchemas().contains(putOnAction));
        Assert.assertTrue(testProblem.getActionSchemas().contains(leaveOvernightAction));
    }
}
