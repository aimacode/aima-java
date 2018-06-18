package aima.core.learning.knowledge;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 19.2, page
 * 771.<br>
 * <br>
 *
 * <pre>
 *
 * function Version-Space-Learning(examples) returns a version space
 *  local variables: V, the version space: the set of all hypotheses
 *
 *  V ← the set of all hypotheses
 *  for each example e in examples do
 *    if V is not empty then V ← Version-Space-Update(V, e)
 *  return V
 *
 * </pre>
 * <pre>
 *     function Version-Space-Update(V, e) returns an updated version space
 *           V ← {h ∈ V : h is consistent with e}
 * </pre>
 * <p>
 *     Figure 19.3 The version space learning algorithm. It
 *     finds a subset of V that is consistent with all the examples.
 * @author samagra
 */

public class VersionSpaceLearning {

    /**
     * function Version-Space-Learning(examples) returns a version space
     * @param examples
     * @return
     */
    public VersionSpace versionSpaceLearning(List<LogicalExample> examples) {
        // local variables: V, the version space: the set of all hypotheses
        //  V ← the set of all hypotheses
        VersionSpace v = new VersionSpace();
        // if V is not empty then V ← Version-Space-Update(V, e)
        for (LogicalExample e :
                examples) {
            if (v != null) {
                v = versionSpaceUpdate(v, e);
            }
        }
        //  return V
        return v;
    }

    /**
     * function Version-Space-Update(V, e) returns an updated version space
     * @param v
     * @param e
     * @return
     */
    private VersionSpace versionSpaceUpdate(VersionSpace v, LogicalExample e) {
        // V ← {h ∈ V : h is consistent with e}

        // False negative for S i : This means S i is too specific, so we replace it by all its immediate
        //generalizations, provided they are more specific than some member of G.
        if (e.getGoal() && !v.predictFromSpecialisedSet(e)) {
            v.setMostSpecificSet(v.immediateGeneralisation(v.getMostSpecificSet(), e));
        }

        //False positive for G i : This means G i is too general, so we replace it by all its immediate
        //specializations, provided they are more general than some member of S.
        if (!e.getGoal() && v.predictFromGeneralisedSet(e)) {
            v.setMostGeneralSet(v.immediateSpecialisation(v.getMostGeneralSet(), e));
        }
        return v;
    }
}
