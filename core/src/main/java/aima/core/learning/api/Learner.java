package aima.core.learning.api;

import aima.core.learning.DataSet;

/**
 * Defines the requirements for an object that can be used to train a {@link Classifier}
 *
 * @author shantanusinghal
 */
public interface Learner {

  /**
   * Build a classifier model from the training data.
   *
   * @param dataSet the training input examples
   */
  Classifier train(DataSet dataSet);

}
