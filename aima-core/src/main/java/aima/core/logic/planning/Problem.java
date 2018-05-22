package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.util.math.permute.PermutationGenerator;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page
 * 383.<br>
 * <br>
 * <p>
 * A set of action schemas serves as a definition of a planning domain. A specific problem
 * within the domain is defined with the addition of an initial state and a goal. The initial
 * state is a conjunction of ground atoms.The goal is just like a
 * precondition: a conjunction of literals (positive or negative) that may contain variables, such
 * as At(p, SFO ) ∧ Plane(p). Any variables are treated as existentially quantified, so this goal
 * is to have any plane at SFO. The problem is solved when we can find a sequence of actions
 * that end in a state s that entails the goal.
 *
 * @author samagra
 */
public class Problem {
    State initialState;// initialState
    Set<ActionSchema> actionSchemas;// Planning Domain
    State goalState;// goalState


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

    /**
     * @return Constants for a particular problem domain.
     */
    public List<Constant> getProblemConstants() {
        List<Constant> constants = new ArrayList<>();
        for (Literal literal :
                getInitialState().getFluents()) {
            for (Term term :
                    literal.getAtomicSentence().getArgs()) {
                if (term instanceof Constant) {
                    if (!constants.contains((Constant) term))
                        constants.add((Constant) term);
                }
            }
        }
        for (Literal literal :
                getGoalState().getFluents()) {
            for (Term term :
                    literal.getAtomicSentence().getArgs()) {
                if (term instanceof Constant) {
                    if (!constants.contains((Constant) term))
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

    /**
     * @return Propositionalises all the actionschemas to return a set of possible ground actions
     */
    public List<ActionSchema> getPropositionalisedActions() {
        List<Constant> problemConstants = getProblemConstants();
        List<ActionSchema> result = new ArrayList<>();
        for (ActionSchema actionSchema :
                getActionSchemas()) {
            int numberOfVars = actionSchema.getVariables().size();
            for (List<Constant> constants :
                    PermutationGenerator.generatePermutations(problemConstants, numberOfVars)) {
                result.add(actionSchema.getActionBySubstitution(constants));
            }
        }
        return result;
    }
}
