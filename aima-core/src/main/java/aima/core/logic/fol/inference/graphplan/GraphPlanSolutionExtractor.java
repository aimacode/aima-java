package aima.core.logic.fol.inference.graphplan;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.util.datastructure.LabeledGraph;
import aima.core.util.datastructure.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * A backwards search algorithm that attempts to find a solution in a given planning graph.
 * @author Matt Grenander
 */
public class GraphPlanSolutionExtractor implements SolutionExtractor {
    private static boolean triedBest;
    private static int tries;

    /**
     * Attempts to extract a solution from a planning graph given a set of goals.
     * @param graph
     *              the planning graph
     * @param goals
     *              a list of literals that represents the goals
     * @param numLevels
     *              the maximum level in the planning graph
     * @param nogoods
     *              a HashSet that holds the previous failures from this method
     * @return a list of actions if a solution is found, null otherwise
     */
    public List<PDDLAction> extractSolution(LabeledGraph<GraphPlanNode,MutexLink> graph, List<Literal> goals, int numLevels, HashSet<Pair<Integer,List<Literal>>> nogoods) {
        //Check if we already marked this attempt as no good
        for (Pair<Integer,List<Literal>> pair: nogoods) {
            Set<Literal> noGoodLiterals = new HashSet<>(pair.getSecond());
            Set<Literal> goalLiterals = new HashSet<>(goals);

            if (pair.getFirst().equals(numLevels) && noGoodLiterals.equals(goalLiterals)) {
                return null;
            }
        }

        //Base case. Check if we are at the initial level, S_0
        if (numLevels == 0) {
            //Find initial states and see if the goals are contained
            List<GraphPlanNode> vertices = graph.getVertexLabels();
            List<Literal> initStates = new ArrayList<>();

            for (GraphPlanNode vertex: vertices) {
                if (vertex instanceof PlanGraphState && vertex.getLevel() == 0) {
                    initStates.add(((PlanGraphState) vertex).getLiteral());
                }
            }

            //See if the initial states contain the goals
            if (initStates.containsAll(goals)) {
                return new ArrayList<>(); //Return empty list to indicate success
            } else { //Failure as we could not satisfy the goals
                return null;
            }
        }

        //Set indicator variables
        triedBest = false;
        tries = 0;

        while(true) {
            //Find best actions to take
            List<PDDLAction> actionsToTake = findBestActions(graph, goals, numLevels);

            //There are no valid actions to take
            if (actionsToTake == null) {
                break;
            }

            //We attempt to satisfy the preconditions of actionsToTake recursively
            //Get preconditions of actions
            List<Literal> newGoals = new ArrayList<>();
            for (PDDLAction action: actionsToTake) {
                newGoals.addAll(GraphPlan.CNFToLiteral(action.getPrecond()));
            }

            //Attempt recursion
            List<PDDLAction> recurseActions = extractSolution(graph, newGoals, numLevels - 1, nogoods);

            if (recurseActions != null) { //Recursion was successful - append actionsToTake and return
                recurseActions.addAll(actionsToTake);
                return recurseActions;
            } else { //Try a different plan, if there is one
                tries++;
            }
        }

        //Mark this attempt as no good
        nogoods.add(new Pair<>(numLevels,goals));
        return null;
    }

    /**
     * Finds the best action plan for the planning graph based on the heuristic given on pg. 385, given a set of goals and the current level.
     * @param graph
     *              the planning graph
     * @param goals
     *              the goal literals
     * @param level
     *              the current level we are searching at
     * @return A valid list of actions to take to accomplish the goals, or null if there are none.
     */
    private static List<PDDLAction> findBestActions(LabeledGraph<GraphPlanNode,MutexLink> graph, List<Literal> goals, int level) {
        if(!triedBest) { //Find the heuristic solution based on pg. 385
            triedBest = true; //Ensures we don't try this again if it fails
            PriorityQueue<Literal> sortedGoals = findSortedGoals(graph,goals);
            List<PlanGraphAction> potentialActions = new ArrayList<>();

            for (Literal goal: sortedGoals) {
                //For each goal, pick the action that covers it with the lowest sum of precondition level costs
                PlanGraphAction potentialAction = findBestActionForGoal(graph,goal,level);

                if (potentialAction == null) { //We could not find any action for goal. So this problem is not solvable.
                    return null;
                } else if (!potentialActions.contains(potentialAction)) {
                    potentialActions.add(potentialAction);
                }
            }

            //If valid, we return the action plan
            if (areValidActions(potentialActions,goals,graph)) {
                List<PDDLAction> actions = new ArrayList<>();
                for (PlanGraphAction actionState: potentialActions) {
                    actions.add(actionState.getAction());
                }
                return actions;
            }
        }

        //If findBestAction fails, we find any valid action plan
        return findAnyActions(graph,goals,level);
    }

    /**
     * Finds any valid action plan for the planning graph, given a set of goals and the current level.
     * @param graph
     *              the planning graph
     * @param goals
     *              the goal literals
     * @param level
     *              the current level we are searching at
     * @return A valid list of actions to take to accomplish the goals, or null if there are none.
     */
    private static List<PDDLAction> findAnyActions(LabeledGraph<GraphPlanNode,MutexLink> graph, List<Literal> goals, int level) {
        List<GraphPlanNode> vertices = graph.getVertexLabels();
        List<PlanGraphAction> allActionsInLevel = new ArrayList<>();
        for (GraphPlanNode vertex: vertices) {
            if (vertex instanceof PlanGraphAction && vertex.getLevel() == level - 1) {
                allActionsInLevel.add((PlanGraphAction)vertex);
            }
        }

        //We iterate through all elements in the power set of the actions (i.e. every subset in A_i-1)
        int stop = (int)Math.pow(2,allActionsInLevel.size());
        while (tries < stop) {
            //Get binary representation of tries, and pad with appropriate number of zeroes
            String binary = Integer.toBinaryString(tries);
            if(binary.length() != allActionsInLevel.size()) {
                binary = String.format("%0$0" + allActionsInLevel.size() + "d", Integer.parseInt(binary));
            }

            List<PlanGraphAction> actionsToTry = new ArrayList<>();

            //In the binary representation, if we find a 1, we take that action
            //If we find a 0, we do not take the action. This gives us an ordering on the power set of the available actions.
            for (int i = 0; i < binary.length(); i++) {
                if (binary.charAt(i) == '1') {
                    actionsToTry.add(allActionsInLevel.get(i));
                }
            }

            //If actions are valid, we return them. Otherwise increment tries and attempt again.
            if (areValidActions(actionsToTry,goals,graph)) {
                List<PDDLAction> returnedActions = new ArrayList<>();
                for (PlanGraphAction action: actionsToTry) {
                    returnedActions.add(action.getAction());
                }
                return returnedActions;
            } else {
                tries++;
            }
        }
        return null;
    }

    /**
     * Sorts the goal literals by level cost and stores the results in a priority queue (pg. 382)
     * @param graph
     *              the planning graph
     * @param goals
     *              the goal literals
     * @return a Priority Queue of the literal goals, sorted by level cost
     */
    private static PriorityQueue<Literal> findSortedGoals(LabeledGraph<GraphPlanNode,MutexLink> graph, List<Literal> goals) {
        PriorityQueue<Literal> levelCost = new PriorityQueue<>((Literal l1, Literal l2)->findLevelCost(l2,graph)-findLevelCost(l1,graph));
        List<GraphPlanNode> vertices = graph.getVertexLabels();

        for (GraphPlanNode vertex: vertices) {
            if (vertex instanceof PlanGraphState && goals.contains(((PlanGraphState) vertex).getLiteral())) {
                Literal vLit = ((PlanGraphState) vertex).getLiteral();
                if (!levelCost.contains(vLit)) {
                    levelCost.add(vLit);
                }
            }
        }
        return levelCost;
    }

    /**
     * Finds the level cost of a literal.
     * @param literal
     *                the goal literal
     * @param graph
     *                the planning graph
     * @return The level cost of the literal.
     */
    private static int findLevelCost(Literal literal, LabeledGraph<GraphPlanNode,MutexLink> graph) {
        List<GraphPlanNode> vertices = graph.getVertexLabels();
        int levelCost = GraphPlan.numLevels(graph);

        for (GraphPlanNode vertex: vertices) {
            if (vertex instanceof PlanGraphState && ((PlanGraphState) vertex).getLiteral().equals(literal) && vertex.getLevel() < levelCost) {
                levelCost = vertex.getLevel();
            }
        }
        return levelCost;
    }

    /**
     * Returns the best action (by smallest preconditions) for a given goal
     * @param graph
     *              the planning graph
     * @param goal
     *              the goal literal
     * @param level
     *              the level we are searching at
     * @return The action with smallest preconditions
     */
    private static PlanGraphAction findBestActionForGoal(LabeledGraph<GraphPlanNode,MutexLink> graph, Literal goal, int level) {
        List<GraphPlanNode> vertices = graph.getVertexLabels();
        List<PlanGraphAction> potentialActions = new ArrayList<>();

        //Find all valid actions
        for (GraphPlanNode vertex: vertices) {
            if (vertex instanceof PlanGraphAction && vertex.getLevel() == level - 1) {
                PlanGraphAction action = ((PlanGraphAction)vertex);
                if (GraphPlan.CNFToLiteral(action.getAction().getEffect()).contains(goal)) {
                    potentialActions.add(action);
                }
            }
        }

        //No actions satisfy the goal, return null
        if (potentialActions.isEmpty()) {
            return null;
        }

        //Find the action with the smallest preconditions, by level cost
        PlanGraphAction bestAction = potentialActions.get(0);
        int bestActionSum = sumPreconditions(bestAction, graph);

        for (PlanGraphAction potentialAction: potentialActions) {
            int potActionSum = sumPreconditions(potentialAction,graph);

            if (potActionSum < bestActionSum) { //We found a better action
                bestAction = potentialAction;
                bestActionSum = potActionSum;
            }
        }

        return bestAction;
    }

    /**
     * Sums the level costs of the preconditions of an action.
     * @param a
     *          the action
     * @param graph
     *          the planning graph, needed to compute the level cost
     * @return The sum described above.
     */
    private static int sumPreconditions(PlanGraphAction a, LabeledGraph<GraphPlanNode,MutexLink> graph) {
        int sum = 0;
        for (Literal l: GraphPlan.CNFToLiteral(a.getAction().getPrecond())) {
            sum += findLevelCost(l,graph);
        }
        return sum;
    }

    /**
     * Determines if the effects of the actions supplied cover the given goals and are non-mutex.
     * @param actions
     *                the actions we are examining
     * @param goals
     *                the goal literals
     * @param graph
     *                the planning graph
     * @return True if the actions cover the goals and are non-mutex. False otherwise.
     */
    private static boolean areValidActions(List<PlanGraphAction> actions, List<Literal> goals, LabeledGraph<GraphPlanNode,MutexLink> graph) {
        //Check if actions are all non-mutex
        if (!actionsNonMutex(actions,graph)) {
            return false;
        }

        List<Literal> allEffects = new ArrayList<>();
        for (PlanGraphAction action: actions) {
            allEffects.addAll(GraphPlan.CNFToLiteral(action.getAction().getEffect()));
        }

        return allEffects.containsAll(goals);
    }

    /**
     * Determines if every pair of actions are non-mutex. Used in the heuristic solution for findBestAction.
     * @param actions
     *                PDDLActions to be checked.
     * @param graph
     *                the planning graph they are in
     * @return true if all actions are non-mutex. False otherwise.
     */
    private static boolean actionsNonMutex(List<PlanGraphAction> actions, LabeledGraph<GraphPlanNode,MutexLink> graph) {
        if (actions.size() <= 1) { //Not possible for pairs to be mutex if there's only one.
            return true;
        }

        //Iterate through each pair
        for(PlanGraphAction a1: actions) {
            for (PlanGraphAction a2: actions) {
                if (!a1.equals(a2)) {
                    MutexLink edge = graph.get(a1,a2);
                    if (edge != null && edge == MutexLink.MUTEX) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}