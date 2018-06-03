package aima.test.core.unit.learning.knowledge;

import aima.core.learning.knowledge.CurrentBestLearning;
import aima.core.learning.knowledge.Hypothesis;
import aima.core.learning.knowledge.KnowledgeLearningProblemFactory;
import aima.core.learning.knowledge.LogicalExample;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author samagra
 */
public class CurrentBestLearningTest {
    @Test
    public void currentBestLearningTest() {
        CurrentBestLearning algo = new CurrentBestLearning();
        HashMap<String, String> initial = new HashMap<>();
        initial.put("Alt", "Yes");
        Hypothesis initialHypothesis = new Hypothesis("WillWait", new ArrayList<>(Collections.singletonList(initial)));
        Hypothesis result = algo.currentBestLearning(KnowledgeLearningProblemFactory.getRestaurantLogicalExample(), initialHypothesis, new ArrayList<>());
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
