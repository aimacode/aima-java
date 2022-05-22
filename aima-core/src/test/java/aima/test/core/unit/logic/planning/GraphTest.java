package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.planning.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Ruediger Lunde
 */
public class GraphTest {
    PlanningProblem problem;

    @Before
    public void setup() {
        problem = PlanningProblemFactory.spareTireProblem();
    }

    @Test
    public void expandTest() {
        Graph graph = new Graph(problem);
        Assert.assertEquals(1, graph.numLevels());
        // test literal level 0
        Level<Literal, ActionSchema> litLevel = graph.getLiteralLevel(0);
        Assert.assertNull(litLevel.getPrevLevel());
        List<Literal> expectedLevelObjects = Utils.parse(
                "Tire(Flat)^Tire(Spare)^At(Spare,Trunk)^At(Flat,Axle)"
        );
        List<Integer> expectedNextObjects = Arrays.asList(1, 1, 2, 2);
        Assert.assertTrue(litLevel.getLevelObjects().containsAll(expectedLevelObjects));
        for (int i = 0; i < expectedLevelObjects.size(); i++) {
            List<ActionSchema> linkedActions = litLevel.getLinkedPrevObjects(expectedLevelObjects.get(i));
            Assert.assertTrue(linkedActions != null && linkedActions.size() == 0);
            linkedActions = litLevel.getLinkedNextObjects(expectedLevelObjects.get(i));
            Assert.assertTrue(linkedActions != null && linkedActions.size() == expectedNextObjects.get(i));
        }
        Assert.assertTrue(litLevel.getMutexLinks() != null && litLevel.getMutexLinks().size() == 0);

        graph.expand(problem);
        Assert.assertEquals(2, graph.numLevels());

        // test action level 0
        Level<ActionSchema, Literal> actLevel = graph.getActionLevel(0);
        Assert.assertNotNull(actLevel.getPrevLevel());
        List<ActionSchema> actions = actLevel.getLevelObjects();
        Assert.assertEquals(7, actions.size()); // 4 NO-OP, 2 Remove, 1 LeaveOvernight
        int prevNull = 0;
        int nextNull = 0;
        for (ActionSchema action : actions) {
            if (actLevel.getLinkedPrevObjects(action) == null)
                prevNull++;
            if (actLevel.getLinkedNextObjects(action) == null)
                nextNull++;
        }
        Assert.assertEquals(1, prevNull); // currently, no prev links for actions without preconditions
        Assert.assertEquals(0, nextNull); // next links must be complete!
        Assert.assertEquals(5, actLevel.getMutexLinks().size());

        // test literal level 1
        litLevel = graph.getLiteralLevel(1);
        Assert.assertNotNull(litLevel.getPrevLevel());

        List<Literal> literals = litLevel.getLevelObjects();
        Assert.assertEquals(12, literals.size());
        for (Literal literal : literals) {
            Assert.assertNotNull(litLevel.getLinkedPrevObjects(literal));
            Assert.assertNotNull(litLevel.getLinkedNextObjects(literal));
        }
        Assert.assertEquals(10, litLevel.getMutexLinks().size());
    }
}
