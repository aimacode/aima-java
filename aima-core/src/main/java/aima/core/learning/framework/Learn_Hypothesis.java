package aima.core.learning.framework;

import aima.core.learning.knowledge.Hypothesis;

/**
 * made by allen
 */
public interface Learn_Hypothesis {

    /**
     * @param complexity the complexity for the model of the hypothesis
     * @param training_sample the dataset for the hypothesis to be based on
     *
     * @return returns hypothesis
     */
    Hypothesis make_hypothesis(int complexity, DataSet training_sample);

    /**
     * @param hypothesis the hypothesis to use
     * @param sample the dataset to test on
     *
     * @return a double representing the error value
     */
    double error_rate(Object hypothesis, DataSet sample);

}
