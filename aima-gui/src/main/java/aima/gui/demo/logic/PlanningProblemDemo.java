package aima.gui.demo.logic;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.planning.*;
import aima.core.search.framework.QueueBasedSearch;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.uninformed.BreadthFirstSearch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Demonstrates how different algorithms solve some example planning problems.
 *
 * @author Ruediger Lunde
 */
public class PlanningProblemDemo {
    public static void main(String[] args) {
        List<PlanningProblem> problems = Arrays.asList(
                PlanningProblemFactory.spareTireProblem(),
                PlanningProblemFactory.goHomeToSFOProblem(),
                PlanningProblemFactory.airCargoTransportProblem()
        );
        for (PlanningProblem pProblem : problems) {
            startGraphPlanDemo(pProblem);
            startHeuristicForwardStateSpaceSearchDemo(pProblem);
            startForwardStateSpaceSearchDemo(pProblem);
            startBackwardStateSpaceSearchDemo(pProblem);
        }
    }

    public static void startGraphPlanDemo(PlanningProblem pProblem) {
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();

        System.out.println("\n\nProblem " + describe(pProblem) + " using GraphPlan");
        System.out.println("Initial State:");
        System.out.println(pProblem.getInitialState().getFluents());
        System.out.println("Goal State:");
        System.out.println(pProblem.getGoalState().getFluents());

        long start = System.currentTimeMillis();
        List<List<ActionSchema>> solution = algorithm.graphPlan(pProblem);
        long duration = System.currentTimeMillis() - start;
        System.out.println("Time for Planning [ms]: " + duration);
        System.out.println("Levels: " + algorithm.getGraph().numLevels());
        if (solution != null) {
            System.out.println("Plan:");
            List<ActionSchema> sol = algorithm.asFlatList(solution);
            sol.forEach(System.out::println);
        }
    }

    public static void startHeuristicForwardStateSpaceSearchDemo(PlanningProblem pProblem) {
        System.out.println("\n\nProblem " + describe(pProblem) + " using HeuristicForwardStateSpaceSearch");
        System.out.println("Initial State:");
        System.out.println(pProblem.getInitialState().getFluents());
        System.out.println("Goal State:");
        System.out.println(pProblem.getGoalState().getFluents());

        long start = System.currentTimeMillis();
        HeuristicForwardStateSpaceSearchAlgorithm algorithm = new HeuristicForwardStateSpaceSearchAlgorithm(pProblem);
        algorithm.setStepCostFn(PlanningProblemDemo::costs);
        Optional<List<ActionSchema>> solution = algorithm.search();
        long duration = System.currentTimeMillis() - start;
        System.out.println("Time for Planning [ms]: " + duration);
        if (solution.isPresent())
            solution.get().forEach(System.out::println);
        else
            System.out.println("No Solution");
    }

    // for testing: add costs for cargo stored in a plane (cargo should't be parked in a plane).
    private static double costs(List<Literal> s1, ActionSchema action, List<Literal> s2) {
        double result = 1;
        for (Literal literal : s2)
            if (literal.isPositiveLiteral() && literal.getAtomicSentence().getSymbolicName().equals("In"))
                result += 1;
        return result;
    }

    public static void startForwardStateSpaceSearchDemo(PlanningProblem pProblem) {
        System.out.println("\n\nProblem " + describe(pProblem) + " using ForwardStateSpaceSearch");
        System.out.println("Initial State:");
        System.out.println(pProblem.getInitialState().getFluents());
        System.out.println("Goal State:");
        System.out.println(pProblem.getGoalState().getFluents());

        ForwardStateSpaceSearchProblem sProblem = new ForwardStateSpaceSearchProblem(pProblem);
        long start = System.currentTimeMillis();
        QueueBasedSearch<List<Literal>, ActionSchema> search = new BreadthFirstSearch<>(new GraphSearch<>());
        Optional<List<ActionSchema>> solution = search.findActions(sProblem);
        long duration = System.currentTimeMillis() - start;
        System.out.println("Time for Planning [ms]: " + duration);
        if (solution.isPresent())
            solution.get().forEach(System.out::println);
        else
            System.out.println("No Solution");
    }

    public static void startBackwardStateSpaceSearchDemo(PlanningProblem pProblem) {
        System.out.println("\n\nProblem " + describe(pProblem) + " using BackwardStateSpaceSearch");
        System.out.println("Initial State:");
        System.out.println(pProblem.getInitialState().getFluents());
        System.out.println("Goal State:");
        System.out.println(pProblem.getGoalState().getFluents());

        BackwardStateSpaceSearchProblem sProblem = new BackwardStateSpaceSearchProblem(pProblem);
        long start = System.currentTimeMillis();
        QueueBasedSearch<List<Literal>, ActionSchema> search = new BreadthFirstSearch<>(new GraphSearch<>());
        Optional<List<ActionSchema>> solution = search.findActions(sProblem);
        long duration = System.currentTimeMillis() - start;
        System.out.println("Time for Planning [ms]: " + duration);
        if (solution.isPresent()) {
            Collections.reverse(solution.get());
            solution.get().forEach(System.out::println);
        } else
            System.out.println("No Solution");
    }

    private static String describe(PlanningProblem pProblem) {
        StringBuilder builder = new StringBuilder();
        String conn = "";
        for (ActionSchema action : pProblem.getActionSchemas()) {
            builder.append(conn).append(action.getName());
            conn = "/";
        }
        return builder.toString();
    }
}
