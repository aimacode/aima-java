package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.*;
import java.util.stream.Collectors;

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
 * @author Ruediger Lunde
 */
public class GraphPlanAlgorithm {

    /** Graph which was created in the last <code>graphPlan</code> call. */
    private Graph graph;

    public Graph getGraph() {
        return graph;
    }

    /**
     * function GRAPHPLAN(problem) returns solution or failure
     *
     * @param problem the planning problem for which the plan is to be created
     * @return a solution or null
     */
    public List<List<ActionSchema>> graphPlan(PlanningProblem problem) {
        // graph ← INITIAL-PLANNING-GRAPH(problem)
        graph = new Graph(problem);
        // goals ← CONJUNCTS(problem.GOAL)
        List<Literal> goals = problem.getGoal();
        // nogoods ← an empty hash table
        Hashtable<Integer, List<Literal>> nogoods = new Hashtable<>();
        Level<Literal, ActionSchema> state;
        // for tl = 0 to ∞ do
        for (int tl = 0; ; tl++) {
            // St
            state = graph.getLiteralLevel(tl);
            // if goals all non-mutex in St of graph then
            if (checkAllGoalsNonMutex(state, goals)) {
                // solution ← EXTRACT-SOLUTION(graph, goals, NUMLEVELS(graph), nogoods)
                List<List<ActionSchema>> solution = extractSolution(graph, goals, tl, nogoods);
                // if solution ≠ failure then return solution
                if (solution != null)
                    return solution;
            } else {
                // seems to be missing in the book - but needed to guarantee termination! (RLu)
                nogoods.put(tl, goals);
            }
            // if graph and nogoods have both leveled off then return failure
            if (graph.levelledOff() && leveledOff(nogoods))
                return null;
            // graph ← EXPAND-GRAPH(graph, problem)
            graph.expand(problem);
        }
    }

    /**
     * This method extracts a solution from the planning graph.
     * <p>
     * Artificial Intelligence A Modern Approach (3rd Edition): page 384.<br>
     * <p>
     * We can define EXTRACT -SOLUTION as a backward search problem, where
     * each state in the search contains a pointer to a level in the planning graph and a set of
     * unsatisfied goals. We define this search problem as follows:
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
     * Here a simple depth-first search is used.
     *
     * @param graph    The planning graph.
     * @param goals    Goals of the planning problem.
     * @param level    Index of the level to be analyzed (starting with last level in the graph).
     * @param nogoods  A hash table to store previously calculated results.
     * @return a solution if found else null
     */
    private List<List<ActionSchema>> extractSolution(Graph graph, List<Literal> goals, int level,
                                                     Hashtable<Integer, List<Literal>> nogoods) {
        if (level <= 0)
            return new ArrayList<>();
        if (nogoods.containsKey(level) && nogoods.get(level).contains(goals))
            return null;

        Level<Literal, ActionSchema> currLevel = graph.getLiteralLevel(level);
        List<List<ActionSchema>> setOfPossibleActions = new ArrayList<>();
        HashMap<ActionSchema, List<ActionSchema>> mutexLinks = currLevel.getPrevLevel().getMutexLinks();
        for (Literal literal : goals) {
            List<ActionSchema> possibleActionsPerLiteral = new ArrayList<>(currLevel.getLinkedPrevObjects(literal));
            setOfPossibleActions.add(possibleActionsPerLiteral);
        }
        List<List<ActionSchema>> allPossibleSubSets = generateCombinations(setOfPossibleActions);
        for (List<ActionSchema> possibleSet : allPossibleSubSets) {
            boolean validSet = true;
            for (int i = 0; i < possibleSet.size() && validSet; i++) {
                ActionSchema firstAction = possibleSet.get(i);
                for (int j = i + 1; j < possibleSet.size() &&  validSet; j++) {
                    ActionSchema secondAction = possibleSet.get(j);
                    if (mutexLinks.containsKey(firstAction) && mutexLinks.get(firstAction).contains(secondAction))
                        validSet = false;
                }
            }
            if (validSet) {
                List<Literal> newGoals = new ArrayList<>();
                for (ActionSchema action : possibleSet) {
                    for (Literal literal : action.getPrecondition())
                        if (!newGoals.contains(literal))
                            newGoals.add(literal);
                }
                newGoals.sort(Comparator.comparing(Literal::hashCode)); // defined order necessary for nogood test
                List<List<ActionSchema>> solution = extractSolution(graph, newGoals, level-1, nogoods);
                if (solution != null) {
                    solution.add(possibleSet);
                    return solution;
                }
            }
        }
        nogoods.put(level, goals);
        return null;
    }

    public List<ActionSchema> asFlatList(List<List<ActionSchema>> solution) {
        return solution.stream().flatMap(Collection::stream)
                .filter(actionSchema -> !ActionSchema.NO_OP.equals(actionSchema.getName()))
                .collect(Collectors.toList());
    }

    /**
     * This method is used to check if all goals are present in a particular state
     * and none of them has a mutex link.
     *
     * @param level The current level in which to check for the goals.
     * @param goals List of goals to be checked
     * @return Boolean representing if goals all non mutex in St
     */
    private boolean checkAllGoalsNonMutex(Level<Literal, ActionSchema> level, List<Literal> goals) {
        if (!level.getLevelObjects().containsAll(goals))
            return false;
        boolean mutexCheck = false;
        for (Literal literal : goals) {
            List<Literal> mutexOfGoal = level.getMutexLinks().get(literal);
            if (mutexOfGoal != null) {
                for (Object object : mutexOfGoal) {
                    if (goals.contains(object)) {
                        mutexCheck = true;
                        break;
                    }
                }
            }
        }
        return (!mutexCheck);
    }

    private boolean leveledOff(Hashtable<Integer, List<Literal>> nogoods) {
        int lastLevel = nogoods.size()-1;
        if (lastLevel < 1)
            return false;
        return nogoods.get(lastLevel).equals(nogoods.get(lastLevel-1));
    }

    public List<List<ActionSchema>> generateCombinations(List<List<ActionSchema>> actionLists) {
        List<List<ActionSchema>> result = new ArrayList<>();
        if (actionLists.size() == 1) {
            result.add(actionLists.get(0));
        } else if (actionLists.size() > 1) {
            result = combineTwoLists(actionLists.get(0), actionLists.get(1));
            if (actionLists.size() > 2) {
                for (int i = 2; i < actionLists.size(); i++)
                    result = combineExtraList(result, actionLists.get(i));
            }
        }
        // sorting by increasing number of actions might reduce execution costs of first found solution.
        result =  result.stream().sorted
                (Comparator.comparing(x -> x.stream().filter(y -> !ActionSchema.NO_OP.equals(y.getName())).count()))
                .collect(Collectors.toList());
        return result;
    }

    // Helper methods for combinations and permutations.
    public List<List<ActionSchema>> combineTwoLists(List<ActionSchema> firstList, List<ActionSchema> secondList) {
        List<List<ActionSchema>> result = new ArrayList<>();
        for (ActionSchema firstAction : firstList) {
            for (ActionSchema secondAction : secondList) {
                result.add(Arrays.asList(firstAction, secondAction));
            }
        }
        return result;
    }

    public List<List<ActionSchema>> combineExtraList(List<List<ActionSchema>> combinedList, List<ActionSchema> newList) {
        List<List<ActionSchema>> result = new ArrayList<>();
        for (List<ActionSchema> combined : combinedList) {
            for (ActionSchema action : newList) {
                List<ActionSchema> tempList = new ArrayList<>(combined);
                tempList.add(action);
                result.add(tempList);
            }
        }
        return result;
    }
}
