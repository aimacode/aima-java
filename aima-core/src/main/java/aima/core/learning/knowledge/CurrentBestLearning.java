package aima.core.learning.knowledge;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 19.2, page
 * 771.<br>
 * <br>
 *
 * <pre>
 * function CURRENT-BEST-LEARNING(examples, h) returns a hypothesis or fail
 *
 *   if examples is empty then
 *      return h
 *   e <- FIRST(examples)
 *   if e is consistent with h then
 *      return CURRENT-BEST-LEARNING(REST(examples), h)
 *   else if e is a false positive for h then
 *     for each h' in specializations of h consistent with examples seen so far do
 *       h'' <- CURRENT-BEST-LEARNING(REST(examples), h')
 *       if h'' != fail then return h''
 *   else if e is a false negative for h then
 *     for each h' in generalization of h consistent with examples seen so far do
 *       h'' <- CURRENT-BEST-LEARNING(REST(examples), h')
 *       if h'' != fail then return h''
 *   return fail
 * </pre>
 * <p>
 * Figure 19.2 The current-best-hypothesis learning algorithm. It searches for a
 * consistent hypothesis that fits all the examples and backtracks when no
 * consistent specialization/generalization can be found. To start the
 * algorithm, any hypothesis can be passed in; it will be specialized or
 * generalized as needed.
 *
 * @author Ciaran O'Reilly
 * @author samagra
 */
public class CurrentBestLearning {

    /**
     * function CURRENT-BEST-LEARNING(examples, h) returns a hypothesis or fail
     *
     * @param examples
     * @param h
     * @param examplesSoFar
     * @return
     */
    public Hypothesis currentBestLearning(List<LogicalExample> examples, Hypothesis h, List<LogicalExample> examplesSoFar) {
        // if examples is empty then
        if (examples.isEmpty()) {
            //return h
            return h;
        }
        // e <- FIRST(examples)
        LogicalExample e = examples.get(0);
        examplesSoFar.add(e);
        // if e is consistent with h then
        if (h.isConsistent(e)) {
            // return CURRENT-BEST-LEARNING(REST(examples), h)
            return currentBestLearning(examples.subList(1, examples.size()), h, examplesSoFar);
        }
        // else if e is a false positive for h then
        else if ((h.predict(e)) && (!e.getGoal())) {
            // for each h' in specializations of h consistent with examples seen so far do
            for (Hypothesis hypothesis :
                    h.specialisations(examplesSoFar)) {
                if (hypothesis.isConsistent(examplesSoFar)) {
                    // h'' <- CURRENT-BEST-LEARNING(REST(examples), h')
                    Hypothesis newHypothesis = currentBestLearning(examples.subList(1, examples.size())
                            , hypothesis, examplesSoFar);
                    // if h'' != fail then return h''
                    if (newHypothesis != null) {
                        return newHypothesis;
                    }
                }
            }
        }
        // else if e is a false negative for h then
        else if ((!h.predict(e)) && (e.getGoal())) {
            // for each h' in generalization of h consistent with examples seen so far do
            for (Hypothesis hypothesis :
                    h.generalisations(examplesSoFar)) {
                if (hypothesis.isConsistent(examplesSoFar)) {
                    // h'' <- CURRENT-BEST-LEARNING(REST(examples), h')
                    Hypothesis newHypothesis = currentBestLearning(examples.subList(1, examples.size()),
                            hypothesis, examplesSoFar);
                    // if h'' != fail then return h''
                    if (newHypothesis != null) {
                        return newHypothesis;
                    }
                }
            }
        }
        // return fail
        return null;
    }
}
