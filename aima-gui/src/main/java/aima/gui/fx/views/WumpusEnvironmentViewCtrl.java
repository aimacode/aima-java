package aima.gui.fx.views;


import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.Percept;
import aima.core.environment.wumpusworld.AgentPosition;
import aima.core.environment.wumpusworld.Room;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusEnvironment;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * Environment view controller class which is specialized for wumpus environments.
 * It adds a wumpus cave state view to the text-based view of the
 * {@link SimpleEnvironmentViewCtrl}.
 *
 * @author Ruediger Lunde
 */
public class WumpusEnvironmentViewCtrl extends SimpleEnvironmentViewCtrl {

    private GridPane gridPane = new GridPane();
    private Label actionLabel = new Label();
    private List<RoomButton> roomButtons = new ArrayList<>();
    private boolean isEditingEnabled; // edit cave only after initialization, not during agent simulation!

    protected WumpusEnvironment env;

    public WumpusEnvironmentViewCtrl(StackPane viewRoot) {
        super(viewRoot);
        BorderPane envStateView;
        envStateView = new BorderPane();
        splitPane.getItems().add(0, envStateView);
        splitPane.setDividerPosition(0, 0.6);
        splitPane.setStyle("-fx-background-color: white");
        envStateView.setMinWidth(0.0);
        envStateView.setCenter(gridPane);
        viewRoot.setAlignment(Pos.BOTTOM_CENTER);
        gridPane.maxWidthProperty().bind(Bindings.min(envStateView.widthProperty(), envStateView.heightProperty()).
                subtract(40));
        gridPane.maxHeightProperty().bind(Bindings.min(envStateView.widthProperty(), envStateView.heightProperty()).
                subtract(40));
        gridPane.setVgap(20);
        gridPane.setHgap(20);

        actionLabel.setMaxWidth(Double.MAX_VALUE);
        actionLabel.setAlignment(Pos.CENTER);
        actionLabel.setFont(Font.font(20));
        envStateView.setBottom(actionLabel);
    }

    @Override
    public void initialize(Environment env) {
        super.initialize(env);
        isEditingEnabled = true;
    }

    @Override
    public void agentActed(Agent agent, Percept percept, Action action, Environment source) {
        super.agentActed(agent, percept, action, source);
        isEditingEnabled = false; // never change the cave when the agent has started his job.
        Platform.runLater(() -> actionLabel.setText("Percept: " + percept + ", Action: " + action));
    }

    /** Updates the view. */
    @Override
    public void updateEnvStateView(Environment env) {
        this.env = (WumpusEnvironment) env;
        update();
    }

    private void update() {
        WumpusCave cave = this.env.getCave();
        int rows = cave.getCaveYDimension();
        int cols = cave.getCaveXDimension();

        actionLabel.setText("");

        // update grid if necessary
        if (cols != gridPane.getColumnConstraints().size() || rows != gridPane.getRowConstraints().size()) {
            gridPane.getChildren().clear();
            gridPane.getRowConstraints().clear();
            gridPane.getColumnConstraints().clear();

            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / rows);
            for (int row = 0; row < rows; row++)
                gridPane.getRowConstraints().add(rc);

            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / cols);
            for (int col = 0; col < rows; col++)
                gridPane.getColumnConstraints().add(cc);

            roomButtons.clear();
            for (int row = 1; row <= rows; row++) {
                for (int col = 1; col <= cols; col++) {
                    final int c = col;
                    final int r = row;
                    RoomButton btn = new RoomButton();
                    btn.setOnAction(ev -> toggleRoomContent(c, r));
                    roomButtons.add(btn);
                    gridPane.add(btn, col-1, rows - row);
                }
            }
        }

        // visualize cave information
        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= cols; col++) {
                Room room = new Room(col, row);
                RoomButton btn = getRoomButton(col, row);
                btn.getLabel().setTextFill(Color.BLACK);
                String txt = "";
                if (room.equals(cave.getStart().getRoom())) {
                    txt = "S";
                } else if (room.equals(cave.getWumpus())) {
                    if (!this.env.isWumpusAlive())
                        btn.getLabel().setTextFill(Color.GRAY);
                    txt = "W";
                } else if (room.equals(cave.getGold())) {
                    if (this.env.isGoalGrabbed())
                        btn.getLabel().setTextFill(Color.GRAY);
                    txt = "Gold";
                }
                if (cave.isPit(room))
                   txt = txt.isEmpty() ? "Pit" : "Pit " + txt;
                btn.getLabel().setText(txt);
                btn.clearAgentMarker();
            }
        }

        // visualize agent position
        List<Agent> agents = env.getAgents();
        if (!agents.isEmpty()) {
            AgentPosition pos = this.env.getAgentPosition(agents.get(0));
            getRoomButton(pos.getX(), pos.getY()).setAgentMarker
                    (agents.get(0).isAlive() ? Color.RED : Color.LIGHTGRAY, pos.getOrientation());
        }
    }

    private RoomButton getRoomButton(int col, int row) {
        return roomButtons.get((row -1) * env.getCave().getCaveXDimension() + col - 1);
    }

    private void toggleRoomContent(int col, int row) {
        WumpusCave cave = env.getCave();
        Room room = new Room(col, row);
        if (isEditingEnabled && !room.equals(cave.getStart().getRoom()) && !room.equals(cave.getGold())) {
            if (cave.isPit(room)) {
                if (cave.getWumpus().equals(room)) {
                    cave.setPit(room, false);
                } else {
                    cave.setWumpus(room);
                }
            } else {
                cave.setPit(room, true);
            }
            update();
        } else {
            actionLabel.setText("Cave can only be edited after initialization.");
        }

    }

    private static class RoomButton extends Button {
        Label label;
        Pane markerPane;

        RoomButton() {
            StackPane sp = new StackPane();
            setGraphic(sp);
            markerPane = new StackPane();
            label = new Label();
            label.setFont(Font.font(50));
            sp.getChildren().addAll(markerPane, label);
            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }

        Label getLabel() {
            return label;
        }

        void setAgentMarker(Color color, AgentPosition.Orientation orientation) {
            Polygon marker = new Polygon(-30, 50, 30, 50, 0, -50);
            marker.setFill(color);
            switch (orientation) {
                case FACING_EAST: marker.setRotate(90); break;
                case FACING_SOUTH: marker.setRotate(180); break;
                case FACING_WEST: marker.setRotate(270); break;
            }
            markerPane.getChildren().add(marker);
        }

        void clearAgentMarker() {
            markerPane.getChildren().clear();
        }
    }
}
