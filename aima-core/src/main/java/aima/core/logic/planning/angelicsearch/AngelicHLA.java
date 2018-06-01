package aima.core.logic.planning.angelicsearch;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class AngelicHLA {
    List<Term> variables;// list of variables
    List<Literal> precondition; //PRECONDITION: treated as a conjunction of fluents
    List<HashSet<Literal>> effects;//EFFECT: treated as a conjunction of fluents
    HashSet<Literal> effectsPositiveLiterals;
    HashSet<Literal> effectsNegativeLiterals;
    HashSet<Literal> effectsMaybePositiveLiterals;
    HashSet<Literal> effectsMaybeNegativeLiterals;
    private String name;//action name
    List<List<Object>> refinements = new ArrayList<>();

    public void setRefinements(List<List<Object>> refinements) {
        this.refinements = refinements;
    }

    public void addRefinement(List<Object> newRefinement){
        this.refinements.add(newRefinement);
    }

    public List<List<Object>> getRefinements() {
        return refinements;
    }

    public AngelicHLA(String name, List<Term> variables,
                      List<Literal> precondition, List<HashSet<Literal>> effects) {
        if (variables == null)
            variables = new ArrayList<>();
        this.name = name;
        this.variables = variables;
        this.precondition = precondition;
        this.effects = effects;
        effectsNegativeLiterals = new HashSet<>();
        effectsPositiveLiterals = new HashSet<>();
        effectsMaybePositiveLiterals = new HashSet<>();
        effectsMaybeNegativeLiterals = new HashSet<>();
        this.sortEffects();
    }

    public AngelicHLA(String name, List<Term> variables, String precondition, String effects) {
        this(name, variables, Utils.parse(precondition), Utils.angelicParse(effects));
    }

    private void sortEffects() {
        effectsPositiveLiterals.addAll(this.effects.get(0));
        effectsNegativeLiterals.addAll(this.effects.get(1));
        effectsMaybePositiveLiterals.addAll(this.effects.get(2));
        effectsMaybeNegativeLiterals.addAll(this.effects.get(3));
    }

    public List<Term> getVariables() {
        return variables;
    }

    public List<Literal> getPrecondition() {
        return precondition;
    }

    public List<HashSet<Literal>> getEffects() {
        return effects;
    }

    public String getName() {
        return name;
    }

    public HashSet<Literal> getEffectsPositiveLiterals() {
        return effectsPositiveLiterals;
    }

    public HashSet<Literal> getEffectsNegativeLiterals() {
        return effectsNegativeLiterals;
    }

    public HashSet<Literal> getEffectsMaybePositiveLiterals() {
        return effectsMaybePositiveLiterals;
    }

    public HashSet<Literal> getEffectsMaybeNegativeLiterals() {
        return effectsMaybeNegativeLiterals;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Action(" + this.getName() + ")\n\tPRECOND:");
        for (Literal precond :
                getPrecondition()) {
            result.append("^").append(precond.toString());
        }
        result.append("\n\tEFFECT:");
        for (Literal effect :
                getEffectsPositiveLiterals()) {
            result.append("^").append(effect.toString());
        }

        for (Literal effect :
                getEffectsNegativeLiterals()) {
            result.append("^~").append(effect.toString());
        }

        for (Literal effect :
                getEffectsMaybePositiveLiterals()) {
            if (getEffectsMaybeNegativeLiterals().contains(effect))
                result.append("^~+-").append(effect.toString());
            else
                result.append("^~+").append(effect.toString());
        }

        for (Literal effect :
                getEffectsMaybeNegativeLiterals()) {
            result.append("^~-").append(effect.toString());
        }


        return result.toString();
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        for (Literal preCo :
                this.getPrecondition()) {
            hashCode = 37 * hashCode + preCo.hashCode();
        }
        for (Literal effect :
                this.getEffectsPositiveLiterals()) {
            hashCode = 37 * hashCode + effect.hashCode();
        }

        for (Literal effect :
                this.getEffectsNegativeLiterals()) {
            hashCode = 37 * hashCode + effect.hashCode();
        }

        for (Literal effect :
                this.getEffectsMaybePositiveLiterals()) {
            hashCode = 37 * hashCode + effect.hashCode();
        }

        for (Literal effect :
                this.getEffectsMaybeNegativeLiterals()) {
            hashCode = 37 * hashCode + effect.hashCode();
        }

        for (Term var :
                this.getVariables()) {
            hashCode = 37 * hashCode + var.hashCode();
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AngelicHLA))
            return false;
        return this.getName().equals(((AngelicHLA) obj).getName()) &&
                this.getPrecondition().containsAll(((AngelicHLA) obj).getPrecondition())
                && ((AngelicHLA) obj).getPrecondition().containsAll(this.getPrecondition())
                && this.getEffectsPositiveLiterals().containsAll(((AngelicHLA) obj).getEffectsPositiveLiterals())
                && ((AngelicHLA) obj).getEffectsPositiveLiterals().containsAll(this.getEffectsPositiveLiterals())
                && this.getEffectsNegativeLiterals().containsAll(((AngelicHLA) obj).getEffectsNegativeLiterals())
                && ((AngelicHLA) obj).getEffectsNegativeLiterals().containsAll(this.getEffectsNegativeLiterals())
                && this.getEffectsMaybePositiveLiterals().containsAll(((AngelicHLA) obj).getEffectsMaybePositiveLiterals())
                && ((AngelicHLA) obj).getEffectsMaybePositiveLiterals().containsAll(this.getEffectsMaybePositiveLiterals())
                && this.getEffectsMaybeNegativeLiterals().containsAll(((AngelicHLA) obj).getEffectsMaybeNegativeLiterals())
                && ((AngelicHLA) obj).getEffectsMaybeNegativeLiterals().containsAll(this.getEffectsNegativeLiterals());
    }


}
