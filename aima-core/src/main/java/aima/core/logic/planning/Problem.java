package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Term;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.*;

public class Problem {
    State initialState;
    Set<ActionSchema> actionSchemas;
    State goalState;


    public Problem(State initialState, State goalState, Set<ActionSchema> actionSchemas) {
        this.initialState = initialState;
        this.actionSchemas = actionSchemas;
        this.goalState = goalState;
    }

    public Problem(State initialState, State goalState, ActionSchema... actions) {
        this(initialState, goalState, new HashSet<>(Arrays.asList(actions)));
    }

    public State getInitialState() {
        return initialState;
    }

    public Set<ActionSchema> getActionSchemas() {
        return actionSchemas;
    }

    public State getGoalState() {
        return goalState;
    }
    public List<Constant> getProblemConstants(){
        List<Constant> constants = new ArrayList<>();
        for (Literal literal :
                getInitialState().getFluents()) {
            for (Term term :
                    literal.getAtomicSentence().getArgs()) {
                if (term instanceof Constant){
                    if (!constants.contains((Constant)term))
                    constants.add((Constant) term);
                }
            }
        }
        for (Literal literal :
                getGoalState().getFluents()) {
            for (Term term :
                    literal.getAtomicSentence().getArgs()) {
                if (term instanceof Constant){
                    if (!constants.contains((Constant)term))
                    constants.add((Constant) term);
                }
            }
        }
        for (ActionSchema actionSchema :
             getActionSchemas()) {
            for (Constant constant :
                    actionSchema.getConstants()) {
                if (!constants.contains(constant))
                    constants.add(constant);
            }
        }
        return constants;
    }
    public List<ActionSchema> getPropositionalisedActions(){
      List<Constant> problemConstants = getProblemConstants();
      List<ActionSchema> propositionalisedActions = new ArrayList<>();
        for (ActionSchema actionSchema :
                getActionSchemas()) {
            int numberOfVars = actionSchema.getVariables().size();

        }
        return null;
     }
}
