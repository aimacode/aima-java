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
 * as At(p, SFO) ∧ Plane(p). Any variables are treated as existentially quantified, so this goal
 * is to have any plane at SFO. The problem is solved when we can find a sequence of actions
 * that end in a state s that entails the goal.
 *
 * @author samagra
 * @author Ruediger Lunde
 */
public class PlanningProblem {
    private final State initialState;
    private final Set<ActionSchema> actionSchemas;
    private final List<Literal> goal;

    private List<ActionSchema> propositionalisedActionSchemas;


    public PlanningProblem(State initialState, List<Literal> goal, Set<ActionSchema> actionSchemas) {
        this.initialState = initialState;
        this.actionSchemas = actionSchemas;
        this.goal = goal;
    }

    public PlanningProblem(State initialState, List<Literal> goal, ActionSchema... actions) {
        this(initialState, goal, new LinkedHashSet<>(Arrays.asList(actions)));
    }

    public State getInitialState() {
        return initialState;
    }

    public Set<ActionSchema> getActionSchemas() {
        return actionSchemas;
    }

    public List<Literal> getGoal() {
        return goal;
    }

    /**
     * @return Constants for a particular problem domain.
     */
    public List<Constant> getProblemConstants() {
        List<Constant> constants = new ArrayList<>();
        for (Literal literal : getInitialState().getFluents()) {
            for (Term term : literal.getAtomicSentence().getArgs()) {
                if (term instanceof Constant) {
                    if (!constants.contains((Constant) term))
                        constants.add((Constant) term);
                }
            }
        }
        for (Literal literal : getGoal()) {
            for (Term term : literal.getAtomicSentence().getArgs()) {
                if (term instanceof Constant) {
                    if (!constants.contains((Constant) term))
                        constants.add((Constant) term);
                }
            }
        }
        for (ActionSchema actionSchema : getActionSchemas()) {
            for (Constant constant : actionSchema.getConstants()) {
                if (!constants.contains(constant))
                    constants.add(constant);
            }
        }
        return constants;
    }

    /**
     * Note: Here, all variables are replaced by different constants. It is assumed that preconditions contain
     * x != y literals for every pair of variables.
     * @return Propositionalises all the actionschemas to return a set of possible ground actions
     */
    public List<ActionSchema> getPropositionalisedActions() {
        if (propositionalisedActionSchemas == null) {
            List<Constant> problemConstants = getProblemConstants();
            propositionalisedActionSchemas = new ArrayList<>();
            for (ActionSchema actionSchema : getActionSchemas()) {
                int numberOfVars = actionSchema.getVariables().size();
                for (List<Constant> constants : PermutationGenerator.generatePermutations(problemConstants, numberOfVars))
                    propositionalisedActionSchemas.add(actionSchema.getActionBySubstitution(constants));
            }
        }
        return propositionalisedActionSchemas;
    }
}
