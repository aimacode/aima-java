package aima.core.learning.knowledge;


import java.util.HashMap;
import java.util.List;

/**
 * @author samagra
 */
public class LogicalExample {
    HashMap<String, String> attributes = new HashMap<>();
    boolean goal;

    public LogicalExample(List<String> attributeNames, List<String> attributeValues, boolean goal) throws Exception {
        if (attributeNames.size() != attributeValues.size())
            throw new Exception("Inconsistent example");
        for (int i = 0; i < attributeNames.size(); i++) {
            attributes.put(attributeNames.get(i), attributeValues.get(i));
        }
        this.goal = goal;
    }

    public LogicalExample(List<String> attributeNames,List<String> attributeValus) throws Exception {
        this(attributeNames,attributeValus,false);
    }

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public boolean getGoal() {
        return goal;
    }
}
