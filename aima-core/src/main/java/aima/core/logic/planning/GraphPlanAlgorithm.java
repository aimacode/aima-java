package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 10.9 page
 * 383.<br>
 * <br>
 *
 * <pre>
 *
 * function GRAPHPLAN(problem) returns solution or failure
 *
 *  graph ← INITIAL-PLANNING-GRAPH(problem)
 *  goals ← CONJUNCTS(problem.GOAL)
 *  nogoods ← an empty hash table
 *  for tl = 0 to ∞ do
 *    if goals all non-mutex in St of graph then
 *      solution ← EXTRACT-SOLUTION(graph, goals, NUMLEVELS(graph), nogoods)
 *      if solution ≠ failure then return solution
 *    if graph and nogoods have both leveled off then return failure
 *    graph ← EXPAND-GRAPH(graph, problem)
 * </pre>
 * <p>
 * Figure 10.9 The GRAPHPLAN algorithm. GRAPHPLAN calls EXPAND-GRAPH to add a
 * level until either a solution is found by EXTRACT-SOLUTION, or no solution
 * is possible.
 *
 * @author samagra
 */
public class GraphPlanAlgorithm {

    /**
     * function GRAPHPLAN(problem) returns solution or failure
     *
     * @param problem the planning problem for which the plan is to be created
     * @return a solution or null
     */
    public List<List<ActionSchema>> graphPlan(Problem problem) {
        //graph ← INITIAL-PLANNING-GRAPH(problem)
        Graph graph = initialPlanningGraph(problem);
        // goals ← CONJUNCTS(problem.GOAL)
        List<Literal> goals = conjuncts(problem.getGoalState());
        // nogoods ← an empty hash table
        Hashtable<Integer, List<Literal>> nogoods = new Hashtable<>();
        Level state;
        // for tl = 0 to ∞ do
        for (int tl = 0; ; tl++) {
            //St
            state = graph.getLevels().get(2 * tl);
            // if goals all non-mutex in St of graph then
            if (checkAllGoalsNonMutex(state, goals)) {
                // solution ← EXTRACT-SOLUTION(graph, goals, NUMLEVELS(graph), nogoods)
                List<List<ActionSchema>> solution = extractSolution(graph, goals, graph.numLevels(), nogoods);
                //if solution ≠ failure then return solution
                if (solution != null && solution.size() != 0)
                    return solution;
            }
            // if graph and nogoods have both leveled off then return failure
            if (levelledOff(graph) && leveledOff(nogoods)) {
                return null;
            }
            //   graph ← EXPAND-GRAPH(graph, problem)
            graph = expandGraph(graph);
        }
    }

    /**
     * This method extracts a solution from the planning graph.
     * <p>
     * Artificial Intelligence A Modern Approach (3rd Edition): page 384.<br>
     * <p>
     * We can define EXTRACT -SOLUTION as a backward search problem, where
     * each state in the search contains a pointer to a level in the planning graph and a set of unsat-
     * isfied goals. We define this search problem as follows:
     * • The initial state is the last level of the planning graph, S n , along with the set of goals
     * from the planning problem.
     * • The actions available in a state at level S i are to select any conflict-free subset of the
     * actions in A i−1 whose effects cover the goals in the state. The resulting state has level
     * S i−1 and has as its set of goals the preconditions for the selected set of actions. By
     * “conflict free,” we mean a set of actions such that no two of them are mutex and no two
     * of their preconditions are mutex.
     * • The goal is to reach a state at level S 0 such that all the goals are satisfied.
     * • The cost of each action is 1.
     *
     * @param graph    The planning graph.
     * @param goals    Goals of the planning problem.
     * @param numLevel Number of levels in the graph.
     * @param nogoods  A hash table to store previously calculated results.
     * @return a solution if found else null
     */
    private List<List<ActionSchema>> extractSolution(Graph graph, List<Literal> goals, int numLevel,
                                                     Hashtable<Integer, List<Literal>> nogoods) {
        if (nogoods.contains(numLevel))
            return null;

        int level = (graph.numLevels() - 1) / 2;
        List<Literal> goalsCurr = goals;
        List<List<ActionSchema>> solution = new ArrayList<>();
        Level currLevel = graph.getLevels().get(2 * level);
        while (level > 0) {
            List<List<ActionSchema>> setOfPossibleActions = new ArrayList<>();
            HashMap<Object, List<Object>> mutexLinks = currLevel.getPrevLevel().getMutexLinks();
            for (Literal literal :
                    goalsCurr) {
                List<ActionSchema> possiBleActionsPerLiteral = new ArrayList<>();
                for (Object action :
                        currLevel.getPrevLinks().get(literal)) {
                    possiBleActionsPerLiteral.add((ActionSchema) action);
                }
                setOfPossibleActions.add(possiBleActionsPerLiteral);
            }
            List<List<ActionSchema>> allPossibleSubSets = generateCombinations(setOfPossibleActions);
            boolean validSet;
            List<ActionSchema> setToBeTaken = null;
            for (List<ActionSchema> possibleSet :
                    allPossibleSubSets) {
                validSet = true;
                ActionSchema firstAction, secondAction;
                for (int i = 0; i < possibleSet.size(); i++) {
                    firstAction = possibleSet.get(i);
                    for (int j = i + 1; j < possibleSet.size(); j++) {
                        secondAction = possibleSet.get(j);
                        if (mutexLinks.containsKey(firstAction) && mutexLinks.get(firstAction).contains(secondAction))
                            validSet = false;
                    }
                }
                if (validSet) {
                    setToBeTaken = possibleSet;
                    break;
                }
            }
            if (setToBeTaken == null) {
                nogoods.put(level, goalsCurr);
                return null;
            }

            level--;
            currLevel = graph.getLevels().get(2 * level);
            goalsCurr.clear();
            solution.add(setToBeTaken);
            for (ActionSchema action :
                    setToBeTaken) {
                for (Literal literal :
                        action.getPrecondition()) {
                    if (!goalsCurr.contains(literal)) {
                        goalsCurr.add(literal);
                    }
                }
            }
        }
        return solution;
    }

    /**
     * This method is used to check if all goals are present in a particular state
     * and none of them has a mutex link.
     *
     * @param level The current level in which to check for the goals.
     * @param goals List of goals to be checked
     * @return Boolean representing if goals all non mutex in St
     */
    private boolean checkAllGoalsNonMutex(Level level, List<Literal> goals) {
        if (!level.getLevelObjects().containsAll(goals)) {
            return false;
        }
        boolean mutexCheck = false;
        for (Object literal :
                goals) {
            List<Object> mutexOfGoal = level.getMutexLinks().get(literal);
            if (mutexOfGoal != null) {
                for (Object object :
                        mutexOfGoal) {
                    if (goals.contains((Literal) object)) {
                        mutexCheck = true;
                        break;
                    }
                }
            }
        }
        return (!mutexCheck);
    }

    /**
     * This method adds a new state (a state level and an action level both) to the planning graph.
     *
     * @param graph The planning graph.
     * @return The expanded graph.
     */
    private Graph expandGraph(Graph graph) {
        return graph.addLevel().addLevel();
    }

    /**
     * A graph is said to be levelled off if two consecutive levels are identical.
     *
     * @param nogoods
     * @return Boolean stating if the hashtable is levelled off.
     */
    private boolean leveledOff(Hashtable<Integer, List<Literal>> nogoods) {
        if (nogoods.size() < 2)
            return false;
        return nogoods.get(nogoods.size() - 1).equals(nogoods.get(nogoods.size() - 2));
    }

    /**
     * A graph is said to be levelled off if two consecutive levels are identical.
     *
     * @param graph
     * @return Boolean stating if the graph is levelled off.
     */
    private boolean levelledOff(Graph graph) {
        if (graph.numLevels() < 2)
            return false;
        return graph.levels.get(graph.numLevels() - 1).equals(graph.levels.get(graph.numLevels() - 2));
    }

    /**
     * Returns a list of literals in a state.
     *
     * @param goalState
     * @return List of literals.
     */
    private List<Literal> conjuncts(State goalState) {
        return goalState.getFluents();
    }

    /**
     * This method initialises the planning graph for a particular problem.
     *
     * @param problem The planning problem.
     * @return Graph for the planning problem.
     */
    private Graph initialPlanningGraph(Problem problem) {
        Level initialLevel = new Level(null, problem);
        return new Graph(problem, initialLevel);
    }

    // Helper methods for combinations and permutations.
    public List<List<ActionSchema>> combineTwoLists(List<ActionSchema> firstList, List<ActionSchema> secondList) {
        List<List<ActionSchema>> result = new ArrayList<>();
        for (ActionSchema firstAction :
                firstList) {
            for (ActionSchema secondAction :
                    secondList) {
                result.add(Arrays.asList(firstAction, secondAction));
            }
        }
        return result;
    }

    public List<List<ActionSchema>> combineExtraList(List<List<ActionSchema>> combinedList, List<ActionSchema> newList) {
        List<List<ActionSchema>> result = new ArrayList<>();
        for (List<ActionSchema> combined :
                combinedList) {
            for (ActionSchema action :
                    newList) {
                List<ActionSchema> tempList = new ArrayList<>(combined);
                tempList.add(action);
                result.add(tempList);
            }
        }
        return result;
    }

    public List<List<ActionSchema>> generateCombinations(List<List<ActionSchema>> actionLists) {
        List<List<ActionSchema>> result = new ArrayList<>();
        if (actionLists.size() == 1) {
            result.add(actionLists.get(0));
            return result;
        }
        if (actionLists.size() == 2) {
            return combineTwoLists(actionLists.get(0), actionLists.get(1));
        } else {
            result = combineTwoLists(actionLists.get(0), actionLists.get(1));
            for (int i = 2; i < actionLists.size(); i++) {
                result = combineExtraList(result, actionLists.get(i));
            }
            return result;
        }

    }
}
