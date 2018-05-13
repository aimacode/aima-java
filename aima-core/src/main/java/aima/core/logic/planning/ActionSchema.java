package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;

import java.util.List;

public class ActionSchema {
    List<Term> variables;
    List<Literal> precondition; //treated as a conjucntion of fluents
    List<Literal> effects;//treated as a conjunction of fluents
    List<Literal> effectsPositiveLiterals;
    List<Literal> effectsNegativeLiterals;
    private String name;

    public ActionSchema(String name, List<Term> variables,
                        List<Literal> precondition, List<Literal> effects) {
        this.name = name;
        this.variables = variables;
        this.precondition = precondition;
        this.effects = effects;
        this.sortEffects();
    }

    public ActionSchema(String name, List<Term> variables, String precondition, String effects) {
        this(name, variables, Utils.parse(precondition), Utils.parse(effects));
    }

    private void sortEffects() {
        for (Literal effect :
                this.effects) {
            if (effect.isNegativeLiteral()) {
                effectsNegativeLiterals.add(effect);
            } else {
                effectsPositiveLiterals.add(effect);
            }

        }
    }

    public String getName() {
        return name;
    }

    public List<Term> getVariables() {
        return variables;
    }

    public List<Literal> getPrecondition() {
        return precondition;
    }

    public List<Literal> getEffects() {
        return effects;
    }

    public List<Literal> getEffectsPositiveLiterals() {
        return effectsPositiveLiterals;
    }

    public List<Literal> getEffectsNegativeLiterals() {
        return effectsNegativeLiterals;
    }
}
