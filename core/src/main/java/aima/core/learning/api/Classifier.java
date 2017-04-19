package aima.core.learning.api;

import aima.core.learning.data.DataSet;
import java.util.List;
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
   * Predicts the class value for the given example
   *
   * @param example the example to be classified
   * @return the predicted (most likely) class value for the input example
   */
  String predict(Example example);

  /**
   * Predicts the class value(s) for all examples in the input dataset
   *
   * @param dataSet the collection of examples to be classified
   * @return a map of examples and their predicted (most likely) class value
   */
  Map<Example, String> predict(DataSet dataSet);

}
