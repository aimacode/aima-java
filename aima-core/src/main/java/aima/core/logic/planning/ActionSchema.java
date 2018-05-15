package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;

import java.util.ArrayList;
import java.util.List;

public class ActionSchema {
    List<Term> variables;
    List<Literal> precondition; //treated as a conjunction of fluents
    List<Literal> effects;//treated as a conjunction of fluents
    List<Literal> effectsPositiveLiterals;
    List<Literal> effectsNegativeLiterals;
    private String name;

    public ActionSchema(String name, List<Term> variables,
                        List<Literal> precondition, List<Literal> effects) {
        if (variables == null)
            variables = new ArrayList<>();
        this.name = name;
        this.variables = variables;
        this.precondition = precondition;
        this.effects = effects;
        effectsNegativeLiterals = new ArrayList<>();
        effectsPositiveLiterals = new ArrayList<>();
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

    @Override
    public String toString() {
        String result = "Action("+this.getName()+")\n\tPRECOND:";
        for (Literal precond :
                getPrecondition()) {
            result = result+"^"+precond.toString();
        }
        result = result+"\n\tEFFECT:";
        for (Literal effect :
                getEffects()) {
            result = result+"^"+effect.toString();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(!(obj instanceof ActionSchema))
            return false;
        return this.getName().equals(((ActionSchema) obj).getName())&&
                this.getPrecondition().containsAll(((ActionSchema) obj).getPrecondition())
                && ((ActionSchema) obj).getPrecondition().containsAll(this.getPrecondition())
                && this.getEffects().containsAll(((ActionSchema) obj).getEffects())
                && ((ActionSchema) obj).getEffects().containsAll(this.getEffects());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        for (Literal preCo :
                this.getPrecondition()) {
            hashCode = 37 * hashCode + preCo.hashCode();
        }
        for (Literal effect :
                this.getEffects()) {
            hashCode = 37 * hashCode + effect.hashCode();
        }
        for (Term var :
                this.getVariables()) {
            hashCode = 37 * hashCode + var.hashCode();
        }
        return hashCode;
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
