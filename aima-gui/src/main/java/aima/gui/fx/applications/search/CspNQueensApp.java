package aima.gui.fx.applications.search;

import java.util.Arrays;
import java.util.List;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.search.csp.*;
import aima.core.search.csp.examples.NQueensCSP;
import aima.core.search.csp.inference.AC3Strategy;
import aima.core.search.csp.inference.ForwardCheckingStrategy;
import aima.core.search.framework.Metrics;
import aima.core.util.datastructure.XYLocation;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
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
public class CspNQueensApp extends IntegrableApplication {

    public static void main(String[] args) {
        launch(args);
    }

    public final static String PARAM_STRATEGY = "strategy";
    public final static String PARAM_VAR_SELECT = "varSelect";
    public final static String PARAM_VAL_SELECT = "valOrder";
    public final static String PARAM_INFERENCE = "inference";

    public final static String PARAM_BOARD_SIZE = "boardSize";

    private NQueensViewCtrl stateViewCtrl;
    private SimulationPaneCtrl simPaneCtrl;
    private CSP csp;
    private SolutionStrategy solver;
    private ProgressAnalyzer progressAnalyzer = new ProgressAnalyzer();

    @Override
    public String getTitle() {
        return "CSP N-Queens App";
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

        SimulationPaneBuilder builder = new SimulationPaneBuilder();
        builder.defineParameters(params);
        builder.defineStateView(stateView);
        builder.defineInitMethod(this::initialize);
        builder.defineSimMethod(this::simulate);
        simPaneCtrl = builder.getResultFor(root);
        simPaneCtrl.setParam(SimulationPaneCtrl.PARAM_SIM_SPEED, 0);

        return root;
    }

    protected List<Parameter> createParameters() {
        Parameter p1 = new Parameter(PARAM_STRATEGY, "Backtracking", "Min-Conflicts");
        Parameter p2 = new Parameter(PARAM_VAR_SELECT, "Default", "MRV", "DEG", "MRV&DEG");
        Parameter p3 = new Parameter(PARAM_VAL_SELECT, "Default", "LCV");
        Parameter p4 = new Parameter(PARAM_INFERENCE, "None", "FC", "AC3");
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
        csp = new NQueensCSP(simPaneCtrl.getParamAsInt(PARAM_BOARD_SIZE));
        Object strategy = simPaneCtrl.getParamValue(PARAM_STRATEGY);
        if (strategy.equals("Backtracking")) {
            BacktrackingStrategy bSolver = new BacktrackingStrategy();
            switch ((String) simPaneCtrl.getParamValue(PARAM_VAR_SELECT)) {
                case "MRV": bSolver.set(CspHeuristics.mrv()); break;
                case "DEG": bSolver.set(CspHeuristics.deg()); break;
                case "MRV&DEG": bSolver.set(CspHeuristics.mrvDeg()); break;
            }
            switch ((String) simPaneCtrl.getParamValue(PARAM_VAL_SELECT)) {
                case "LCV": bSolver.set(CspHeuristics.lcv()); break;
            }
            switch ((String) simPaneCtrl.getParamValue(PARAM_INFERENCE)) {
                case "FC": bSolver.set(new ForwardCheckingStrategy()); break;
                case "AC3": bSolver.set(new AC3Strategy()); break;
            }
            solver = bSolver;
        } else if (strategy.equals("Min-Conflicts")) {
            solver = new MinConflictsStrategy(1000);

        }
        solver.addCSPStateListener(progressAnalyzer);
        progressAnalyzer.reset();
        stateViewCtrl.update(new NQueensBoard(csp.getVariables().size()));
        simPaneCtrl.setStatus("");
    }

    @Override
    public void cleanup() {
        simPaneCtrl.cancelSimulation();
    }

    /**
     * Starts the experiment.
     */
    public void simulate() {
        Assignment solution = solver.solve(csp);
        if (solution != null) {
            NQueensBoard board = getBoard(solution);
            stateViewCtrl.update(board);
        }
        simPaneCtrl.setStatus(progressAnalyzer.getResults().toString());
    }

    private NQueensBoard getBoard(Assignment assignment) {
        NQueensBoard board = new NQueensBoard(csp.getVariables().size());
        for (Variable var : assignment.getVariables()) {
            int col = Integer.parseInt(var.getName().substring(1)) - 1;
            int row = ((int) assignment.getValue(var)) - 1;
            board.addQueenAt(new XYLocation(col, row));
        }
        return board;
    }

    /**
     * Caution: While the background thread should be slowed down, updates of
     * the GUI have to be done in the GUI thread!
     */
    private void updateStateView(NQueensBoard board) {
        Platform.runLater(() -> stateViewCtrl.update(board));
        simPaneCtrl.waitAfterStep();
    }

    protected class ProgressAnalyzer implements CspListener {
        private int assignmentCount = 0;
        private int domainCount = 0;

        @Override
        public void stateChanged(Assignment assignment, CSP csp) {
            updateStateView(getBoard(assignment));
            ++assignmentCount;
        }

        @Override
        public void stateChanged(CSP csp) {
            ++domainCount;
        }

        public void reset() {
            assignmentCount = 0;
            domainCount = 0;
        }

        public Metrics getResults() {
            Metrics result = new Metrics();
            result.set("assignmentChanges", assignmentCount);
            if (domainCount != 0)
                result.set("domainChanges", domainCount);
            return result;
        }
    }
}
