package aima.gui.fx.applications.search;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.search.csp.*;
import aima.core.search.csp.examples.NQueensCSP;
import aima.core.search.csp.solver.inference.AC3Strategy;
import aima.core.search.csp.solver.inference.ForwardCheckingStrategy;
import aima.core.search.csp.solver.*;
import aima.core.util.datastructure.XYLocation;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
import aima.gui.fx.views.NQueensViewCtrl;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Integrable application which demonstrates how different CSP solution
 * strategies solve the N-Queens problem.
 *
 * @author Ruediger Lunde
 */
public class NQueensCspApp extends IntegrableApplication {

    public static void main(String[] args) {
        launch(args);
    }

    private final static String PARAM_STRATEGY = "strategy";
    private final static String PARAM_VAR_SELECT = "varSelect";
    private final static String PARAM_VAL_SELECT = "valOrder";
    private final static String PARAM_INFERENCE = "inference";

    private final static String PARAM_BOARD_SIZE = "boardSize";

    private NQueensViewCtrl stateViewCtrl;
    private TaskExecutionPaneCtrl taskPaneCtrl;
    private CSP<Variable, Integer> csp;
    private CspSolver<Variable, Integer> solver;
    private CspListener.StepCounter<Variable, Integer> stepCounter = new CspListener.StepCounter<>();

    @Override
    public String getTitle() {
        return "N-Queens CSP App";
    }

    /**
     * Defines state view, parameters, and call-back functions and calls the
     * simulation pane builder to create layout and controller objects.
     */
    @Override
    public Pane createRootPane() {
        BorderPane root = new BorderPane();

        StackPane stateView = new StackPane();
        stateViewCtrl = new NQueensViewCtrl(stateView);

        List<Parameter> params = createParameters();

        TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
        builder.defineParameters(params);
        builder.defineStateView(stateView);
        builder.defineInitMethod(this::initialize);
        builder.defineTaskMethod(this::startExperiment);
        taskPaneCtrl = builder.getResultFor(root);
        taskPaneCtrl.setParam(TaskExecutionPaneCtrl.PARAM_EXEC_SPEED, 0);

        return root;
    }

    protected List<Parameter> createParameters() {
        Parameter p1 = new Parameter(PARAM_STRATEGY, "Backtracking", "Min-Conflicts");
        Parameter p2 = new Parameter(PARAM_VAR_SELECT, "Default", "MRV", "DEG", "MRV&DEG");
        Parameter p3 = new Parameter(PARAM_VAL_SELECT, "Default", "LCV");
        Parameter p4 = new Parameter(PARAM_INFERENCE, "None", "Forward Checking", "AC3");
        p2.setDependency(PARAM_STRATEGY, "Backtracking");
        p3.setDependency(PARAM_STRATEGY, "Backtracking");
        p4.setDependency(PARAM_STRATEGY, "Backtracking");
        Parameter p5 = new Parameter(PARAM_BOARD_SIZE, 4, 8, 16, 32, 64);
        p5.setDefaultValueIndex(1);
        return Arrays.asList(p1, p2, p3, p4, p5);
    }

    /**
     * Displays the initialized board on the state view.
     */
    @Override
    public void initialize() {
        csp = new NQueensCSP(taskPaneCtrl.getParamAsInt(PARAM_BOARD_SIZE));
        Object strategy = taskPaneCtrl.getParamValue(PARAM_STRATEGY);
        if (strategy.equals("Backtracking")) {
            FlexibleBacktrackingSolver<Variable, Integer> bSolver = new FlexibleBacktrackingSolver<>();
            switch ((String) taskPaneCtrl.getParamValue(PARAM_VAR_SELECT)) {
                case "MRV": bSolver.set(CspHeuristics.mrv()); break;
                case "DEG": bSolver.set(CspHeuristics.deg()); break;
                case "MRV&DEG": bSolver.set(CspHeuristics.mrvDeg()); break;
            }
            switch ((String) taskPaneCtrl.getParamValue(PARAM_VAL_SELECT)) {
                case "LCV": bSolver.set(CspHeuristics.lcv()); break;
            }
            switch ((String) taskPaneCtrl.getParamValue(PARAM_INFERENCE)) {
                case "Forward Checking": bSolver.set(new ForwardCheckingStrategy<>()); break;
                case "AC3": bSolver.set(new AC3Strategy<>()); break;
            }
            solver = bSolver;
        } else if (strategy.equals("Min-Conflicts")) {
            solver = new MinConflictsSolver<>(1000);

        }
        solver.addCspListener(stepCounter);
        solver.addCspListener((csp, assign, var) -> { if (assign != null) updateStateView(getBoard(assign));});
        stepCounter.reset();
        stateViewCtrl.update(new NQueensBoard(csp.getVariables().size()));
        taskPaneCtrl.setStatus("");
    }

    @Override
    public void cleanup() {
        taskPaneCtrl.cancelExecution();
    }

    /**
     * Starts the experiment.
     */
    public void startExperiment() {
        Optional<Assignment<Variable, Integer>> solution = solver.solve(csp);
        if (solution.isPresent()) {
            NQueensBoard board = getBoard(solution.get());
            stateViewCtrl.update(board);
        }
    }

    private NQueensBoard getBoard(Assignment<Variable, Integer> assignment) {
        NQueensBoard board = new NQueensBoard(csp.getVariables().size());
        for (Variable var : assignment.getVariables()) {
            int col = Integer.parseInt(var.getName().substring(1)) - 1;
            int row = assignment.getValue(var) - 1;
            board.addQueenAt(new XYLocation(col, row));
        }
        return board;
    }

    /**
     * Caution: While the background thread should be slowed down, updates of
     * the GUI have to be done in the GUI thread!
     */
    private void updateStateView(NQueensBoard board) {
        Platform.runLater(() -> {
            stateViewCtrl.update(board); taskPaneCtrl.setStatus(stepCounter.getResults().toString()); });
        taskPaneCtrl.waitAfterStep();
    }
}
