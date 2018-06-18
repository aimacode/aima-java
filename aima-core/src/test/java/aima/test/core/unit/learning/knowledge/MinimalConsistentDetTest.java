package aima.test.core.unit.learning.knowledge;

import aima.core.learning.knowledge.KnowledgeLearningProblemFactory;
import aima.core.learning.knowledge.LogicalExample;
import aima.core.learning.knowledge.MinimalConsistentDet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author samagra
 */
public class MinimalConsistentDetTest {
    @Test
    public void minimalConsistentDetTest(){
        HashSet<String> setOfString = new HashSet<>(Arrays.asList("Mass","Temperature","Material","Size"));
        List<LogicalExample> examples = KnowledgeLearningProblemFactory.getConductanceMeasurementProblem();
        MinimalConsistentDet algo = new MinimalConsistentDet();
        Assert.assertTrue(algo.minimalConsistentDet(examples, setOfString).
                containsAll(Arrays.asList("Temperature","Material")));
        Assert.assertEquals(2,algo.minimalConsistentDet(examples,setOfString).size());

    }
}
