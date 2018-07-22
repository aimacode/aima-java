package aima.core.learning.inductive;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.framework.DataSet.PartitionOfSets;
import aima.core.learning.learners.SizeSpecifiedLearner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by shiri on 9/28/17.
 */
public class CrossValidationWrapper {

    private static double fakeConvergeFactor = 0.01; //TODO: let user decide

    private class ErrorTuple {
        Double errTraining;
        Double errValidation;

        public ErrorTuple(Double errTraining, Double errValidation) {
            this.errTraining = errTraining;
            this.errValidation = errValidation;
        }
    }

    //example set of size N will be split into a training set of N/k and a validation set
    //of size N - N/k.
    public SizeSpecifiedLearner crossValidationWrapper(Learner learner, int k, DataSet ds) {
        ArrayList<Double> errT = new ArrayList<>(); //TODO: no need to keep an array of these values.
        ArrayList<Double> errV = new ArrayList<>();
        int size = 1;
        while (true) {
            ErrorTuple errorTuple = crossValidation((SizeSpecifiedLearner)learner, size, k, ds);
            if (hasConverged(errorTuple.errTraining, errT)) {
                break;
            }
            errT.add(errorTuple.errTraining);
            errV.add(errorTuple.errValidation);
            size++;
        }
        int bestSize =  IntStream.range(0,errV.size()).boxed()
                .min(Comparator.comparingDouble(errV::get))
                .get() + 1; //index starts at 0, size starts at 1
        ((SizeSpecifiedLearner)learner).train(bestSize, ds);
        return ((SizeSpecifiedLearner)learner);
    }

    private boolean hasConverged(Double errTraining, ArrayList<Double> errT) {
        if (Math.abs(errT.get(errT.size()-1) - errTraining) < fakeConvergeFactor)
            return true;
        return false;
    }

    private ErrorTuple crossValidation(SizeSpecifiedLearner learner, int size, int k, DataSet ds) {
        double foldErrorT = 0;
        double foldErrorV = 0;
        for (int fold = 1; fold <= k; fold++) {
            PartitionOfSets sets = ds.createPartitionOfDataSet(fold, k);
            learner.train(size, sets.getTrainingSet());
            foldErrorT += errorRate(learner, sets.getTrainingSet());
            foldErrorV += errorRate(learner, sets.getValidationSet());

        }
        return new ErrorTuple(foldErrorT/k, foldErrorV/k);
    }

    private double errorRate(Learner learner, DataSet ds) {
        //taken from AdaBoostLearner.java
        //TODO: allow adjusting of exampleWeights, currently all weights are 1
        double error = ds.examples
                .stream()
                .filter(e -> !(learner.predict(e).equals(e.targetValue())))
                .mapToInt(example -> 1)
                .sum();
        return error/ds.examples.size();
    }

}
