package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.planning.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author samagra
 */
public class LevelTest {
    Problem spareTireProblem;

    @Before
    public void setup() {
        spareTireProblem = PlanningProblemFactory.spareTireProblem();
    }

    @Test
    public void firstLevelTest() {
        Level firstLevel = new Level(null, spareTireProblem, "At(Spare,Trunk)^At(Flat,Axle) ^~At(Spare,Axle)^~At(Flat,Ground)^~At(Spare,Ground)");
        List<Literal> firstLevelExpected = Utils.parse("At(Spare,Trunk)^At(Flat,Axle)" +
                "^~At(Spare,Axle)^~At(Flat,Ground)^~At(Spare,Ground)");
        //test for level objects
        Assert.assertTrue(firstLevel.getLevelObjects().containsAll(firstLevelExpected));
        Assert.assertEquals(7, firstLevel.getLevelObjects().size());
        //test for next links
        Assert.assertEquals(2, firstLevel.getNextLinks().get(firstLevelExpected.get(1)).size());
        Assert.assertEquals(1, firstLevel.getNextLinks().get(firstLevelExpected.get(3)).size());
        ActionSchema removeAction = new ActionSchema("Remove", null,
                "At(Spare,Trunk)",
                "~At(Spare,Trunk)^At(Spare,Ground)");
        Assert.assertTrue(firstLevel.getNextLinks().get(firstLevelExpected.get(0)).contains(removeAction));
    }

    @Test
    public void secondLevelTest() {
        Level firstLevel = new Level(null, spareTireProblem, "At(Spare,Trunk)^At(Flat,Axle) ^~At(Spare,Axle)^~At(Flat,Ground)^~At(Spare,Ground)");
        Level secondLevel = new Level(firstLevel, spareTireProblem);
        //TODO:test for level objects
        Assert.assertEquals(10, secondLevel.getLevelObjects().size());//7 No-ops and three actions
        //TODO:test for next links
        /*for (Object object :
                secondLevel.getNextLinks().keySet()) {
            System.out.println("For=====   "+object.toString());
            for (Object obj :
                    secondLevel.getNextLinks().get(object)) {
                System.out.println(obj.toString());
            }
            System.out.println("*************");
        }*/
        Assert.assertEquals(10, secondLevel.getNextLinks().keySet().size());
        //TODO:Test for mutexes
        /*for (Object object :
        secondLevel.getMutexLinks().keySet()) {
            System.out.println("For=====   "+object.toString());
            for (Object obj :
                    secondLevel.getMutexLinks().get(object)) {
                System.out.println("With ==="+obj.toString());
            }
            System.out.println("*************");
        }*/
        //TODO:test for previous links
        /*for (Object object :
                secondLevel.getPrevLinks().keySet()) {
            System.out.println("For=====   " + object.toString());
            for (Object obj :
                    secondLevel.getPrevLinks().get(object)) {
                System.out.println(obj.toString());
            }
            System.out.println("*************");
        }*/

    }

    @Test
    public void thirdLevelTest() {
        Level firstLevel = new Level(null, spareTireProblem, "At(Spare,Trunk)^At(Flat,Axle) ^~At(Spare,Axle)^~At(Flat,Ground)^~At(Spare,Ground)");
        Level secondLevel = new Level(firstLevel, spareTireProblem);
        Level thirdLevel = new Level(secondLevel, spareTireProblem);
        //TODO:Test for level objects
        /*for (Object obj :
                thirdLevel.getLevelObjects()) {
            System.out.println(obj.toString());
        }
        */
        //TODO:Test for nextLinks
        /*for (Object object :
                thirdLevel.getNextLinks().keySet()) {
            System.out.println("For=====   "+object.toString());
            for (Object obj :
                    thirdLevel.getNextLinks().get(object)) {
                System.out.println(obj.toString());
            }
            System.out.println("*************");
        }*/
        //TODO:Test for mutex links
        /*for (Object object :
                thirdLevel.getMutexLinks().keySet()) {
            System.out.println("For=====   "+object.toString());
            for (Object obj :
                    thirdLevel.getMutexLinks().get(object)) {
                System.out.println("With ==="+obj.toString());
            }
            System.out.println("*************");
        }*/
        //TODO:test for prev links
        /*for (Object object :
                thirdLevel.getPrevLinks().keySet()) {
            System.out.println("For=====   " + object.toString());
            for (Object obj :
                    thirdLevel.getPrevLinks().get(object)) {
                System.out.println(obj.toString());
            }
            System.out.println("*************");
        }*/
    }
}
