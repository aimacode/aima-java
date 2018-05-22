package aima.test.core.unit.logic.planning;

import aima.core.logic.planning.Graph;
import aima.core.logic.planning.Level;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author samagra
 */
public class GraphTest {
    Problem problem;
    Level firstLevel, secondLevel, thirdLevel;

    @Before
    public void setup() {
        problem = PlanningProblemFactory.spareTireProblem();
        firstLevel = new Level(null, problem, "At(Spare,Trunk)^At(Flat,Axle) ^~At(Spare,Axle)^~At(Flat,Ground)^~At(Spare,Ground)");
        secondLevel = new Level(firstLevel, problem);
        thirdLevel = new Level(thirdLevel, problem);
    }

    @Test
    public void addLevelTest() {
        Graph graph = new Graph(problem, firstLevel);
        graph.addLevel();
        Assert.assertEquals(2, graph.getLevels().size());
        graph.addLevel();
        Assert.assertEquals(3, graph.getLevels().size());
    }
}
