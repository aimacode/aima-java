package aima.gui.fx.applications.search;

import aima.core.environment.sudoku.SudokuDifficulty;
import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Variable;
import aima.core.search.csp.examples.SudokuCSP;
import aima.core.search.csp.solver.*;
import aima.core.search.csp.solver.inference.AC3Strategy;
import aima.core.search.csp.solver.inference.ForwardCheckingStrategy;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
import aima.gui.fx.views.CspViewCtrl;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;

public class SudokuCspApp extends IntegrableApplication {

    public static void main(String[] args) {
        launch(args);
    }

    protected final static String PARAM_STRATEGY = "strategy";
    protected final static String PARAM_DIFFICULTY = "difficulty level";

    protected CspViewCtrl<Variable, Integer> stateViewCtrl; //SudokuViewCtrl
    protected TaskExecutionPaneCtrl taskPaneCtrl;

    protected CSP<Variable, Integer> csp;
    protected CspSolver<Variable, Integer> strategy;
    private int stepCounter;
    private SudokuDifficulty difficultyLevel;
    private Assignment<Variable, Integer> startAssignment;

    @Override
    public String getTitle() {
        return "Sudoku CSP App";
    }

    @Override
    public Pane createRootPane() {
        BorderPane root = new BorderPane();

        StackPane stateView = new StackPane();
        stateViewCtrl = new CspViewCtrl<>(stateView); //new SudokuViewCtrl(stateView);

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
        Parameter p1 = new Parameter(PARAM_STRATEGY, "Backtracking",
                "Backtracking + MRV",
                "Backtracking + DEG",
                "Backtracking + LCV",
                "Backtracking + Forward Checking",
                "Backtracking + Forward Checking + MRV&DEG",
                "Backtracking + AC3",
                "Backtracking + AC3 + MRV&DEG + LCV",
                "Backtracking + Backjumping",
                "Backtracking + Backjumping + MRV&DEG"
        );
        Parameter p2 = new Parameter(PARAM_DIFFICULTY, "Easy", "Medium", "Hard");
        p2.setDefaultValueIndex(2);
        return Arrays.asList(p1, p2);
    }

    @Override
    public void initialize() {
        // CSP
        csp = new SudokuCSP();

        // Difficulty
        switch (taskPaneCtrl.getParamValueIndex(PARAM_DIFFICULTY)) {
            case 0:
                this.difficultyLevel = SudokuDifficulty.EASY;
                break;
            case 1:
                this.difficultyLevel = SudokuDifficulty.MEDIUM;
                break;
            default:
                this.difficultyLevel = SudokuDifficulty.HARD;
                break;
        }

        this.startAssignment = ((SudokuCSP) csp).getStartingAssignment(this.difficultyLevel);

        // Strategy
        switch (taskPaneCtrl.getParamValueIndex(PARAM_STRATEGY)) {
            case 0:
                strategy = new FlexibleBacktrackingSolver<>();
                break;
            case 1: // MRV
                strategy = new FlexibleBacktrackingSolver<Variable, Integer>().set(CspHeuristics.mrv());
                break;
            case 2: // DEG
                strategy = new FlexibleBacktrackingSolver<Variable, Integer>().set(CspHeuristics.deg());
                break;
            case 3: // LCV
                strategy = new FlexibleBacktrackingSolver<Variable, Integer>().set(CspHeuristics.lcv());
                break;
            case 4: // FC
                strategy = new FlexibleBacktrackingSolver<Variable, Integer>().set(new ForwardCheckingStrategy<>());
                break;
            case 5: // FC + MRV&DEG
                strategy = new FlexibleBacktrackingSolver<Variable, Integer>().set(CspHeuristics.mrvDeg())
                        .set(new ForwardCheckingStrategy<>());
                break;
            case 6: // AC3
                strategy = new FlexibleBacktrackingSolver<Variable, Integer>().set(new AC3Strategy<>());
                break;
            case 7: // AC3 + MRV&DEG + LCV
                strategy = new FlexibleBacktrackingSolver<Variable, Integer>().setAll();
                break;
            case 8: // Backjumping
                strategy = new BackjumpingBacktrackingSolver<>();
                break;
            case 9: // Backjumping + MRV&DEG
                strategy = new BackjumpingBacktrackingSolver<Variable, Integer>().set(CspHeuristics.mrvDeg());
                break;
        }

        strategy.addCspListener((csp, assignment, var) -> {
            stepCounter++;
            updateStateView(csp, assignment, var);
        });

        ((SudokuCSP) csp).setDomainsForStartingAssignment(this.startAssignment);

        stateViewCtrl.initialize(csp);
        stateViewCtrl.update(csp, this.startAssignment);
    }

    @Override
    public void cleanup() {
        taskPaneCtrl.cancelExecution();
    }

    /**
     * Starts the experiment.
     */
    public void startExperiment() {
        stepCounter = 0;
        strategy.solve(csp, this.startAssignment);
    }

    public void updateStateView(CSP<Variable, Integer> csp, Assignment<Variable, Integer> assignment, Variable var) {
        Platform.runLater(() -> updateStateViewLater(csp, assignment, var));
        taskPaneCtrl.waitAfterStep();
    }

    private void updateStateViewLater(CSP<Variable, Integer> csp, Assignment<Variable, Integer> assignment, Variable var) {
        stateViewCtrl.update(csp, assignment);

        String txt1 = "Step " + stepCounter + ": ";
        String txt2 = "Domain reduced";
        if (assignment != null) {
            if (var != null)
                txt2 = "Assignment changed, " + var + " = " + assignment.getValue(var);
            else
                txt2 = "Assignment changed, " + assignment.toString();
            if (assignment.isSolution(csp))
                txt2 += " (Solution)";
        } else {
            txt2 = "Domain reduced" + (var != null ? ", Dom(" + var + ") = " + csp.getDomain(var) + ")" : "");
        }
        taskPaneCtrl.setStatus(txt1 + txt2);
    }

}
