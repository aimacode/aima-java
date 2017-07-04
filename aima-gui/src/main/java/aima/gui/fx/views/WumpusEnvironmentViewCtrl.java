package aima.gui.fx.views;


import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.environment.wumpusworld.AgentPosition;
import aima.core.environment.wumpusworld.Room;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusEnvironment;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.HashMap;
import java.util.Map;

/**
 * Environment view controller class which is specialized for wumpus environments.
 * It adds a wumpus cave state view to the text-based view of the
 * {@link SimpleEnvironmentViewCtrl}.
 *
 * @author Ruediger Lunde
 */
public class WumpusEnvironmentViewCtrl extends AbstractGridEnvironmentViewCtrl {

    private Map<Agent, Shape> agentSymbols = new HashMap<>();
    private boolean showRoomContent = true;

    public WumpusEnvironmentViewCtrl(StackPane viewRoot) {
        super(viewRoot,
                env -> ((WumpusEnvironment) env).getCave().getCaveXDimension(),
                env -> ((WumpusEnvironment) env).getCave().getCaveYDimension());
    }

    /** Can be called from every thread. */
    public void setShowRoomContent(boolean showRoomContent) {
        if (this.showRoomContent != showRoomContent) {
            this.showRoomContent = showRoomContent;
            if (env != null) {
                if (Platform.isFxApplicationThread())
                    update();
                else
                    Platform.runLater(this::update);
            }
        }
    }

    @Override
    public void initialize(Environment env) {
        agentSymbols.clear();
        super.initialize(env);
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
                if (showRoomContent) {
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
                }
                btn.getLabel().setText(txt);
                btn.getPane().getChildren().clear();
            }
        }

        // visualize agent positions
        for (Agent agent : wEnv.getAgents()) {
            AgentPosition pos = wEnv.getAgentPosition(agent);
            Pane pane = getSquareButton(pos.getX(), pos.getY()).getPane();
            pane.getChildren().add(getAgentSymbol(agent, pos));
        }
    }

    @Override
    protected void onEdit(int x, int y) {
        WumpusCave cave = ((WumpusEnvironment) env).getCave();
        Room room = new Room(x, y);
        if (!isEditingEnabled)
            perceptLabel.setText("Cave can only be edited after initialization.");
        else if (!room.equals(cave.getStart().getRoom()) && !room.equals(cave.getGold())) {
            if (cave.isPit(room)) {
                if (room.equals(cave.getWumpus()))
                    cave.setPit(room, false);
                else
                    cave.setWumpus(room);
            } else {
                cave.setPit(room, true);
            }
            update();
            perceptLabel.setText("");
        }
    }

    private Node getAgentSymbol(Agent agent, AgentPosition pos) {
        Shape result = agentSymbols.get(agent);
        if (result == null) {
            result = new Polygon(-1.0 / 3, 1, 1.0 / 3, 1, 0, 0);
            result.scaleXProperty().bind(squareSize.multiply(0.75));
            result.scaleYProperty().bind(squareSize.multiply(0.75));
            agentSymbols.put(agent, result);
        }

        result.setFill(agent.isAlive() ? Color.RED : Color.LIGHTGRAY);
        switch (pos.getOrientation()) {
            case FACING_NORTH:
                result.setRotate(0);
                break;
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
