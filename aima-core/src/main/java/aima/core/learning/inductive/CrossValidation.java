package aima.core.learning.inductive;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.Example;
import aima.core.learning.learners.SampleParameterizedLearner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 10.9 page
 * </br>
 *
 * <pre>
 *     function CROSS-VALIDATION-WRAPPER(Learner, k, examples) returns a hypothesis
 *  local variables: errT, an array, indexed by size, storing training-set error rates
 *         errV, an array, indexed by size, storing validation-set error rates
 *  for size = 1 to ∞ do
 *    errT[size], errV[size] ← CROSS-VALIDATION(Learner, size, k, examples)
 *    if errT has converged then do
 *      best_size ← the value of size with minimum errV[size]
 *      return Learner(best_size, examples)
 *
 * function CROSS-VALIDATION(Learner, size, k, examples) returns two values:
 *  fold_errT ← 0; fold_errV ← 0
 *  for fold = 1 to k do
 *    training_set, validation_set ← PARTITION(examples, fold, k)
 *    h ← Learner(size, training_set)
 *    fold_errT ← fold_errT + ERROR-RATE(h, training_set)
 *    fold_errV ← fold_errV + ERROR-RATE(h, validation_set)
 *  return fold_errT ⁄ k, fold_errV ⁄ k
 *     </pre>
 * <p>
 * An algorithm to select the model that has the lowest error rate on validation
 * data by building models of increasing complexity, and choosing the one with
 * best empirical error rate on validation data. Here errT means error rate
 * on the training data, and errV means error rate on the validation data.
 * Learner(size, exmaples) returns a hypothesis whose complexity is set by
 * the parameter size, and which is trained on the examples.
 * PARTITION(examples, fold, k) splits examples into two subsets:
 * a validation set of size N ⁄ k and a training set with all the other examples. The split is different for each value of fold.
 * </p>
 *
 * @author samagra
 */
public class CrossValidation {
    double minErrT;

    public CrossValidation(double minErrT) {
        this.minErrT = minErrT;
    }

    /**
     * function CROSS-VALIDATION-WRAPPER(Learner, k, examples) returns a hypothesis
     *
     * @param learner
     * @param k
     * @param examples
     * @return
     */
    public SampleParameterizedLearner crossValidationWrapper(SampleParameterizedLearner learner, int k, DataSet examples) {
        //local variables: errT, an array, indexed by size, storing training-set error rates
        //   errV, an array, indexed by size, storing validation-set error rates
        List<Double> errT = new ArrayList<>();
        List<Double> errV = new ArrayList<>();
        double[] crossValResult;
        //for size = 1 to ∞ do
        for (int size = 0; ; size++) {
            //errT[size], errV[size] ← CROSS-VALIDATION(Learner, size, k, examples)
            crossValResult = crossValidation(learner, size, k, examples);
            errT.add(crossValResult[0]);
            errV.add(crossValResult[1]);
            // if errT has converged then do
            if (hasConverged(errT.get(size))) {
                // best_size ← the value of size with minimum errV[size]
                int best_size = errV.indexOf(Collections.min(errV));
                //return Learner(best_size, examples)
                learner.train(best_size, examples);
                return learner;
            }
        }
    }

    /**
     * This method checks if the training has converged.
     * Training is assumed to converge if the error comes below a certain minimum error.
     *
     * @param errT The error of the current training.
     * @return boolean representing the status of convergence
     */
    private boolean hasConverged(double errT) {
        return errT < minErrT;
    }

    /**
     * function CROSS-VALIDATION(Learner, size, k, examples) returns two values:
     *
     * @param learner
     * @param size
     * @param k
     * @param examples
     * @return
     */
    public double[] crossValidation(SampleParameterizedLearner learner, int size, int k, DataSet examples) {
        // fold_errT ← 0; fold_errV ← 0
        double[] foldErr = new double[2];
        foldErr[0] = 0; // represents fold_errT
        foldErr[1] = 0; // represents fold_errV
        //temp vars
        DataSet trainingSet, validationSet;
        DataSet[] temp;
        // for fold = 1 to k do
        for (int fold = 0; fold < k; fold++) {
            // training_set, validation_set ← PARTITION(examples, fold, k)
            temp = partition(examples, fold, k);
            trainingSet = temp[0];
            validationSet = temp[1];
            // h ← Learner(size, training_set)
            learner.train(size, trainingSet);
            //fold_errT ← fold_errT + ERROR-RATE(h, training_set)
            foldErr[0] = foldErr[0] + errorRate(learner.test(trainingSet));
            // fold_errV ← fold_errV + ERROR-RATE(h, validation_set)
            foldErr[1] = foldErr[1] + errorRate(learner.test(validationSet));
        }
        //  return fold_errT ⁄ k, fold_errV ⁄ k
        foldErr[0] = foldErr[0] / k;
        foldErr[1] = foldErr[1] / k;
        return foldErr;
    }

    /**
     * Calculates error rate for a particular dataset
     *
     * @param test
     * @return
     */
    private double errorRate(int[] test) {
        return Math.abs((double) (test[0] - test[1])) / ((double) 100);
    }

    /**
     * PARTITION(examples, fold, k) splits examples into two subsets:
     * a validation set of size N ⁄ k and a training set with all the other examples.
     * The split is different for each value of fold.
     *
     * @param examples
     * @param fold
     * @param k
     * @return
     */
    private DataSet[] partition(DataSet examples, int fold, int k) {
        List<Example> exampleList = examples.examples;
        ArrayList<Integer> temp = new ArrayList<>();
        List<Example> trainingExamples = new ArrayList<>();
        List<Example> validationExamples = new ArrayList<>();
        for (int i = 0; i < exampleList.size(); i++) {
            temp.add(i);
        }
        Collections.shuffle(temp);
        for (int i = 0; i < temp.size(); i++) {
            if (i < k)
                validationExamples.add(exampleList.get(temp.get(i)));
            else
                trainingExamples.add(exampleList.get(temp.get(i)));
        }
        DataSet trainingSet = new DataSet(new DataSetSpecification());
        DataSet validationSet = new DataSet(new DataSetSpecification());
        trainingSet.examples = trainingExamples;
        validationSet.examples = validationExamples;
        DataSet[] result = {trainingSet, validationSet};
        return result;
    }
}
