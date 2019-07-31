package aima.core.environment.wumpusworld;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 237.<br>
 * <br>
 * The agent can move Forward, TurnLeft by 90 degree, or TurnRight by 90 degree. The
 * agent dies a miserable death if it enters a square containing a pit or a live wumpus.
 * If an agent tries to move
 * forward and bumps into a wall, then the agent does not move. The action Grab can be
 * used to pick up the gold if it is in the same square as the agent. The action Shoot can
 * be used to fire an arrow in a straight line in the direction the agent is facing. The arrow
 * continues until it either hits (and hence kills) the wumpus or hits a wall. The agent has
 * only one arrow, so only the first Shoot action has any effect. Finally, the action Climb
 * can be used to climb out of the cave, but only from square [1,1].
 *
 * @author Ruediger Lunde
 */
public enum WumpusAction {

    FORWARD("Forward"), TURN_LEFT("TurnLeft"), TURN_RIGHT("TurnRight"), GRAB("Grab"), SHOOT("Shoot"), CLIMB("Climb");

    public String getSymbol() {
        return symbol;
    }

    private String symbol;

    WumpusAction(String sym) {
        symbol = sym;
    }
}
