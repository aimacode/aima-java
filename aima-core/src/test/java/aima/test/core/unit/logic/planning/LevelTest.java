package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.planning.Level;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;
import aima.core.logic.planning.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class LevelTest {
    Problem spareTireProblem;
    @Before
    public void setup(){
        spareTireProblem = PlanningProblemFactory.spareTireProblem();
    }

    @Test
    public void firstLevelTest(){
        Level firstLevel = new Level(null,spareTireProblem,"At(Spare,Trunk)^At(Flat,Axle) ^~At(Spare,Axle)^~At(Flat,Ground)^~At(Spare,Ground)");
        List<Literal> firstLevelExpected = Utils.parse("At(Spare,Trunk)^At(Flat,Axle)" +
                "^~At(Spare,Axle)^~At(Flat,Ground)^~At(Spare,Ground)");
        Assert.assertTrue(firstLevel.getLevelObjects().containsAll(firstLevelExpected));
        Assert.assertEquals(7,firstLevel.getLevelObjects().size());
        for (Object obj :
                firstLevel.getNextLinks().keySet()) {
            System.out.println("Main"+obj.toString());
            for (Object abc :
                    firstLevel.getNextLinks().get(obj)) {
                System.out.println(abc.toString());
            }
        }
        Assert.assertEquals(2,firstLevel.getNextLinks().get(firstLevelExpected.get(1)).size());
        Assert.assertEquals(1,firstLevel.getNextLinks().get(firstLevelExpected.get(3)).size());
    }
}
