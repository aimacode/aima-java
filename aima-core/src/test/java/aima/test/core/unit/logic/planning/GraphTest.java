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
 * @author Ruediger Lunde
 */
public class GraphTest {
    Problem problem;

    @Before
    public void setup() {
        problem = PlanningProblemFactory.spareTireProblem();
    }

    @Test
    public void expandTest() {
        Graph graph = new Graph(problem);
        graph.expand(problem);
        Assert.assertEquals(3, graph.getLiteralLevels().size());
    }
}
