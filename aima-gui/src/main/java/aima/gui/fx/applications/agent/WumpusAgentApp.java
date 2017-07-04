package aima.gui.fx.applications.agent;

import aima.core.environment.wumpusworld.*;
import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.inference.OptimizedDPLL;
import aima.core.util.Tasks;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
import aima.gui.fx.views.WumpusEnvironmentViewCtrl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.Arrays;
import java.util.List;

/**
 * Integrable application which demonstrates how the Hybrid Wumpus Agent
 * solves the problem of finding gold in a Wumpus cave.
 *
 * @author Ruediger Lunde
 *
 */
public class WumpusAgentApp extends IntegrableApplication {

    public static void main(String[] args) {
        launch(args);
    }

    protected static String PARAM_CAVE = "cave";
    protected static String PARAM_AGENT = "agent";
    protected static String PARAM_SAT_SOLVER = "satSolver";
    protected static String PARAM_VIEW = "view";

    protected TaskExecutionPaneCtrl taskPaneCtrl;
    protected WumpusEnvironmentViewCtrl envViewCtrl;

    protected WumpusEnvironment env = null;
    protected HybridWumpusAgent agent = null;
    /** The selected Wumpus cave */
    protected WumpusCave cave;

    @Override
    public String getTitle() {
        return "Wumpus Agent App";
    }

    /**
     * Defines state view, parameters, and call-back functions and calls the
     * simulation pane builder to create layout and controller objects.
     */
    @Override
    public Pane createRootPane() {
        BorderPane root = new BorderPane();

        StackPane envView = new StackPane();
        envViewCtrl = new WumpusEnvironmentViewCtrl(envView);

        List<Parameter> params = createParameters();

        TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
        builder.defineParameters(params);
        builder.defineStateView(envView);
        builder.defineInitMethod(this::initialize);
        builder.defineTaskMethod(this::startExperiment);
        taskPaneCtrl = builder.getResultFor(root);

        return root;
    }

    protected List<Parameter> createParameters() {
        Parameter p1 = new Parameter(PARAM_CAVE, "2x2", "3x3", "4x4a", "4x4b");
        p1.setDefaultValueIndex(2);
        Parameter p2 = new Parameter(PARAM_AGENT, "Hybrid Wumpus Agent", "Efficient Hybrid Wumpus Agent");
        p2.setDefaultValueIndex(1);
        Parameter p3 = new Parameter(PARAM_SAT_SOLVER, "DPLLSatisfiable", "OptimizedDPLL");
        p3.setDefaultValueIndex(1);
        Parameter p4 = new Parameter(PARAM_VIEW, "Default", "Hide Room Content", "Show KB");
        return Arrays.asList(p1, p2, p3, p4);
    }

    /** Is called after each parameter selection change. */
    @Override
    public void initialize() {
        switch (taskPaneCtrl.getParamValueIndex(PARAM_CAVE)) {
            case 0:
                cave = new WumpusCave(2, 2, ""
                        + "W . "
                        + "S G ");
                break;
            case 1:
                cave = new WumpusCave(3, 3, ""
                        + "P . G "
                        + ". W . "
                        + "S . P ");
                break;
            case 2:
                // from Figure 7.2 A typical wumpus world.
                cave = new WumpusCave(4, 4, ""
                        + ". . . P "
                        + "W G P . "
                        + ". . . . "
                        + "S . P . ");
                break;
            case 3:
                cave = new WumpusCave(4, 4, ""
                        + ". . W G "
                        + ". . . P "
                        + ". . . . "
                        + "S . . . ");
                break;
        }
        env = new WumpusEnvironment(cave);

        DPLL dpll = null;
        switch (taskPaneCtrl.getParamValueIndex(PARAM_SAT_SOLVER)) {
            case 0:
                dpll = new DPLLSatisfiable();
                break;
            case 1:
                dpll = new OptimizedDPLL();
                break;
        }

        switch (taskPaneCtrl.getParamValueIndex(PARAM_AGENT)) {
            case 0:
                agent = new HybridWumpusAgent(cave.getCaveXDimension(), cave.getCaveYDimension(),
                        cave.getStart(), dpll, env);
                break;
            case 1:
                agent = new EfficientHybridWumpusAgent(cave.getCaveXDimension(), cave.getCaveYDimension(),
                        cave.getStart(), dpll, env);
                break;
        }
        env.addEnvironmentView(envViewCtrl);
        envViewCtrl.initialize(env);
        envViewCtrl.setShowRoomContent(taskPaneCtrl.getParamValueIndex(PARAM_VIEW) != 1);
    }

    /** Starts the experiment. */
    public void startExperiment() {
        env.addAgent(agent);
        taskPaneCtrl.waitAfterStep();
        while (!env.isDone() && !Tasks.currIsCancelled()) {
            env.step();
            taskPaneCtrl.setStatus(agent.getMetrics().toString());
            taskPaneCtrl.waitAfterStep();
        }
        if (taskPaneCtrl.getParamValueIndex(PARAM_VIEW) == 1)
            envViewCtrl.setShowRoomContent(true);
        if (taskPaneCtrl.getParamValueIndex(PARAM_VIEW) == 2)
            env.notifyViews("KB:\n" + agent.getKB().toString());
    }

    @Override
    public void cleanup() {
        taskPaneCtrl.cancelExecution();
    }
}
