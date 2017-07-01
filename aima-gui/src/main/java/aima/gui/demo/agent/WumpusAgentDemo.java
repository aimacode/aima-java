package aima.gui.demo.agent;

import aima.core.agent.EnvironmentView;
import aima.core.agent.impl.SimpleEnvironmentView;
import aima.core.environment.wumpusworld.HybridWumpusAgent;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusEnvironment;
import aima.core.logic.propositional.inference.DPLLSatisfiable;

/**
 * Demonstrates, how a hybrid search- and logic-based agent tries to find gold in a Wumpus cave.
 *
 * @author Ruediger Lunde
 */
public class WumpusAgentDemo {
    public static void main(String[] args) {
        WumpusCave cave;
        cave = create2x2Cave();
        // cave = create3x3Cave();
        // cave = create4x4Cave();

        WumpusEnvironment env = new WumpusEnvironment(cave);
        EnvironmentView view = new SimpleEnvironmentView();
        env.addEnvironmentView(view);

        HybridWumpusAgent a;
        a = new HybridWumpusAgent
        // a = new EfficientHybridWumpusAgent
                (cave.getCaveXDimension(), cave.getCaveYDimension(), cave.getStart(),
                new DPLLSatisfiable(), env);

        env.notifyViews("The cave:\n" + cave.toString());
        env.addAgent(a);
        env.stepUntilDone();
        env.notifyViews("Metrics: " + a.getMetrics());
        env.notifyViews("KB:\n" + a.getKB());
    }

    private static WumpusCave create2x2Cave() {
        // Caution: 2x2 caves need a wumpus - otherwise KB becomes inconsistent in step 2...
        return new WumpusCave(2, 2, ""
                + "W . "
                + "S G ");
    }

    private static WumpusCave create3x3Cave() {
        return new WumpusCave(3, 3, ""
                + "P . G "
                + ". W . "
                + "S . P ");
    }

    /**
     * Artificial Intelligence A Modern Approach (3rd Edition): page 237.<br>
     * <br>
     * Figure 7.2 A typical wumpus world. The agent is in the bottom left corner, facing right.
     */
    private static WumpusCave create4x4Cave() {
        return new WumpusCave(4, 4, ""
                + ". . . P "
                + "W G P . "
                + ". . . . "
                + "S . P . ");
    }
}
