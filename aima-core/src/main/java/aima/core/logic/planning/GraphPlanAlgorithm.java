package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.*;

public class GraphPlanAlgorithm {
    List<List<ActionSchema>> graphPlan(Problem problem) {
        Graph graph = initialPlanningGraph(problem);
        List<Literal> goals = conjuncts(problem.goalState);
        Hashtable<Integer, List<Literal>> nogoods = new Hashtable<>();
        Level state;
        for (int tl = 0; ; tl++) {
            state = graph.levels.get(2 * tl);
            boolean mutexCheck = true;
            if (state.levelObjects.containsAll(goals)) {
                mutexCheck = false;
                for (Object literal :
                        goals) {
                    List<Object> mutexOfGoal = state.mutexLinks.get(literal);
                    for (Object object :
                            mutexOfGoal) {
                        if (goals.contains(object)) {
                            mutexCheck = true;
                            break;
                        }
                    }
                }
            }
            if (!mutexCheck) {
                List<List<ActionSchema>> solution = extractSolution(graph, goals, graph.numLevels(), nogoods);
                if (solution != null)
                    return solution;
            }
            if (levelledOff(graph) && leveledOff(nogoods))
                return null;
            graph = expandGraph(graph, problem);
        }
    }

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
                } else {
                    nogoods.put(level, goalsCurr);
                    return null;
                }
            }

            level--;
            currLevel = graph.getLevels().get(2 * level);
            goalsCurr.clear();
            if (setToBeTaken != null) {
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
        }
        return solution;
    }

    private Graph expandGraph(Graph graph, Problem problem) {
        return graph.addLevel();
    }

    private boolean leveledOff(Hashtable<Integer, List<Literal>> nogoods) {
        return nogoods.get(nogoods.size()).equals(nogoods.get(nogoods.size() - 1));
    }

    private boolean levelledOff(Graph graph) {
        return graph.levels.get(graph.numLevels()).equals(graph.levels.get(graph.numLevels() - 2));
    }

    private List<Literal> conjuncts(State goalState) {
        return goalState.getFluents();
    }

    private Graph initialPlanningGraph(Problem problem) {
        Level initialLevel = new Level(null, problem);
        return new Graph(problem, initialLevel);
    }

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
