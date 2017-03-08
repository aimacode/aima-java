package aima.core.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.LearnHypothesis;
import aima.core.learning.knowledge.Hypothesis;

import java.util.ArrayList;
import java.util.List;

/**
 * made by allen
 */
public class CrossValidationWrapper<T extends LearnHypothesis> {

    /**
     * @param learner an object that is able to return a trained learner and the error values on a sample
     * @param size complexity for learner
     * @param k split examples into k groups fro cross validation
     * @param examples data to train/test on
     *
     * @return a tuple having the average training set and validation set error
     */
    private Tuple<Double, Double> crossValidation(T learner, int size, int k, DataSet examples) {

        //fold errT ← 0; fold errV ← 0
        Double foldErrT = 0.0;
        Double foldErrV = 0.0;

        //for fold = 1 to k do
        for (int fold=1; fold<=k; fold++) {

            //training set, validation set ← PARTITION(examples,fold, k)
            Tuple<DataSet, DataSet> partitionTuple = partition(examples, fold, k);
            DataSet trainingSets = partitionTuple.getValue1();
            DataSet testSet = partitionTuple.getValue2();

            //h ←Learner (size,training set)
            Hypothesis h = learner.makeHypothesis(size, examples);

            //  fold errT ← fold errT + ERROR-RATE(h,training set)
            foldErrT += learner.errorRate(h, trainingSets);
            //  fold errV ← fold errV +ERROR-RATE(h, validation set)
            foldErrV += learner.errorRate(h, testSet);
        }

        //return fold errT /k, fold errV /k
        return new Tuple<>(foldErrT/k, foldErrV/k);
    }


    /**
     * @param learner an object that is able to return a trained learner and the error values on a sample
     * @param k split examples into k groups fro cross validation
     * @param examples data to train/test on
     *
     * @return a hypothesis
     */
    // may not be guaranteed to terminate because of the psuedo code has from a loop from 1 to inf

    public Hypothesis crossValidationWrapper(T learner, int k, DataSet examples) {

        //local variables:
        //errT , an array, indexed by size, storing training-set error rates
        List<Double> errT = new ArrayList<>();
        //errV , an array, indexed by size, storing validation-set error rates
        List<Double> errV = new ArrayList<>();

        //for fold = 1 to inf do
        int fold = 0;
        // while (size<Double.POSITIVE_INFINITY) is equivalent to while(true)
        while (true) {
            //errT[size], errV [size] ← CROSS-VALIDATION(Learner , size, k, examples)
            Tuple<Double, Double> crossValidationTuple = crossValidation(learner, fold, k, examples);
            errT.add(crossValidationTuple.getValue1());
            errV.add(crossValidationTuple.getValue2());
            fold++;

            //if errT has converged then do
            if (fold != 1 &&(errT.get(fold-1) - errT.get(fold-2) < 1.0E-8)) {

                //best size← the value of size with minimum errV [size]
                double min = Double.POSITIVE_INFINITY;
                int bestSize = 0;

                for (int i=0; i<fold-1; i++) {
                    if (errV.get(i) < min) {
                        min = errV.get(i);
                        bestSize = i;
                    }
                }
                //return Learner (best size, examples)
                return learner.makeHypothesis(bestSize, examples);
            }
        }
    }

    // supporting code

    /**
     * @param examples data to train/test on
     * @param fold takes the 'fold-th' group from the k groups
     * @param k split examples into k groups fro cross validation
     *
     * @return returns a tuple of 2 datasets, the second being the examples in the
     *          'fold-th' group and the first being everything else
     */

    private Tuple<DataSet, DataSet> partition(DataSet examples, int fold, int k) {
        DataSet trainingSet = new DataSet(new DataSetSpecification());
        DataSet testSet = new DataSet(new DataSetSpecification());

        int N = examples.size();
        for (int i=0; i<N; i++) {
            if (i%k == fold) {
                testSet.add(examples.getExample(i));
            } else {
                trainingSet.add(examples.getExample(i));
            }
        }

        return new Tuple<>(trainingSet, testSet);
    }

    private final class Tuple<X, Y> {
        private final X value1;
        private final Y value2;

        Tuple(X value1, Y value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        X getValue1() { return value1; }
        Y getValue2() { return value2; }
    }

}
