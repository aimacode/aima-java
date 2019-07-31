package aima.gui.fx.views;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
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
 * Square (1, 1) is bottom left. Environments are expected to be derived from {@link AbstractEnvironment}.
 *
 * @param <P> Typ used for percepts
 * @param <A> Typ used for actions
 * @author Ruediger Lunde
 */
public abstract class AbstractGridEnvironmentViewCtrl<P, A> extends SimpleEnvironmentViewCtrl<P, A> {

    protected Label perceptLabel = new Label();
    protected double fontSizeFactor = 0.3;
    protected boolean isEditingEnabled; // edit cave only after initialization, not during agent simulation!
    protected IntegerProperty xDimension = new SimpleIntegerProperty(-1);
    protected IntegerProperty yDimension = new SimpleIntegerProperty(-1);
    protected IntegerProperty squareSize = new SimpleIntegerProperty();

    protected AbstractEnvironment<? extends P, ? extends A> env;
    private ToIntFunction<Environment> xDimFn;
    private ToIntFunction<Environment> yDimFn;
    private GridPane gridPane = new GridPane();
    private List<SquareButton> squareButtons = new ArrayList<>();

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
        squareSize.bind(Bindings.min(envStateView.widthProperty().subtract(20).divide(xDimension),
                envStateView.heightProperty().subtract(20).subtract(perceptLabel.heightProperty()).divide(yDimension)));
        squareSize.addListener((obs, o, n) -> updateFontSize());
        gridPane.maxWidthProperty().bind(squareSize.multiply(xDimension));
        gridPane.maxHeightProperty().bind(squareSize.multiply(yDimension));
        gridPane.vgapProperty().bind(squareSize.divide(20));
        gridPane.hgapProperty().bind(squareSize.divide(20));

        perceptLabel.setMaxWidth(Double.MAX_VALUE);
        perceptLabel.setAlignment(Pos.CENTER);
        perceptLabel.setFont(Font.font(20));
        envStateView.setBottom(perceptLabel);
    }

    /** (1, 1) is bottom left. */
    protected SquareButton getSquareButton(int x, int y) {
        return squareButtons.get((yDimension.get() - y) * xDimension.get() + x - 1);
    }

    @Override
    public void initialize(AbstractEnvironment<? extends P, ? extends A> env) {
        this.env = env;
        updateDimensions();
        super.initialize(env);
        isEditingEnabled = true;
    }

    /**
     * Updates the grid structure during initialization.
     */
    private void updateDimensions() {
        int xDim = xDimFn.applyAsInt(env);
        int yDim = yDimFn.applyAsInt(env);
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
        for (int y = yDim; y >= 1; y--) {
            for (int x = 1; x <= xDim; x++) {
                final int x1 = x;
                final int y1 = y;
                SquareButton btn = new SquareButton();
                btn.getIdLabel().setText(x1 + ", " + y1);
                btn.setOnAction(ev -> onEdit(x1, y1));
                squareButtons.add(btn);
                gridPane.add(btn, x - 1, yDim - y);
            }
        }
        updateFontSize();
    }

    private void updateFontSize() {
        for (SquareButton btn : squareButtons) {
            btn.getLabel().setFont(Font.font(squareSize.get() * fontSizeFactor));
            btn.getIdLabel().setVisible(squareSize.get() >= 80);
        }
    }

    /**
     * Can be called from every thread.
     */
    @Override
    public void agentAdded(Agent<?, ?> agent, Environment<?, ?> source) {
        super.agentAdded(agent, source);
        updatePerceptLabel(agent);
    }

    /** Can be called from every thread. */
    @Override
    public void agentActed(Agent<?, ?> agent, P percept, A action, Environment<?, ?> source) {
        super.agentActed(agent, percept, action, source);
        isEditingEnabled = false; // never change the environment when the agent has started his job.
        updatePerceptLabel(agent);
    }

    /** Updates the view. */
    @Override
    protected final void updateEnvStateView(Environment<?, ?> env) {
        if (this.env != env)
            throw new IllegalStateException("Environment can only be changed during initialization.");
        updatePerceptLabel(null);
        update();
    }

    /** Updates the percept label with the current percept of the agent. Can be called from every thread. */
    private void updatePerceptLabel(Agent<?, ?> agent) {
        String agentInfo = null;
        P percept = null;
        if (agent != null) {
            agentInfo = env.getAgents().size() > 1 ? " (A" + (env.getAgents().indexOf(agent) + 1) + ")" : "";
            percept = env.getPerceptSeenBy(agent);
        }
        String txt = (agent != null) ? "Percept" + agentInfo + ": " + percept : "";
        if (Platform.isFxApplicationThread())
            perceptLabel.setText(txt);
        else
            Platform.runLater(() -> perceptLabel.setText(txt));
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
    protected void onEdit(int x, int y) {}


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
            idLabel = new Label();
            StackPane.setAlignment(idLabel, Pos.TOP_LEFT);
            sp.getChildren().addAll(idLabel, pane, label);
            setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            setMinSize(10, 10);
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
