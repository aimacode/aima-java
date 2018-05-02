package aima.core.logic.planning;

import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Variable;

import java.util.List;

public class ActionSchema {
    private String name;
    List<Variable> variables;
    List<AtomicSentence> precondition; //treated as a conjucntion of fluents
    List<AtomicSentence> effect;//treated as a conjunction of fluents
    List<AtomicSentence> effectsPositiveLiterals;
    List<AtomicSentence> effectsNegativeLiterals;

    public ActionSchema(String name, List<Variable> variables,
                        List<AtomicSentence> precondition, List<AtomicSentence> effect) {
        this.name = name;
        this.variables = variables;
        this.precondition = precondition;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public List<Variable> getVariables() {
        return variables;
    }

    public List<AtomicSentence> getPrecondition() {
        return precondition;
    }

    public List<AtomicSentence> getEffect() {
        return effect;
    }

    public List<AtomicSentence> getEffectsPositiveLiterals() {
        return effectsPositiveLiterals;
    }

    public List<AtomicSentence> getEffectsNegativeLiterals() {
        return effectsNegativeLiterals;
    }
}
