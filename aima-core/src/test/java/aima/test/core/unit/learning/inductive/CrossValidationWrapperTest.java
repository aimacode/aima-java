package aima.test.core.unit.learning.inductive;

import aima.core.learning.framework.DataSetFactory;
import aima.core.learning.inductive.CrossValidation;
import aima.core.learning.learners.SampleParameterizedLearner;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author samagra
 */
public class CrossValidationWrapperTest {
    @Test
    public void crossValidationWrapperTest() {
        CrossValidation validation = new CrossValidation(0.05);
        try {
            //This learner gives least error when size param is 70
            SampleParameterizedLearner result = validation.crossValidationWrapper(new SampleParameterizedLearner(), 5, DataSetFactory.getRestaurantDataSet());
            Assert.assertEquals(70, result.getParameterSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
