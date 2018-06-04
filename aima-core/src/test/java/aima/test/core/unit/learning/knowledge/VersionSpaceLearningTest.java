package aima.test.core.unit.learning.knowledge;

import aima.core.learning.knowledge.KnowledgeLearningProblemFactory;
import aima.core.learning.knowledge.LogicalExample;
import aima.core.learning.knowledge.VersionSpace;
import aima.core.learning.knowledge.VersionSpaceLearning;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author samagra
 */

public class VersionSpaceLearningTest {
    @Test
    public void versionSpaceLearningTest() {
        VersionSpaceLearning algo = new VersionSpaceLearning();
        VersionSpace result = algo.versionSpaceLearning(KnowledgeLearningProblemFactory.getRestaurantLogicalExample());
        List<LogicalExample> examples = KnowledgeLearningProblemFactory.getRestaurantLogicalExample();
        int correct = 0;
        int wrong = 0;
        for (LogicalExample example : examples) {
            Assert.assertEquals(example.getGoal(), result.predict(example));
            if (example.getGoal() == result.predict(example))
                correct++;
            else
                wrong++;
        }
        Assert.assertEquals(12, correct);
        Assert.assertEquals(0, wrong);
    }
}
