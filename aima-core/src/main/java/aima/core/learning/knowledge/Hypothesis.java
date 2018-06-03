package aima.core.learning.knowledge;

import aima.core.util.math.permute.PowerSetGenerator;

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
            String value = conjunction.get(attributeName);
            if (value.charAt(0) == '!') {
                if (example.getAttributes().get(attributeName).equals(value.substring(1))) {
                    return false;
                }
            } else if (!example.getAttributes().get(attributeName).equals(conjunction.get(attributeName))) {
                return false;
            }
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
        LogicalExample lastExample = examplesSoFar.get(examplesSoFar.size() - 1);


        for (String key :
                lastExample.getAttributes().keySet()) {
            List<HashMap<String, String>> satisfiedDisjuncts = new ArrayList<>();
            for (HashMap<String, String> disjunct :
                    this.getHypothesis()) {
                if (this.satisfiesConjunction(lastExample, disjunct))
                    satisfiedDisjuncts.add(new HashMap<>(disjunct));
            }
            List<HashMap<String, String>> tempDisjuncts = new ArrayList<>(this.getHypothesis());
            tempDisjuncts.removeAll(satisfiedDisjuncts);
            for (HashMap<String, String> falseDisjunct :
                    satisfiedDisjuncts) {
                if (falseDisjunct.containsKey(key))
                    continue;
                else
                    falseDisjunct.put(key, "!" + lastExample.getAttributes().get(key));
            }
            tempDisjuncts.addAll(new ArrayList<>(satisfiedDisjuncts));
            Hypothesis newHypo = new Hypothesis(this.getGoal(), new ArrayList<>(tempDisjuncts));
            result.add(newHypo);
        }
        Collections.shuffle(result);
        return result;
    }

    public boolean isConsistent(List<LogicalExample> examplesSoFar) {
        for (LogicalExample example :
                examplesSoFar) {
            if (!this.isConsistent(example))
                return false;
        }
        return true;
    }

    public List<Hypothesis> generalisations(List<LogicalExample> examplesSoFar) {
        List<Hypothesis> result = new ArrayList<>();
        LogicalExample lastExample = examplesSoFar.get(examplesSoFar.size() - 1);
        for (List<String> possibledisjunct :
                PowerSetGenerator.powerSet(new ArrayList<>(lastExample.getAttributes().keySet()))) {
            boolean temp = true;
            for (HashMap<String, String> availableKeys :
                    this.getHypothesis()) {
                if (availableKeys.keySet().containsAll(possibledisjunct) && possibledisjunct.containsAll(availableKeys.keySet()))
                    temp = false;
            }
            if (temp) {
                HashMap<String, String> disjunct = new HashMap<>();
                for (String s :
                        possibledisjunct) {
                    disjunct.put(s, lastExample.getAttributes().get(s));
                }
                Hypothesis toAdd = new Hypothesis(this.goal, new ArrayList<>(this.getHypothesis()));
                toAdd.getHypothesis().add(disjunct);
                result.add(toAdd);
            }
        }
        Collections.shuffle(result);
        return result;
    }
}
