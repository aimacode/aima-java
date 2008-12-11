package aima.learning.knowledge;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 19.2, page 681.
 * 
 * <code>
 * function CURRENT-BEST-LEARNING(examples) returns a hypothesis
 * 
 *   H <- any hypothesis consistent with the first example in examples.
 *   for each remaining example in examples do
 *     if e is false positive for H then
 *       H <- choose a specialization of H consistent with examples
 *     else if e is false negative for H then
 *       H <- choose a generalization of H consistent with examples
 *     if no consistent specialization/generalization can be found then fail
 *   return H
 * </code>
 * 
 * Figure 19.2 The current-best-hypothesis learning algorithm. It searches for a consistent
 * hypothesis and backtracks when no consistent specialization/generalization can be found.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CurrentBestLearning {

}
