package aima.core.learning.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 * @author samagra
 */
public class Hypothesis {
    private String goal;
    private List<HashMap<String, String>> hypothesis = new ArrayList<>(); //Treated as conjunction of literals

    public Hypothesis(String goal, List<HashMap<String, String>> attributes) {
        this.goal = goal;
        this.hypothesis = attributes;
    }

    public boolean isConsistent(LogicalExample example) {
        return example.getGoal() == this.predict(example);
    }

    public boolean predict(LogicalExample example) {
        for (HashMap<String, String> conjunction :
                hypothesis) {
            if (satisfiesConjunction(example, conjunction))
                return true;
        }
        return false;
    }

    private boolean satisfiesConjunction(LogicalExample example, HashMap<String, String> conjunction) {
        for (String attributeName :
                conjunction.keySet()) {
            if (!example.getAttributes().get(attributeName).equals(conjunction.get(attributeName)))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("FORALL x\t");
        result.append(this.goal).append("(x)\t<=>\t");
        for (HashMap<String, String> conjunction :
                this.hypothesis) {
            for (String s :
                    conjunction.keySet()) {
                result.append(s).append("==").append(conjunction.get(s)).append("^");
            }
            result.append("\n").append("||");
        }
        return result.toString();
    }
}
