package aima.gui.fx.views;


import aima.core.agent.Agent;
import aima.core.environment.wumpusworld.AgentPosition;
import aima.core.environment.wumpusworld.Room;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusEnvironment;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.List;

/**
 * Environment view controller class which is specialized for wumpus environments.
 * It adds a wumpus cave state view to the text-based view of the
 * {@link SimpleEnvironmentViewCtrl}.
 *
 * @author Ruediger Lunde
 */
public class WumpusEnvironmentViewCtrl extends AbstractGridEnvironmentViewCtrl {

    public WumpusEnvironmentViewCtrl(StackPane viewRoot) {
        super(viewRoot,
                env -> ((WumpusEnvironment) env).getCave().getCaveXDimension(),
                env -> ((WumpusEnvironment) env).getCave().getCaveYDimension());
    }

    protected void update() {
        WumpusEnvironment wEnv = (WumpusEnvironment) env;
        WumpusCave cave = wEnv.getCave();

        // visualize cave information
        for (int y = 1; y <= yDimension.get(); y++) {
            for (int x = 1; x <= xDimension.get(); x++) {
                Room room = new Room(x, y);
                SquareButton btn = getSquareButton(x, y);
                btn.getLabel().setTextFill(Color.BLACK);
                String txt = "";
                if (room.equals(cave.getStart().getRoom())) {
                    txt = "S";
                } else if (room.equals(cave.getWumpus())) {
                    if (!wEnv.isWumpusAlive())
                        btn.getLabel().setTextFill(Color.GRAY);
                    txt = "W";
                } else if (room.equals(cave.getGold())) {
                    if (wEnv.isGoalGrabbed())
                        btn.getLabel().setTextFill(Color.GRAY);
                    txt = "Gold";
                }
                if (cave.isPit(room))
                    txt = txt.isEmpty() ? "Pit" : "Pit " + txt;
                btn.getLabel().setText(txt);
                btn.getPane().getChildren().clear();
            }
        }

        // visualize agent positions
        for (Agent agent : wEnv.getAgents()) {
            AgentPosition pos = wEnv.getAgentPosition(agent);
            Pane pane = getSquareButton(pos.getX(), pos.getY()).getPane();
            pane.getChildren().add(createAgentSymbol(pane, agent.isAlive() ? Color.RED : Color.LIGHTGRAY,
                    pos.getOrientation()));
        }
    }

    @Override
    protected void onEdit(int x, int y) {
        WumpusCave cave = ((WumpusEnvironment) env).getCave();
        Room room = new Room(x, y);
        if (!isEditingEnabled)
            actionLabel.setText("Cave can only be edited after initialization.");
        else if (!room.equals(cave.getStart().getRoom()) && !room.equals(cave.getGold())) {
            if (cave.isPit(room)) {
                if (cave.getWumpus().equals(room))
                    cave.setPit(room, false);
                else
                    cave.setWumpus(room);
            } else {
                cave.setPit(room, true);
            }
            update();
        }
    }

    private Node createAgentSymbol(Pane pane, Color color, AgentPosition.Orientation orientation) {
        int size = squareSize.get() / 2;
        Polygon result = new Polygon(-size / 3, size, size / 3, size, 0, 0);
        result.setFill(color);
        switch (orientation) {
            case FACING_EAST:
                result.setRotate(90);
                break;
            case FACING_SOUTH:
                result.setRotate(180);
                break;
            case FACING_WEST:
                result.setRotate(270);
                break;
        }
        return result;
    }
}
