package aima.core.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;

import java.util.ArrayList;

/**
 * Created by shiri on 9/28/17.
 */
public abstract class SizeSpecifiedLearner implements Learner {
    private int size;

    public SizeSpecifiedLearner(int size, DataSet ds) {
        this.size = size;
        train(size, ds);
    }

    SizeSpecifiedLearner() {

    }

    abstract public void train(int size, DataSet trainingSet);
}
