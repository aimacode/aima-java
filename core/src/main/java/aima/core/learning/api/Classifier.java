package aima.core.learning.api;

import aima.core.learning.DataSet;
import aima.core.learning.Example;
import java.util.Map;

/**
 * Defines the requirements for an object that can be used to predicts the value of a target
 * variable based on several input variables
 *
 * Use a {@link Learner} to build a classifier instance.
 *
 * @author shantanusinghal
 */
public interface Classifier {

  /**
   * Predicts the class value for given example(s)
   *
   * @param dataSet the collection of examples to be classified
   * @return a map of examples and their predicted (most likely) class
   */
  Map<Example, String> predict(DataSet dataSet);

}
