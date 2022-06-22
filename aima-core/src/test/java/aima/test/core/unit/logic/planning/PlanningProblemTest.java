package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.logic.planning.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author samagra
 */
public class PlanningProblemTest {
    private PlanningProblem testProblem;

    @Before
    public void setup() {
        testProblem = PlanningProblemFactory.spareTireProblem();
    }

    @Test
    public void constructorTest() {
        State initState = new State("Tire(Flat)^Tire(Spare)^At(Flat,Axle)^At(Spare,Trunk)");
        List<Literal> goal = Utils.parse("At(Spare,Axle)");
        Variable obj = new Variable("obj");
        Variable loc = new Variable("loc");
        Variable t = new Variable("t");
        ArrayList<Term> removeVars = new ArrayList<>(Arrays.asList(obj, loc));
        ArrayList<Term> putOnVars = new ArrayList<>(Arrays.asList(t));
        ActionSchema removeAction = new ActionSchema("Remove", removeVars,
                "At(obj,loc)",
                "~At(obj,loc)^At(obj,Ground)");
        ActionSchema putOnAction = new ActionSchema("PutOn", putOnVars,
                "Tire(t)^At(t,Ground)^~At(Flat,Axle)",
                "~At(t,Ground)^At(t,Axle)");
        ActionSchema leaveOvernightAction = new ActionSchema("LeaveOvernight", null,
                "",
                "~At(Spare,Ground)^~At(Spare,Axle)^~At(Spare,Trunk)" +
                        "^~At(Flat,Ground)^~At(Flat,Axle)^~At(Flat,Trunk)");
        Assert.assertEquals(initState, testProblem.getInitialState());
        Assert.assertEquals(goal, testProblem.getGoal());
        Assert.assertEquals(3, testProblem.getActionSchemas().size());
        Assert.assertTrue(testProblem.getActionSchemas().contains(removeAction));
        Assert.assertTrue(testProblem.getActionSchemas().contains(putOnAction));
        Assert.assertTrue(testProblem.getActionSchemas().contains(leaveOvernightAction));
    }

    @Test
    public void constantTest() {
        List<Constant> testList = testProblem.getProblemConstants();
        Assert.assertEquals(5, testList.size());
        Assert.assertTrue(testList.contains(new Constant("Spare")));
        Assert.assertTrue(testList.contains(new Constant("Ground")));
        Assert.assertTrue(testList.contains(new Constant("Trunk")));
        Assert.assertTrue(testList.contains(new Constant("Flat")));
        Assert.assertTrue(testList.contains(new Constant("Axle")));
    }
}
