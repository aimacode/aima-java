package aima.core.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;

/**
 * This class is created just for the testing of cross validation wrapper.
 * It does not implement any kind of listener.
 *
 * @author samagra
 */
public class SampleParameterizedLearner implements Learner {
    int parameterSize;
    boolean alpha = true;

    @Override
    public void train(DataSet ds) {

    }

    public int getParameterSize() {
        return parameterSize;
    }

    public void train(int size, DataSet dataSet) {
        parameterSize = size;
        train(dataSet);
    }

    @Override
    public String predict(Example e) {
        return null;
    }

    @Override
    public int[] test(DataSet ds) {
        int[] res = new int[2];
        if (alpha) {
            res[0] = 100;
            res[1] = parameterSize;
        } else {
            res[0] = 70;
            res[1] = parameterSize;
        }
        alpha = !alpha;
        return res;
    }
}
