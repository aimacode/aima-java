package aima.test.core.unit.logic.planning;

import aima.core.logic.planning.Level;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;
import org.junit.Before;
import org.junit.Test;

public class LevelTest {
    Problem spareTireProblem;
    @Before
    public void setup(){
        spareTireProblem = PlanningProblemFactory.spareTireProblem();
    }

    @Test
    public void firstLevelTest(){
        Level firstLevel = new Level(null,spareTireProblem);
    }
}
