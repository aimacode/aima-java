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
        startGraphPlanDemo(problems);
        startForwardStateSpaceSearchDemo(problems);
        startBackwardStateSpaceSearchDemo(problems);
    }

    public static void startGraphPlanDemo(List<PlanningProblem> problems) {
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();

        int i = 1;
        for (PlanningProblem problem : problems) {
            System.out.println("\n\nProblem " + i++ + " (using GraphPlan)");
            System.out.println("Initial State:");
            System.out.println(problem.getInitialState().getFluents());
            System.out.println("Goal State:");
            System.out.println(problem.getGoalState().getFluents());

            long start = System.currentTimeMillis();
            List<List<ActionSchema>> solution = algorithm.graphPlan(problem);
            long duration = System.currentTimeMillis() - start;
            System.out.println("Time for Planning [ms]: " + duration);
            System.out.println("Levels: " + algorithm.getGraph().numLevels());
            if (solution != null) {
                System.out.println("Plan:");
                List<ActionSchema> sol = algorithm.asFlatList(solution);
                sol.forEach(System.out::println);
            }
        }
    }

    // uses simple breadth-first search (no heuristics to limit search space)
    public static void startForwardStateSpaceSearchDemo(List<PlanningProblem> problems) {
        int i = 1;
        for (PlanningProblem pProblem : problems) {
            System.out.println("\n\nProblem " + i++ + " (using ForwardStateSpaceSearch)");
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
    }

    // uses simple breadth-first search (no heuristics to limit search space)
    public static void startBackwardStateSpaceSearchDemo(List<PlanningProblem> problems) {
        int i = 1;
        for (PlanningProblem pProblem : problems) {
            System.out.println("\n\nProblem " + i++ + " (using BackwardStateSpaceSearch)");
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
    }
}
