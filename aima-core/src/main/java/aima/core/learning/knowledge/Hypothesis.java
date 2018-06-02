package aima.core.learning.knowledge;

import java.util.ArrayList;
import java.util.Collections;
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
            String value = example.getAttributes().get(attributeName);
            if (value.charAt(0)=='!'){
                if(conjunction.get(attributeName).equals(value.substring(1)))
                    return false;
            }
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

    public String getGoal() {
        return goal;
    }

    public List<HashMap<String, String>> getHypothesis() {
        return hypothesis;
    }

    public List<Hypothesis> specialisations(List<LogicalExample> examplesSoFar) {
        List<Hypothesis> result = new ArrayList<>();
        for (HashMap<String, String> conjunction :
                this.getHypothesis()) {
            for (LogicalExample example :
                    examplesSoFar) {
                for (String attributeName :
                        example.getAttributes().keySet()) {
                    if (conjunction.containsKey(attributeName))
                        continue;
                    HashMap<String,String> tempConjunction = new HashMap<>(conjunction);
                    tempConjunction.put(attributeName,"!"+example.getAttributes().get(attributeName));
                    Hypothesis tempHypothesis = new Hypothesis(this.getGoal(),new ArrayList<>(this.getHypothesis()));
                    tempHypothesis.getHypothesis().remove(conjunction);
                    tempHypothesis.getHypothesis().add(tempConjunction);
                    if (tempHypothesis.isConsistent(examplesSoFar))
                        result.add(tempHypothesis);
                }
            }
        }
        Collections.shuffle(result);
        return result;
    }

    private boolean isConsistent(List<LogicalExample> examplesSoFar) {
        for (LogicalExample example :
                examplesSoFar) {
            if (!this.isConsistent(example))
                return false;
        }
        return true;
    }

    public List<Hypothesis> generalisations(List<LogicalExample> examplesSoFar){
        List<Hypothesis> result = new ArrayList<>();
        return result;
    }
}
