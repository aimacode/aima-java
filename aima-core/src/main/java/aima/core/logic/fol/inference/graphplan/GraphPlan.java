package aima.core.logic.fol.inference.graphplan;

import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.util.datastructure.LabeledGraph;
import aima.core.util.datastructure.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 383.<br>
 * <br>
 *
 * <pre>
 * <code>
 * function GraphPlan(problem) returns solution or failure
 *
 *   graph &larr; INITIAL-PLANNING-GRAPH(problem)
 *   goals &larr; CONJUNCTS(problem.GOAL)
 *   nogoods &larr; an empty hash table
 *   for tl = 0 to &infin; do
 *       if goals all non-mutex in S<sub>t</sub> of graph then
 *           solution &larr; EXTRACT-SOLUTION(graph, goals, NUMLEVELS(graph), nogoods)
 *           if solution &ne; failure then return solution
 *       if graph and nogoods have both leveled off then return failure
 *       graph &larr; EXPAND-GRAPH(graph, problem)
 * </code>
 * </pre>
 *
 * Figure 10.9 The GraphPlan algorithm. GraphPlan calls Expand-Graph to add a
 * level until either a solution is found by Extract-Solution, or no solution is possible.
 *
 * @author Matt Grenander
 */
public class GraphPlan {
    /**
     * function GraphPlan(problem) returns solution or failure
     *
     * @param problem
     *               provides a PDDL description of the problem
     * @return a list of actions describing a solution for the given problem or
     *         null if no solution is found (i.e failure)
     */
    public List<PDDLAction> graphPlan(PDDL problem) {

        // graph &larr; INITIAL-PLANNING-GRAPH(problem)
        LabeledGraph<GraphPlanNode,MutexLink> graph = initialPlanningGraph(problem);
        // goals &larr; CONJUNCTS(problem.GOAL)
        List<Literal> goals = conjuncts(problem.getGoal());
        // nogoods &larr; an empty hash table
        HashSet<Pair<Integer,List<Literal>>> nogoods = new HashSet<>();

        // for tl = 0 to &infin; do
        for(int t = 0; true; t++) {
            // if goals all non-mutex in S<sub>t</sub> of graph then
            if(allNonMutex(goals,graph,t)) {
                // solution &larr; EXTRACT-SOLUTION(graph, goals, NUMLEVELS(graph), nogoods)
                List<PDDLAction> solution = solutionExtractor.extractSolution(graph,goals,numLevels(graph),nogoods);
                // if solution &ne; failure then return solution
                if(solution != null) { return solution; }
            }

            // if graph and nogoods have both leveled off then return failure
            if(haveLeveledOff(graph,nogoods,goals)) { return null; }

            // graph &larr; EXPAND-GRAPH(graph, problem)
            graph = expandGraph(graph,problem);
        }
    }

    //
    //SUPPORTING CODE
    private SolutionExtractor solutionExtractor = null;

    /**
     * Constructor for GraphPlan
     * @param solutionExtractor
     *                           object that will extract a solution from a planning graph
     */
    public GraphPlan(SolutionExtractor solutionExtractor) {
        this.solutionExtractor = solutionExtractor;
    }

    /**
     * Initialize the planning graph given a PDDL problem description
     * @param problem
     *                 a PDDL object that describes the problem statement
     * @return the initial planning graph for the problem
     */
    private static LabeledGraph<GraphPlanNode,MutexLink> initialPlanningGraph(PDDL problem) {
        List<Literal> literals = CNFToLiteral(problem.getInit());
        LabeledGraph<GraphPlanNode,MutexLink> graph = new LabeledGraph<>();
        for (Literal literal: literals) {
            graph.addVertex(new PlanGraphState(0,literal));
        }
        return graph;
    }

    /**
     * Returns a list of simpler sentences from a goal sentence in CNF form
     * @param goal
     *             the CNF goal sentence
     * @return a list of Literals from the CNF goal, or null if goal is null
     */
    private static List<Literal> conjuncts(CNF goal) {
        if (goal == null) {
            return null;
        }

        List<Clause> clauseGoals = goal.getConjunctionOfClauses();
        ArrayList<Literal> literalGoals = new ArrayList<>(goal.getNumberOfClauses());
        for (Clause clause: clauseGoals) { //Each clause is a unit clause
            literalGoals.add((Literal)clause.getLiterals().toArray()[0]);
        }
        return literalGoals;
    }

    /**
     * Checks if all the goal vertices appear at level t in the planning graph and are non-mutex
     * @param goals
     *              List of goal literals
     * @param graph
     *              Planning graph
     * @param t
     *              The number of levels in the graph
     * @return boolean indicating if all goals are non-mutex or not
     */
    private static boolean allNonMutex(List<Literal> goals, LabeledGraph<GraphPlanNode,MutexLink> graph, int t) {
        //Find all vertices at level S_t
        List<GraphPlanNode> vertices = graph.getVertexLabels();
        List<GraphPlanNode> validVertices = new ArrayList<>();
        List<Literal> validLiterals = new ArrayList<>();

        for (GraphPlanNode vertex: vertices) {
            //If the level is at t AND the state type is state AND the vertex is contained in goals, we add the vertex to validVertices
            if (vertex.getLevel() == t && vertex instanceof PlanGraphState && goals.contains(((PlanGraphState) vertex).getLiteral())) {
                validVertices.add(vertex);
                validLiterals.add(((PlanGraphState) vertex).getLiteral());
            }
        }

        //Check if all goals are at level S_t
        for (Literal goal: goals) {
            if (!validLiterals.contains(goal)) {
                return false;
            }
        }

        //Check if all goals are non-mutex
        for (GraphPlanNode v1: validVertices) {
            for (GraphPlanNode v2: validVertices) {
                if (!v1.equals(v2)) { //If the vertices are the same we continue
                    MutexLink edge = graph.get(v1,v2);
                    if (edge != null && edge == MutexLink.MUTEX) { //The edge is mutex. Return false.
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Returns the number of levels in our graph
     * @param graph
     *               graph to search
     * @return the deepest level in the graph
     */
    static int numLevels(LabeledGraph<GraphPlanNode,MutexLink> graph) {
        List<GraphPlanNode> vertices = graph.getVertexLabels();
        int maxLevel = 0;

        for (GraphPlanNode vertex: vertices) {
           maxLevel = Math.max(maxLevel, vertex.getLevel());
        }
        return maxLevel;
    }

    /**
     * Check if the graph and no-goods have both leveled off
     * @param graph
     *                The planning graph.
     * @param nogoods
     *                The no-goods Hashtable.
     * @return true if they have both leveled off, false otherwise
     */
    private static boolean haveLeveledOff(LabeledGraph<GraphPlanNode,MutexLink> graph, HashSet<Pair<Integer,List<Literal>>> nogoods, List<Literal> goals) {
        //Get max level in graph
        int maxLevel = numLevels(graph);
        if (maxLevel <= 1) { //Not possible to determine if the graph has leveled off yet
            return false;
        }

        //Check if graph has leveled off
        boolean hasGraphLeveledOff = graphLeveledOff(graph,maxLevel);

        //Check if no-goods has leveled off
        boolean hasNoGoodsLeveledOff = noGoodsLeveledOff(nogoods, maxLevel, goals);

        return hasGraphLeveledOff && hasNoGoodsLeveledOff;
    }

    //Checks if graph has leveled off. Helper method for haveLeveledOff method.
    private static boolean graphLeveledOff(LabeledGraph<GraphPlanNode,MutexLink> graph, int maxLevel) {
        //Get all vertices at level maxLevel and maxLevel - 1
        List<GraphPlanNode> allVertices = graph.getVertexLabels();
        Set<Literal> newLevelLiterals = new HashSet<>();
        Set<Literal> oldLevelLiterals = new HashSet<>();

        Set<PDDLAction> newLevelActions = new HashSet<>();
        Set<PDDLAction> oldLevelActions = new HashSet<>();

        for (GraphPlanNode vertex: allVertices) {
            int vertexLevel = vertex.getLevel();
            if (vertexLevel == maxLevel - 1) {
                if (vertex instanceof PlanGraphState) {
                    newLevelLiterals.add(((PlanGraphState) vertex).getLiteral());
                } else if (vertex instanceof  PlanGraphAction){
                    newLevelActions.add(((PlanGraphAction) vertex).getAction());
                }
            } else if (vertexLevel == maxLevel - 2) {
                if (vertex instanceof PlanGraphState) {
                    oldLevelLiterals.add(((PlanGraphState) vertex).getLiteral());
                } else if (vertex instanceof  PlanGraphAction){
                    oldLevelActions.add(((PlanGraphAction) vertex).getAction());
                }
            }
        }

        //Check if the vertices are all the same.
        //Note that these are STATE and ACTION states.
        return newLevelLiterals.equals(oldLevelLiterals) && oldLevelActions.equals(newLevelActions);
    }

    //Checks if no-goods has leveled off. Helper method for haveLeveledOff method.
    private static boolean noGoodsLeveledOff(HashSet<Pair<Integer,List<Literal>>> nogoods, int maxLevel, List<Literal> goals) {
        boolean containsOld = false;
        boolean containsNew = false;
        for (Pair<Integer,List<Literal>> pair: nogoods) {
            if (pair.getSecond().equals(goals)) {
                if (pair.getFirst().equals(maxLevel)) {
                    containsNew = true;
                } else if (pair.getFirst().equals(maxLevel - 1)) {
                    containsOld = true;
                }
            }
        }
        return containsOld && containsNew;
    }

    /**
     * Expands the planning graph one level (one action level, followed by one state level)
     * @param graph
     *                the initial planning graph
     * @param problem
     *                a PDDL description of the problem
     * @return the same planning graph, expanded one level further
     */
    private static LabeledGraph<GraphPlanNode,MutexLink> expandGraph(LabeledGraph<GraphPlanNode,MutexLink> graph, PDDL problem) {
        int currLevel = numLevels(graph);
        addActionLevel(graph,problem,currLevel);
        addStateLevel(graph,currLevel);
        return graph;
    }

    //Helper method for expandGraph. Adds new PlanGraphAction level.
    private static void addActionLevel(LabeledGraph<GraphPlanNode,MutexLink> graph, PDDL problem, int currLevel) {
        //Find actions that have their preconditions satisfied
        List<GraphPlanNode> vertices = graph.getVertexLabels();
        List<Literal> currLiterals = new ArrayList<>();
        for (GraphPlanNode vertex: vertices) {
            if (vertex.getLevel() == currLevel && vertex instanceof PlanGraphState) {
                currLiterals.add(((PlanGraphState) vertex).getLiteral());
            }
        }

        List<PDDLAction> actionSchema = problem.getActions();
        List<PlanGraphAction> newActions = new ArrayList<>();
        for (PDDLAction action: actionSchema) {
            //See if preconditions are satisfied for this action
            List<Literal> precondLiterals = CNFToLiteral(action.getPrecond());

            if (currLiterals.containsAll(precondLiterals)) {
                PlanGraphAction v = new PlanGraphAction(currLevel,action);
                graph.addVertex(v);
                newActions.add(v);

                //Add links between new action vertices and old states
                for (Literal precondLiteral: precondLiterals) {
                    graph.set(v, findStateVertex(currLevel,precondLiteral,graph), MutexLink.NONMUTEX);
                }
            }
        }

        //Add mutex links
        if (newActions.isEmpty()) { //No new actions
            return;
        }

        for (PlanGraphAction v1: newActions) {
            inner:for (PlanGraphAction v2: newActions) {
                if (!v1.equals(v2)) {
                    List<Literal> v1Effects = CNFToLiteral(v1.getAction().getEffect());
                    List<Literal> v2Effects = CNFToLiteral(v2.getAction().getEffect());
                    List<Literal> v1Precond = CNFToLiteral(v1.getAction().getPrecond());
                    List<Literal> v2Precond = CNFToLiteral(v2.getAction().getPrecond());

                    //Condition 1: Inconsistent effects
                    for (Literal e1: v1Effects) {
                        for (Literal e2: v2Effects) {
                            if (e1.isNegativeOf(e2)) {
                                graph.set(v1,v2,MutexLink.MUTEX);
                                continue inner;
                            }
                        }
                    }

                    //Condition 2: Interference
                    for (Literal e1: v1Effects) {
                        for (Literal p2: v2Precond) {
                            if (e1.isNegativeOf(p2)) {
                                graph.set(v1,v2,MutexLink.MUTEX);
                                continue inner;
                            }
                        }
                    }

                    for (Literal e2: v2Effects) {
                        for (Literal p1: v1Precond) {
                            if (e2.isNegativeOf(p1)) {
                                graph.set(v1,v2,MutexLink.MUTEX);
                                continue inner;
                            }
                        }
                    }

                    //Condition 3: Competing needs
                    for (Literal p1: v1Precond) {
                        for (Literal p2: v2Precond) {
                            //See if previous level contains the two preconditions
                            if (currLiterals.contains(p1) && currLiterals.contains(p2)) {
                                //See if they are mutex
                                MutexLink edge = graph.get(findStateVertex(currLevel,p1,graph),findStateVertex(currLevel,p2,graph));
                                if (edge != null && edge == MutexLink.MUTEX) {
                                    graph.set(v1,v2,MutexLink.MUTEX);
                                    continue inner;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Helper method for expandGraph. Adds new PlanGraphState level.
    private static void addStateLevel(LabeledGraph<GraphPlanNode,MutexLink> graph, int currLevel) {
        //Get new actions states
        List<GraphPlanNode> vertices = graph.getVertexLabels();
        List<PlanGraphAction> newActionStates = new ArrayList<>();
        for (GraphPlanNode vertex: vertices) {
            if (vertex.getLevel() == currLevel && vertex instanceof PlanGraphAction) {
                newActionStates.add((PlanGraphAction)vertex);
            }
        }

        //Add effects of new actions
        List<PlanGraphState> newStates = new ArrayList<>();
        for (PlanGraphAction newActionState: newActionStates) {
            List<Literal> effects = CNFToLiteral(newActionState.getAction().getEffect());
            for(Literal effect: effects) {
                if (findStateVertex(currLevel+1,effect,graph) == null) { //Don't add the same vertex twice
                    PlanGraphState newState = new PlanGraphState(currLevel+1,effect);
                    graph.addVertex(newState);
                    newStates.add(newState);
                }
            }
        }

        //Add no-ops
        List<PlanGraphState> noOps = new ArrayList<>();
        for (GraphPlanNode vertex: vertices) {
            if (vertex.getLevel() == currLevel && vertex instanceof PlanGraphState) {
                if (findStateVertex(currLevel+1,((PlanGraphState) vertex).getLiteral(),graph) == null) {
                    PlanGraphState newVertex = new PlanGraphState(currLevel+1,((PlanGraphState) vertex).getLiteral());
                    noOps.add(newVertex);
                    newStates.add(newVertex);
                }
            }
        }

        for (PlanGraphState noOp: noOps) {
            graph.addVertex(noOp);
        }

        //Add mutex between new literals
        for (PlanGraphState v1: newStates) {
            for (PlanGraphState v2: newStates) {
                if(!v1.equals(v2)) {
                    Literal l1 = v1.getLiteral();
                    Literal l2 = v2.getLiteral();

                    //Condition 1: Negation of another
                    if (l1.isNegativeOf(l2)) {
                        graph.set(v1,v2,MutexLink.MUTEX);
                        continue;
                    }

                    //Condition 2: Inconsistent support
                    List<PlanGraphAction> v1Actions = new ArrayList<>();
                    List<PlanGraphAction> v2Actions = new ArrayList<>();
                    for (PlanGraphAction newActionState: newActionStates) {
                        List<Literal> effects = CNFToLiteral(newActionState.getAction().getEffect());
                        if (effects.contains(l1)) {
                            v1Actions.add(newActionState);
                        } else if (effects.contains(l2)) {
                            v2Actions.add(newActionState);
                        }
                    }

                    //Check if v1Actions and v2Actions have any non-mutex links
                    boolean inconsistent = true;
                    outer:for (PlanGraphAction a1: v1Actions) {
                        for (PlanGraphAction a2: v2Actions) {
                            if (!a1.equals(a2) && graph.get(a1,a2) == null) {
                                inconsistent = false;
                                break outer;
                            }
                        }
                    }

                    if(inconsistent) { //Add mutex link
                        graph.set(v1,v2,MutexLink.MUTEX);
                    }
                }
            }
        }
    }

    //Converts CNF to a more useful list of literals
    static List<Literal> CNFToLiteral(CNF cnf) {
        List<Clause> clauses = cnf.getConjunctionOfClauses();
        List<Literal> literals = new ArrayList<>();
        for (Clause clause: clauses) {
            if (clause.isUnitClause()) {
                literals.add((Literal)clause.getLiterals().toArray()[0]);
            } else if (clause.getNumberLiterals() == 0) { //Empty clause
                return literals;
            } else { //Each clause should be unit clause by the formulation of PDDLs
                throw new IllegalStateException("Each clause should be a unit clause.");
            }
        }
        return literals;
    }

    //Finds the specific vertex reference for a state
    private static PlanGraphState findStateVertex(int level, Literal literal, LabeledGraph<GraphPlanNode,MutexLink> graph) {
        List<GraphPlanNode> vertices = graph.getVertexLabels();
        for(GraphPlanNode vertex: vertices) {
            if (vertex.getLevel() == level && vertex instanceof PlanGraphState && ((PlanGraphState) vertex).getLiteral().equals(literal)) {
                return (PlanGraphState)vertex;
            }
        }
        return null;
    }
}

/**
 * Enum to describe whether a link in the planning graph is mutex or non-mutex
 */
enum MutexLink { MUTEX, NONMUTEX }