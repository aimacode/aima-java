package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 379.<br>
 * <p>
 * A planning graph is a directed graph organized into levels: first a level S 0 for the initial
 * state, consisting of nodes representing each fluent that holds in S 0 ; then a level A 0 consisting
 * of nodes for each ground action that might be applicable in S 0 ; then alternating levels S i
 * followed by A i ; until we reach a termination condition.
 *
 * @author samagra
 * @author Ruediger Lunde
 */
public class Graph {
    private final List<Level<Literal, ActionSchema>> literalLevels; // size: numLevels()
    private final List<Level<ActionSchema, Literal>> actionLevels; // size: numLevels()-1

    public Graph(PlanningProblem problem) {
        literalLevels = new ArrayList<>();
        actionLevels = new ArrayList<>();
        literalLevels.add(createLiteralLevel(null, problem));
    }

    public Level<Literal, ActionSchema> getLiteralLevel(int i) {
        return literalLevels.get(i);
    }

    public Level<ActionSchema, Literal> getActionLevel(int i) {
        return actionLevels.get(i);
    }

    public int numLevels() {
        return literalLevels.size();
    }

    /**
     * This method adds a level (an action and a state level) for a new state
     * to the planning graph.
     */
    public void expand(PlanningProblem problem) {
        Level<Literal, ActionSchema> litLevelCurr = literalLevels.get(numLevels() - 1);
        Level<ActionSchema, Literal> actLevelNext = createActionLevel(litLevelCurr, problem);
        Level<Literal, ActionSchema> litLevelNext = createLiteralLevel(actLevelNext, problem);
        actionLevels.add(actLevelNext);
        literalLevels.add(litLevelNext);
    }

    /**
     * A graph is said to be levelled off if two consecutive levels are identical.
     *
     * @return Boolean stating if the graph is levelled off.
     */
    public boolean levelledOff() {
        if (numLevels() < 2)
            return false;
        return getLiteralLevel(numLevels() - 1).equals(getLiteralLevel(numLevels() - 2));
    }

    public Set<ActionSchema> getAllActions() {
        Set<ActionSchema> result = new LinkedHashSet<>();
        for (Level<ActionSchema, Literal> level : actionLevels)
            result.addAll(level.getLevelObjects());
        return result;
    }

    public int getFirstLevelOfOccurrence(Literal literal) {
        int result = 0;
        for (Level<Literal, ActionSchema> level : literalLevels) {
            if (level.getLevelObjects().contains(literal))
                return result;
            result++;
        }
        return Integer.MAX_VALUE;
    }

    private Level<ActionSchema, Literal> createActionLevel(Level<Literal, ActionSchema> prevLevel,
                                                           PlanningProblem problem) {
        Level<ActionSchema, Literal> result =  new Level<>(prevLevel, problem);
        // add actions with empty precondition
        for (ActionSchema action : problem.getPropositionalisedActions()) {
            if (action.getPrecondition().size()==0)
                result.getLevelObjects().add(action);
        }
        // set next links
        for (ActionSchema action : result.getLevelObjects())
            result.putToNextLinks(action, new ArrayList<>(action.getEffect()));
        calculateMutexLinksForActionLevel(result);
        return result;
    }

    // prevLevel can be null
    private Level<Literal, ActionSchema> createLiteralLevel(Level<ActionSchema, Literal> prevLevel,
                                                            PlanningProblem problem) {
        Level<Literal, ActionSchema> result = (prevLevel == null)
                ? new Level<>(getInitialPlanningState(problem), problem)
                : new Level<>(prevLevel, problem);
        calculateNextLinks(result, problem);
        calculateMutexLinksForLiteralLevel(result);
        return result;
    }

    // negative precondition literals are needed for backward search...
    private List<Literal> getInitialPlanningState(PlanningProblem problem) {
        List<Literal> result = new ArrayList<>(problem.getInitialState().getFluents());
        for (ActionSchema action : problem.getPropositionalisedActions()) {
            for (Literal literal : action.getPrecondition())
                if (literal.isNegativeLiteral()) {
                    Literal posLiteral = literal.getComplementaryLiteral();
                    if (!result.contains(posLiteral))
                        result.add(literal);
                }
        }
        return result;
    }

    private void calculateNextLinks(Level<Literal, ActionSchema> level, PlanningProblem problem) {
        // add applicable actions:
        for (ActionSchema action : problem.getPropositionalisedActions()) {
            if (level.getLevelObjects().containsAll(action.getPrecondition()))
                for (Literal literal : action.getPrecondition())
                    level.addToNextLinks(literal, action);
        }
        // add persistence actions:
        for (Literal literal : level.getLevelObjects()) {
            ActionSchema action = new ActionSchema(ActionSchema.NO_OP, null,
                    Collections.singletonList(literal),
                    Collections.singletonList(literal));
            level.addToNextLinks(literal, action);
        }
    }

    private void calculateMutexLinksForActionLevel(Level<ActionSchema, Literal> level) {
        List<ActionSchema> actions = level.getLevelObjects();
        ActionSchema firstAction, secondAction;
        boolean checkMutex;

        for (int i = 0; i < actions.size(); i++) {
            firstAction = actions.get(i);
            List<Literal> firstActionEffects = firstAction.getEffect();
            List<Literal> firstActionPositiveEffects = firstAction.getEffectsPositiveLiterals();
            List<Literal> firstActionPreconditions = firstAction.getPrecondition();
            for (int j = i + 1; j < actions.size(); j++) {
                checkMutex = false;
                secondAction = actions.get(j);
                List<Literal> secondActionEffects = secondAction.getEffect();
                List<Literal> secondActionNegatedLiterals = secondAction.getEffectsNegativeLiterals();
                List<Literal> secondActionPreconditions = secondAction.getPrecondition();
                for (Literal posLiteral : firstActionPositiveEffects) {
                    for (Literal negatedLit : secondActionNegatedLiterals)
                        if (posLiteral.equals(new Literal(negatedLit.getAtomicSentence(), false)))
                            checkMutex = true;
                }
                if (!checkMutex) {
                    if (checkInterference(secondActionPreconditions, firstActionEffects))
                        checkMutex = true;
                    if (checkInterference(firstActionPreconditions, secondActionEffects))
                        checkMutex = true;
                }
                if (!checkMutex) {
                    HashMap<Literal, List<Literal>> prevMutex = level.getPrevLevel().getMutexLinks();
                    if (prevMutex != null) {
                        for (Literal firstActionPrecondition : firstActionPreconditions)
                            for (Literal secondActionPrecondition : secondActionPreconditions)
                                if (prevMutex.get(firstActionPrecondition) != null
                                        && prevMutex.get(firstActionPrecondition).contains
                                        (secondActionPrecondition))
                                    checkMutex = true;
                    }
                }
                if (checkMutex) {
                    level.addToMutexLinks(firstAction, secondAction);
                    level.addToMutexLinks(secondAction, firstAction);
                }
            }
        }
    }

    private boolean checkInterference(List<Literal> firstActionPreconditions, List<Literal> secondActionEffects) {
        boolean checkMutex = false;
        for (Literal secondActionEffect : secondActionEffects) {
            for (Literal firstActionPrecondition : firstActionPreconditions) {
                if (secondActionEffect.equals(new Literal(firstActionPrecondition.getAtomicSentence(),
                        firstActionPrecondition.isPositiveLiteral()))) {
                    checkMutex = true;
                }
            }
        }
        return checkMutex;
    }

    private void calculateMutexLinksForLiteralLevel(Level<Literal, ActionSchema> level) {
        Level<ActionSchema, Literal> prevLevel = level.getPrevLevel();
        if (prevLevel == null)
            return;
        List<Literal> literals = level.getLevelObjects();
        Literal firstLiteral, secondLiteral;
        List<ActionSchema> possibleActionsFirst, possibleActionsSecond;
        for (int i = 0; i < literals.size(); i++) {
            firstLiteral = literals.get(i);
            possibleActionsFirst = level.getLinkedPrevObjects(firstLiteral);
            for (int j = i; j < literals.size(); j++) {
                secondLiteral = literals.get(j);
                possibleActionsSecond = level.getLinkedPrevObjects(secondLiteral);
                if (firstLiteral.getAtomicSentence().getSymbolicName().equals
                        (secondLiteral.getAtomicSentence().getSymbolicName()) &&
                        ((firstLiteral.isNegativeLiteral() && secondLiteral.isPositiveLiteral()) ||
                                firstLiteral.isPositiveLiteral() && secondLiteral.isNegativeLiteral())) {
                    level.addToMutexLinks(firstLiteral, secondLiteral);
                    level.addToMutexLinks(secondLiteral, firstLiteral);
                } else {
                    boolean eachPossiblePairExclusive = true;
                    HashMap<ActionSchema, List<ActionSchema>> prevMutexes = prevLevel.getMutexLinks();
                    for (ActionSchema firstAction : possibleActionsFirst) {
                        for (ActionSchema secondAction : possibleActionsSecond) {
                            if ((!prevMutexes.containsKey(firstAction))
                                    || (!prevMutexes.get(firstAction).contains(secondAction))) {
                                eachPossiblePairExclusive = false;
                            }
                        }
                    }
                    if (eachPossiblePairExclusive) {
                        level.addToMutexLinks(firstLiteral, secondLiteral);
                        level.addToMutexLinks(secondLiteral, firstLiteral);
                    }
                }
            }
        }
    }
}
