package aima.gui.fx.applications.agent;

import aima.core.environment.wumpusworld.*;
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

    private TaskExecutionPaneCtrl taskPaneCtrl;
    private WumpusEnvironmentViewCtrl envViewCtrl;

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
        Parameter p1 = new Parameter(PARAM_CAVE, "2x2", "3x3", "4x4");
        p1.setDefaultValueIndex(2);
        Parameter p2 = new Parameter(PARAM_AGENT, "Hybrid Wumpus Agent", "Efficient Wumpus Agent");
        p2.setDefaultValueIndex(1);
        return Arrays.asList(p1, p2);
    }

    /** Is called after each parameter selection change. */
    @Override
    public void initialize() {
        switch (taskPaneCtrl.getParamValueIndex(PARAM_CAVE)) {
            case 0:
                cave = new WumpusCave(2, 2, ""
                        + "W."
                        + "SG");
                break;
            case 1:
                cave = new WumpusCave(3, 3, ""
                        + "P.G"
                        + ".W."
                        + "S.P");
                break;
            case 2:
                // from Figure 7.2 A typical wumpus world.
                cave = new WumpusCave(4, 4, ""
                        + "...P"
                        + "WGP."
                        + "...."
                        + "S.P.");
                break;
        }
        env = new WumpusEnvironment(cave);
        switch (taskPaneCtrl.getParamValueIndex(PARAM_AGENT)) {
            case 0:
                agent = new HybridWumpusAgent(cave.getCaveXDimension(), cave.getStart(), env);
                break;
            case 1:
                agent = new EfficientHybridWumpusAgent(cave.getCaveXDimension(), cave.getStart(), env);
                break;
        }
        env.addEnvironmentView(envViewCtrl);
        envViewCtrl.initialize(env);
    }

    /** Starts the experiment. */
    public void startExperiment() {
        env.addAgent(agent);
        while (!env.isDone() && !Tasks.currIsCancelled()) {
            env.step();
            updateStatus();
            taskPaneCtrl.waitAfterStep();
        }
    }

    @Override
    public void cleanup() {
        taskPaneCtrl.cancelExecution();
    }

    private void updateStatus() {
        WumpusKnowledgeBase kb = agent.getKB();
        taskPaneCtrl.setStatus("{KB.size=" + kb.size() +
                ", KB.sym.size=" + kb.getSymbols().size() + ", KB.cnf.size=" + kb.asCNF().size() + "}");
    }
}
