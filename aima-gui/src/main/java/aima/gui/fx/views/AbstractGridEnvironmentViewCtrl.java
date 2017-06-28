package aima.gui.fx.views;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.Percept;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Provides base functionality for implementing views for environments which are based on a grid world.
 * Square (1, 1) is bottom left.
 *
 * @author Ruediger Lunde
 */
public abstract class AbstractGridEnvironmentViewCtrl extends SimpleEnvironmentViewCtrl {

    private GridPane gridPane = new GridPane();
    private List<SquareButton> squareButtons = new ArrayList<>();
    protected Label actionLabel = new Label();
    protected boolean isEditingEnabled; // edit cave only after initialization, not during agent simulation!
    protected IntegerProperty xDimension = new SimpleIntegerProperty(-1);
    protected IntegerProperty yDimension = new SimpleIntegerProperty(-1);
    protected IntegerProperty squareSize = new SimpleIntegerProperty();

    protected Environment env;
    private ToIntFunction<Environment> xDimFn;
    private ToIntFunction<Environment> yDimFn;

    protected AbstractGridEnvironmentViewCtrl(StackPane viewRoot, ToIntFunction<Environment> xDimFn,
                                              ToIntFunction<Environment> yDimFn) {
        super(viewRoot);
        this.xDimFn = xDimFn;
        this.yDimFn = yDimFn;
        BorderPane envStateView;
        envStateView = new BorderPane();
        splitPane.getItems().add(0, envStateView);
        splitPane.setDividerPosition(0, 0.6);
        splitPane.setStyle("-fx-background-color: white");
        envStateView.setMinWidth(0.0);
        envStateView.setCenter(gridPane);
        //viewRoot.setAlignment(Pos.BOTTOM_CENTER);
        squareSize.bind(Bindings.min(envStateView.widthProperty().subtract(20).divide(xDimension),
                envStateView.heightProperty().subtract(20).subtract(actionLabel.heightProperty()).divide(yDimension)));
        gridPane.maxWidthProperty().bind(squareSize.multiply(xDimension));
        gridPane.maxHeightProperty().bind(squareSize.multiply(yDimension));
        gridPane.setVgap(20);
        gridPane.setHgap(20);

        actionLabel.setMaxWidth(Double.MAX_VALUE);
        actionLabel.setAlignment(Pos.CENTER);
        actionLabel.setFont(Font.font(20));
        envStateView.setBottom(actionLabel);
    }

    /**
     * Updates the grid structure for a new or changed environment if necessary.
     */
    private void updateDimensions() {
        int xDim = xDimFn.applyAsInt(env);
        int yDim = yDimFn.applyAsInt(env);
        if (xDim != xDimension.get() || yDim != yDimension.get()) {
            xDimension.set(xDim);
            yDimension.set(yDim);

            gridPane.getChildren().clear();
            gridPane.getRowConstraints().clear();
            gridPane.getColumnConstraints().clear();

            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / yDim);
            for (int row = 0; row < yDim; row++)
                gridPane.getRowConstraints().add(rc);

            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / xDim);
            for (int col = 0; col < xDim; col++)
                gridPane.getColumnConstraints().add(cc);

            squareButtons.clear();
            for (int y = 1; y <= yDim; y++) {
                for (int x = 1; x <= xDim; x++) {
                    final int x1 = x;
                    final int y1 = y;
                    SquareButton btn = new SquareButton();
                    btn.idLabel.setText(x1 + ", " + y1);
                    btn.setOnAction(ev -> onEdit(x1, y1));
                    squareButtons.add(btn);
                    gridPane.add(btn, x - 1, yDim - y);
                }
            }
        }
    }

    protected SquareButton getSquareButton(int x, int y) {
        return squareButtons.get((y - 1) * xDimension.get() + x - 1);
    }

    @Override
    public void initialize(Environment env) {
        super.initialize(env);
        isEditingEnabled = true;
    }

    @Override
    public void agentActed(Agent agent, Percept percept, Action action, Environment source) {
        super.agentActed(agent, percept, action, source);
        isEditingEnabled = false; // never change the environment when the agent has started his job.
        Platform.runLater(() -> actionLabel.setText("Percept: " + percept + ", Action: " + action));
    }

    /** Updates the view. */
    @Override
    protected final void updateEnvStateView(Environment env) {
        this.env =  env;
        updateDimensions();
        actionLabel.setText("");
        update();
    }

    /**
     * Updates the content shown on the square buttons.
     */
    abstract protected void update();

    /**
     * Is called whenever a square button is pressed. This implementation does nothing.
     *
     * @param x the x coordinate of the button (1 = left).
     * @param y the y coordinate of the button (1 = bottom).
     */
    protected void onEdit(int x, int y) {
    }


    /**
     * Represents a square in the grid. A button is used with its graphic set to a stack pane
     * with a pane and two labels.
     */
    protected static class SquareButton extends Button {
        private Pane pane;
        private Label label;
        private Label idLabel;

        private SquareButton() {
            StackPane sp = new StackPane();
            setGraphic(sp);
            pane = new StackPane();
            label = new Label();
            label.setFont(Font.font(40));
            idLabel = new Label();
            idLabel.setFont(Font.font(20));
            StackPane.setAlignment(idLabel, Pos.TOP_LEFT);
            sp.getChildren().addAll(idLabel, pane, label);
            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        }

        public Pane getPane() {
            return pane;
        }

        public Label getLabel() {
            return label;
        }

        public Label getIdLabel() {
            return idLabel;
        }
    }
}
