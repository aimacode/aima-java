package aima.core.learning.knowledge;

import aima.core.util.math.permute.CombinationGenerator;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 19.2, page
 * 771.<br>
 * <br>
 *
 * <pre>
 *
 * function Minimal-Consistent-Det(E, A) returns a set of attributes
 *  inputs: E, a set of examples
 *     A, a set of attributes, of size n
 *
 *  for i = 0 to n do
 *   for each subset Ai of A of size i do
 *     if Consistent-Det?(Ai, E) then return Ai
 * </pre>
 * <pre>
 * function Consistent-Det?(A, E) returns a truth value
 *  inputs: A, a set of attributes
 *      E, a set of examples
 *  local variables: H, a hash table
 *
 *  for each example e in E do
 *    if some example in H has the same values as e for the attributes A
 *     but a different classification then return false
 *    store the class of e in_H_, indexed by the values for attributes A of the example e
 *  return true
 * </pre>
 * <p>
 * Figure ?? An algorithm for finding a minimal consistent determination.
 *
 * @author samagra
 */
public class MinimalConsistentDet {

    /**
     * function Minimal-Consistent-Det(E, A) returns a set of attributes
     *
     * @param e
     *  inputs: E, a set of examples
     * @param a
     *      A, a set of attributes, of size n
     * @return
     */
    public Set<String> minimalConsistentDet(List<LogicalExample> e, Set<String> a) {
        // for i = 0 to n do
        for (int i = 0; i < a.size(); i++) {
            // for each subset Ai of A of size i do
            for (List<String> subset :
                    CombinationGenerator.generateCombinations(new ArrayList<>(a), i)) {
                // if Consistent-Det?(Ai, E) then return Ai
                if (consistentDet(subset, e)) {
                    return new HashSet<>(subset);
                }
            }
        }
        return new HashSet<>();
    }

    /**
     * function Consistent-Det?(A, E) returns a truth value
     *
     * @param attributes
     * inputs: A, a set of attributes
     * @param examples
     *      E, a set of examples
     * @return
     */
    public boolean consistentDet(List<String> attributes, List<LogicalExample> examples) {
        //  local variables: H, a hash table
        HashMap<List<String>, String> hashTable = new HashMap<>();
        // for each example e in E do
        for (LogicalExample e :
                examples) {
            List<String> attributeValues = new ArrayList<>();
            for (String attribute : attributes) {
                attributeValues.add(e.getAttributes().get(attribute));
            }
            // if some example in H has the same values as e for the attributes A
            if (hashTable.containsKey(attributeValues)) {
                // but a different classification then return false
                if (!hashTable.get(attributeValues).equals(e.getAttributes().get("Goal")))
                    return false;
            }
            // store the class of e in_H_, indexed by the values for
            // attributes A of the example e
            hashTable.put(attributeValues, e.getAttributes().get("Goal"));
        }
        //  return true
        return true;
    }
}
