package aima.core.learning.knowledge;

import aima.core.util.math.permute.CombinationGenerator;
import aima.core.util.math.permute.PowerSetGenerator;

import java.util.*;

public class MinimalConsistentDet {
    public Set<String> minimalConsistentDet(List<LogicalExample> e, Set<String> a){
        for (int i = 0; i < a.size(); i++) {
            for (List<String> subset :
                    CombinationGenerator.generateCombinations(new ArrayList<>(a), i)) {
                if (consistentDet(subset,e)){
                    return new HashSet<>(subset);
                }
            }
        }
        return new HashSet<>();
    }

    public boolean consistentDet(List<String> attributes, List<LogicalExample> examples) {
        HashMap<List<String>,String> hashTable = new HashMap<>();
        for (LogicalExample e :
                examples) {
            List<String> attributeValues= new ArrayList<>();
            for (String attribute : attributes) {
                attributeValues.add(e.getAttributes().get(attribute));
            }
            if (hashTable.containsKey(attributeValues)){
                if (!hashTable.get(attributeValues).equals(e.getAttributes().get("Goal")))
                    return false;
            }
            hashTable.put(attributeValues,e.getAttributes().get("Goal"));
        }
        return true;
    }
}
