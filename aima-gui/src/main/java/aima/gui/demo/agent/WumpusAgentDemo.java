package aima.gui.demo.agent;

import aima.core.agent.EnvironmentView;
import aima.core.agent.impl.SimpleEnvironmentView;
import aima.core.environment.wumpusworld.HybridWumpusAgent;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusEnvironment;

/**
 * Demonstrates, how hybrid logic-based agent tries to grab gold in a Wumpus cave.
 *
 * @author Ruediger Lunde
 */
public class WumpusAgentDemo {
    public static void main(String[] args) {
        WumpusCave cave;
        cave = create2x2Cave();
        // cave = create3x3Cave();
        // cave = createTypicalCave();

        WumpusEnvironment env = new WumpusEnvironment(cave);
        EnvironmentView view = new SimpleEnvironmentView();
        env.addEnvironmentView(view);

        HybridWumpusAgent a = null;
        a = new HybridWumpusAgent(cave.getCaveXDimension(), cave.getStart(), env);

        env.notifyViews("The cave:\n" + cave.toString());
        env.addAgent(a);
        env.stepUntilDone();
        env.notifyViews("KB (KB.size=" + a.getKB().size() + "):\n" + a.getKB().toString());
    }

    private static WumpusCave create2x2Cave() {
        // Caution: 2x2 caves need a wumpus - otherwise KB becomes inconsistent in step 2...
        return new WumpusCave(2, 2, ""
                + "W."
                + "SG");
    }

    private static WumpusCave create3x3Cave() {
        return new WumpusCave(3, 3, ""
                + "P.G"
                + ".W."
                + "S.P");
    }

    /**
     * Artificial Intelligence A Modern Approach (3rd Edition): page 237.<br>
     * <br>
     * Figure 7.2 A typical wumpus world. The agent is in the bottom left corner, facing right.
     */
    private static WumpusCave createTypicalCave() {
        return new WumpusCave(4, 4, ""
                + "...P"
                + "WGP."
                + "...."
                + "S.P.");
    }
}
