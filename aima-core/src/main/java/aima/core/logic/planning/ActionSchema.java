package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 367.<br>
 * <p>
 * Actions are described by a set of action schemas that implicitly define the ACTIONS (s)
 * and RESULT (s, a) functions needed to do a problem-solving search.
 * <p>
 * The schema consists of the action name, a list of all the variables used in the schema, a
 * precondition and an effect.
 *
 * @author samagra
 */
public class ActionSchema {
    List<Term> variables;// list of variables
    List<Literal> precondition; //PRECONDITION: treated as a conjunction of fluents
    List<Literal> effects;//EFFECT: treated as a conjunction of fluents
    List<Literal> effectsPositiveLiterals;
    List<Literal> effectsNegativeLiterals;
    private String name;//action name

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
        String result = "Action(" + this.getName() + ")\n\tPRECOND:";
        for (Literal precond :
                getPrecondition()) {
            result = result + "^" + precond.toString();
        }
        result = result + "\n\tEFFECT:";
        for (Literal effect :
                getEffects()) {
            result = result + "^" + effect.toString();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ActionSchema))
            return false;
        return this.getName().equals(((ActionSchema) obj).getName()) &&
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

    /**
     * This function generates ground actions from a given actionschema.
     *
     * @param constants The list of constants from which to generate ground actions.
     * @return A ground action.
     */
    public ActionSchema getActionBySubstitution(List<Constant> constants) {
        List<Literal> precondList = this.getPrecondition();
        List<Term> vars = this.getVariables();
        List<Literal> effectList = this.getEffects();
        List<Literal> newPrecond = new ArrayList<>();
        List<Literal> newEffects = new ArrayList<>();
        for (Literal precondition :
                precondList) {
            List<Term> newTerms = new ArrayList<>();
            for (Term variable :
                    precondition.getAtomicSentence().getArgs()) {
                if (variable instanceof Variable) {
                    newTerms.add(constants.get(vars.lastIndexOf(variable)));
                } else
                    newTerms.add(variable);
            }
            newPrecond.add(new Literal(new
                    Predicate(precondition.getAtomicSentence().getSymbolicName(),
                    newTerms), precondition.isNegativeLiteral()));
        }
        for (Literal effect :
                effectList) {
            List<Term> newTerms = new ArrayList<>();
            for (Term variable :
                    effect.getAtomicSentence().getArgs()) {
                if (variable instanceof Variable) {
                    newTerms.add(constants.get(vars.lastIndexOf(variable)));
                } else
                    newTerms.add(variable);
            }
            newEffects.add(new Literal(new
                    Predicate(effect.getAtomicSentence().getSymbolicName(),
                    newTerms), effect.isNegativeLiteral()));
        }
        return new ActionSchema(this.getName(), null, newPrecond, newEffects);
    }

    /**
     * To extract constants from an action.
     *
     * @return A list of constants from the acton.
     */
    public List<Constant> getConstants() {
        List<Constant> constants = new ArrayList<>();
        for (Constant constant :
                extractConstant(getPrecondition())) {
            if (!constants.contains(constant))
                constants.add(constant);
        }
        for (Constant constant :
                extractConstant(getEffects())) {
            if (!constants.contains(constant))
                constants.add(constant);
        }
        return constants;
    }

    public List<Constant> extractConstant(List<Literal> list) {
        List<Constant> result = new ArrayList<>();
        for (Literal literal :
                list) {
            for (Term term :
                    literal.getAtomicSentence().getArgs()) {
                if (term instanceof Constant && !list.contains(term))
                    result.add((Constant) term);
            }
        }
        return result;
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
