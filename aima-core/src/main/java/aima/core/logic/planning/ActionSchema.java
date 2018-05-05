package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Variable;

import java.util.List;

public class ActionSchema {
    List<Variable> variables;
    List<Literal> precondition; //treated as a conjucntion of fluents
    List<Literal> effect;//treated as a conjunction of fluents
    List<Literal> effectsPositiveLiterals;
    List<Literal> effectsNegativeLiterals;
    private String name;

    public ActionSchema(String name, List<Variable> variables,
                        List<Literal> precondition, List<Literal> effect) {
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

    public List<Literal> getPrecondition() {
        return precondition;
    }

    public List<Literal> getEffect() {
        return effect;
    }

    public List<Literal> getEffectsPositiveLiterals() {
        return effectsPositiveLiterals;
    }

    public List<Literal> getEffectsNegativeLiterals() {
        return effectsNegativeLiterals;
    }
}
