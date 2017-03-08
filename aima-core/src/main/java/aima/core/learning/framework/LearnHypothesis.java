package aima.core.learning.framework;

import aima.core.learning.knowledge.Hypothesis;

/**
 * made by allen
 */
public interface LearnHypothesis {

    /**
     * @param complexity the complexity for the model of the hypothesis
     * @param trainingSample the dataset for the hypothesis to be based on
     *
     * @return returns hypothesis
     */
    Hypothesis makeHypothesis(int complexity, DataSet trainingSample);

    /**
     * @param hypothesis the hypothesis to use
     * @param sample the dataset to test on
     *
     * @return a double representing the error value
     */
    double errorRate(Object hypothesis, DataSet sample);

}
