package aima.gui.demo.logic;

import aima.core.logic.planning.ActionSchema;
import aima.core.logic.planning.GraphPlanAlgorithm;
import aima.core.logic.planning.PlanningProblemFactory;
import aima.core.logic.planning.Problem;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates how the GraphPlanAlgorithm solves different example problems.
 * @author Ruediger Lunde
 */
public class GraphPlanAlgorithmDemo {
    public static void main(String[] args) {
        GraphPlanAlgorithm algorithm = new GraphPlanAlgorithm();
        List<Problem> problems = Arrays.asList(
                PlanningProblemFactory.spareTireProblem(),
                PlanningProblemFactory.goHomeToSFOProblem(),
                PlanningProblemFactory.airCargoTransportProblem());

        int i = 1;
        for (Problem problem : problems) {
            System.out.println("\n\nProblem " + i++);
            System.out.println("Initial State:");
            System.out.println(problem.getInitialState().getFluents());
            System.out.println("Goal State:");
            System.out.println(problem.getGoalState().getFluents());

            long start = System.currentTimeMillis();
            List<List<ActionSchema>> solution = algorithm.graphPlan(problem);
            long duration = System.currentTimeMillis() - start;
            System.out.println("Time for Planning [ms]: " + duration);

            System.out.println("Plan:");
            List<ActionSchema> sol = algorithm.asFlatList(solution);
            sol.forEach(System.out::println);
        }
    }
}
