package aima.gui.fx.applications.logic;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.planning.*;
import aima.core.search.framework.QueueBasedSearch;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.uninformed.BreadthFirstSearch;
import aima.core.search.uninformed.UniformCostSearch;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
import aima.gui.fx.views.SimpleTextViewCtrl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// VM options (Java>8): --module-path ${PATH_TO_FX} --add-modules javafx.controls,javafx.fxml

/**
 * Demonstrates how different algorithms solve some example planning problems.
 * @author Ruediger Lunde
 */
public class PlanningProblemApp extends IntegrableApplication {

    public static void main(String[] args) {
        launch(args);
    }

    protected final static String PARAM_PROBLEM = "problem";
    protected final static String PARAM_STRATEGY = "strategy";

    protected SimpleTextViewCtrl stateViewCtrl;
    protected TaskExecutionPaneCtrl taskPaneCtrl;

    protected PlanningProblem problem;

    @Override
    public String getTitle() {
        return "Planning Problem App";
    }

    /**
     * Defines state view, parameters, and call-back functions and calls the
     * simulation pane builder to create layout and controller objects.
     */
    @Override
    public Pane createRootPane() {
        BorderPane root = new BorderPane();

        StackPane stateView = new StackPane();
        stateViewCtrl = new SimpleTextViewCtrl(stateView);

        List<Parameter> params = createParameters();

        TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
        builder.defineParameters(params);
        builder.defineStateView(stateView);
        builder.defineInitMethod(this::initialize);
        builder.defineTaskMethod(this::startExperiment);
        taskPaneCtrl = builder.getResultFor(root);

        return root;
    }

    protected List<Parameter> createParameters() {
        Parameter p1 = new Parameter(PARAM_PROBLEM, "Air Cargo Transport Problem", "Spare Tire Problem");
        Parameter p2 = new Parameter(PARAM_STRATEGY, "Graphplan", "Heuristic Forward State-Space Search",
                "Forward State-Space Search", "Backward State-Space Search");
        return Arrays.asList(p1, p2);
    }

    /**
     * Displays the selected function on the state view.
     */
    @Override
    public void initialize() {
        problem = null;
        switch (taskPaneCtrl.getParamValueIndex(PARAM_PROBLEM)) {
            case 0:
                problem = PlanningProblemFactory.airCargoTransportProblem();
                break;
            case 1:
                problem = PlanningProblemFactory.spareTireProblem();
                break;
        }
        stateViewCtrl.clear();
        stateViewCtrl.notify("Initial State:\n\t" + problem.getInitialState().getFluents());
        stateViewCtrl.notify("Goal:\n\t" + problem.getGoal());
        stateViewCtrl.notify("Actions:");
        problem.getActionSchemas().forEach(a -> stateViewCtrl.notify(a.toString()));
    }

    @Override
    public void cleanup() {
        taskPaneCtrl.cancelExecution();
    }

    /**
     * Starts the experiment.
     */
    public void startExperiment() {
        try {
            QueueBasedSearch<List<Literal>, ActionSchema> search = null;
            Optional<List<ActionSchema>> solution = Optional.empty();
            Graph graph = null;

            long start = System.currentTimeMillis();
            switch (taskPaneCtrl.getParamValueIndex(PARAM_STRATEGY)) {
                case 0:
                    GraphPlanAlgorithm algorithm1 = new GraphPlanAlgorithm();
                    List<List<ActionSchema>> sol = algorithm1.graphPlan(problem);
                    solution = (sol != null) ? Optional.of(algorithm1.asFlatList(sol)) : Optional.empty();
                    graph = algorithm1.getGraph();
                    break;
                case 1:
                    HeuristicForwardStateSpaceSearchAlgorithm algorithm2 =
                            new HeuristicForwardStateSpaceSearchAlgorithm(problem);
                    algorithm2.setStepCostFn(PlanningProblemApp::costs);
                    solution = algorithm2.search();
                    search = algorithm2.getSearch();
                    break;
                case 2:
                    ForwardStateSpaceSearchProblem fsProblem = new ForwardStateSpaceSearchProblem(problem,
                            problem.getPropositionalisedActions(), PlanningProblemApp::costs);
                    search = new UniformCostSearch<>(new GraphSearch<>());
                    solution = search.findActions(fsProblem);
                    break;
                case 3:
                    BackwardStateSpaceSearchProblem bsProblem = new BackwardStateSpaceSearchProblem(problem);
                    search = new BreadthFirstSearch<>(new GraphSearch<>());
                    solution = search.findActions(bsProblem);
                    solution.ifPresent(Collections::reverse);
                    break;
            }
            long duration = System.currentTimeMillis() - start;
            stateViewCtrl.notify("\nTime for Planning [ms]: " + duration);

            if (graph != null) {
                stateViewCtrl.notify("\nLevel Objects in Graph:");
                for (int i = 0; i < graph.numLevels(); i++){
                    stateViewCtrl.notify(graph.getLiteralLevel(i).getLevelObjects().stream().map(Literal::toString)
                            .sorted().collect(Collectors.toList()).toString());
                    if (i < graph.numLevels()-1)
                        stateViewCtrl.notify(graph.getActionLevel(i).getLevelObjects().stream()
                                .map(ActionSchema::getFullName).sorted()
                                .collect(Collectors.toList()).toString());
                }
            }

            stateViewCtrl.notify("\nSolution:");
            solution.ifPresent(actionSchemas -> actionSchemas.forEach(a -> stateViewCtrl.notify(a.toString())));
            taskPaneCtrl.setStatus("Duration[ms]= " + duration
                    + ", Actions=" + (solution.isPresent() ? solution.get().size() : "-")
                    + ((search != null) ? ", SearchMetrics=" + search.getMetrics() : ""));
        } catch (RuntimeException ex) {
            taskPaneCtrl.setStatus(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    // for testing: add costs for cargo stored in a plane (cargo should't be parked in a plane).
    private static double costs(List<Literal> s1, ActionSchema action, List<Literal> s2) {
        double result = 1;
        for (Literal literal : s2)
            if (literal.isPositiveLiteral() && literal.getAtomicSentence().getSymbolicName().equals("In"))
                result += 0.2;
        return result;
    }
}
