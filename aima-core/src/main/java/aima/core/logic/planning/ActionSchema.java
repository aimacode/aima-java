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
 * @author Ruediger Lunde
 */
public class ActionSchema {

    /**
     * Name for persistence actions.
     */
    public final static String NO_OP = "No-op";

    private final String name; // action name
    private final List<Term> variables; // list of variables
    private final List<Literal> precondition; // PRECONDITION: treated as a conjunction of fluents
    private final List<Literal> effect; // EFFECT: treated as a conjunction of fluents

    private final List<Literal> effectsPositiveLiterals;
    private final List<Literal> effectsNegativeLiterals;


    public ActionSchema(String name, List<Term> variables, List<Literal> precondition, List<Literal> effect) {
        if (variables == null)
            variables = new ArrayList<>();
        this.name = name;
        this.variables = variables;
        this.precondition = precondition;
        this.effect = effect;
        effectsNegativeLiterals = new ArrayList<>();
        effectsPositiveLiterals = new ArrayList<>();
        this.sortEffects();
    }

    public ActionSchema(String name, List<Term> variables, String precondition, String effect) {
        this(name, variables, Utils.parse(precondition), Utils.parse(effect));
    }

    private void sortEffects() {
        for (Literal eff : effect) {
            if (eff.isNegativeLiteral())
                effectsNegativeLiterals.add(eff);
            else
                effectsPositiveLiterals.add(eff);
        }
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        StringBuilder result = new StringBuilder(name);
        result.append(("("));
        String sep = "";
        for (Term var : variables) {
            result.append(sep).append(var);
            sep = ", ";
        }
        result.append(")");
        return result.toString() ;
    }

    public List<Term> getVariables() {
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Action ").append(this.getFullName()).append("\n\tPRECOND: ");
        String and = "";
        for (Literal pre : precondition) {
            result.append(and).append(pre);
            and = "^";
        }
        result.append("\n\tEFFECT: ");
        and = "";
        for (Literal eff : effect) {
            result.append(and).append(eff);
            and = "^";
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        return this.getName().equals(((ActionSchema) obj).getName()) &&
                this.getPrecondition().containsAll(((ActionSchema) obj).getPrecondition())
                && ((ActionSchema) obj).getPrecondition().containsAll(this.getPrecondition())
                && this.getEffect().containsAll(((ActionSchema) obj).getEffect())
                && ((ActionSchema) obj).getEffect().containsAll(this.getEffect());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        for (Literal literal : precondition)
            hashCode = 37 * hashCode + literal.hashCode();
        for (Literal literal : effect)
            hashCode = 37 * hashCode + literal.hashCode();
        for (Term var : this.getVariables())
            hashCode = 37 * hashCode + var.hashCode();
        return hashCode;
    }

    /**
     * This function generates ground actions from a given actionschema.
     *
     * @param constants The list of constants from which to generate ground actions.
     * @return A ground action.
     */
    public ActionSchema getActionBySubstitution(List<Constant> constants) {
        List<Literal> newPrecondition = new ArrayList<>();
        List<Literal> newEffect = new ArrayList<>();
        for (Literal pre : precondition) {
            List<Term> newTerms = new ArrayList<>();
            for (Term variable : pre.getAtomicSentence().getArgs()) {
                if (variable instanceof Variable) {
                    newTerms.add(constants.get(variables.lastIndexOf(variable)));
                } else
                    newTerms.add(variable);
            }
            newPrecondition.add(new Literal(new Predicate(pre.getAtomicSentence().getSymbolicName(), newTerms),
                    pre.isNegativeLiteral()));
        }
        for (Literal eff : effect) {
            List<Term> newTerms = new ArrayList<>();
            for (Term variable : eff.getAtomicSentence().getArgs()) {
                if (variable instanceof Variable)
                    newTerms.add(constants.get(variables.lastIndexOf(variable)));
                else
                    newTerms.add(variable);
            }
            newEffect.add(new Literal(new
                    Predicate(eff.getAtomicSentence().getSymbolicName(), newTerms), eff.isNegativeLiteral()));
        }
        return new ActionSchema(this.getName(), new ArrayList<>(constants), newPrecondition, newEffect);
        //return new ActionSchema(this.getName(),null, newPrecondition, newEffect);
    }

    /**
     * To extract constants from an action.
     *
     * @return A list of constants from the acton.
     */
    public List<Constant> getConstants() {
        List<Constant> constants = new ArrayList<>();
        for (Constant constant : extractConstant(precondition)) {
            if (!constants.contains(constant))
                constants.add(constant);
        }
        for (Constant constant : extractConstant(effect)) {
            if (!constants.contains(constant))
                constants.add(constant);
        }
        return constants;
    }

    public List<Constant> extractConstant(List<Literal> list) {
        List<Constant> result = new ArrayList<>();
        for (Literal literal : list) {
            for (Term term : literal.getAtomicSentence().getArgs()) {
                if (term instanceof Constant && !list.contains(term))
                    result.add((Constant) term);
            }
        }
        return result;
    }
}
