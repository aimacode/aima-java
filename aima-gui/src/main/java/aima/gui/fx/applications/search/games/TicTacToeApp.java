package aima.gui.fx.applications.search.games;

import aima.core.environment.tictactoe.TicTacToeGame;
import aima.core.environment.tictactoe.TicTacToeState;
import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.AlphaBetaSearch;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.adversarial.MinimaxSearch;
import aima.core.search.framework.Metrics;
import aima.core.util.datastructure.XYLocation;
import aima.gui.fx.framework.IntegrableApplication;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * Simple graphical Tic-tac-toe game application. It demonstrates the Minimax
 * algorithm for move selection as well as alpha-beta pruning.
 *
 * @author Ruediger Lunde
 */
public class TicTacToeApp extends IntegrableApplication {

    public static void main(String[] args) {
        launch(args);
    }

    private ComboBox<String> strategyCombo;
    private Button proposeBtn;
    private Label statusLabel;

    private Button[] squareBtns = new Button[9];
    private TicTacToeGame game;
    private TicTacToeState currState;
    private Metrics searchMetrics;


    @Override
    public String getTitle() {
        return "Tic-tac-toe App";
    }

    /** Simple pane to control the game. */
    @Override
    public Pane createRootPane() {
        BorderPane root = new BorderPane();

        ToolBar toolBar = new ToolBar();
        Button clearBtn = new Button("Clear");
        clearBtn.setOnAction(ev -> initialize());

        strategyCombo = new ComboBox<>();
        strategyCombo.getItems().addAll("Minimax",
                "Alpha-Beta", "Iterative Deepening Alpha-Beta",
                "Iterative Deepening Alpha-Beta (log)");
        strategyCombo.getSelectionModel().select(0);

        proposeBtn = new Button("Propose Move");
        proposeBtn.setOnAction(ev -> proposeMove());
        toolBar.getItems().addAll(clearBtn, new Separator(), strategyCombo, proposeBtn);
        root.setTop(toolBar);

        StackPane stateViewPane = new StackPane();
        GridPane gridPane = new GridPane();
        gridPane.maxWidthProperty().bind(Bindings.min(stateViewPane.widthProperty(), stateViewPane.heightProperty())
                .subtract(20));
        gridPane.maxHeightProperty().bind(Bindings.min(stateViewPane.widthProperty(), stateViewPane.heightProperty())
                .subtract(20));
        RowConstraints c1 = new RowConstraints();
        c1.setPercentHeight(100.0 / 3);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(100.0 / 3);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        for (int i = 0; i < 3; i++) {
            gridPane.getRowConstraints().add(c1);
            gridPane.getColumnConstraints().add(c2);
        }
        for (int i = 0; i < 9; i++) {
            Button btn = new Button();
            btn.setOnAction(this::handleSquareButtonEvent);
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btn.setMinSize(10, 10);
            btn.widthProperty().addListener((obs, o, n) -> btn.setFont(Font.font(n.intValue() / 2.5)));
            squareBtns[i] = btn;
            gridPane.add(btn, i % 3, i / 3);
        }
        stateViewPane.getChildren().add(gridPane);
        root.setCenter(stateViewPane);

        statusLabel = new Label();
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setMaxWidth(Double.MAX_VALUE);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setFont(Font.font(16));
        root.setBottom(statusLabel);
        return root;
    }

    @Override
    public void initialize() {
        game = new TicTacToeGame();
        currState = game.getInitialState();
        searchMetrics = null;
        update();
    }

    @Override
    public void cleanup() {
        // nothing to do here...
    }

    /** Updates square texts according to game state and also the status bar. */
    private void update() {
        for (int i = 0; i < 9; i++) {
            String val = currState.getValue(i % 3, i / 3);
            if (val == TicTacToeState.EMPTY)
                val = "";
            squareBtns[i].setText(val);
        }
        proposeBtn.setDisable(game.isTerminal(currState));

        // update status...
        String statusText;
        if (game.isTerminal(currState))
            if (game.getUtility(currState, TicTacToeState.X) == 1)
                statusText = "X has won :-)";
            else if (game.getUtility(currState, TicTacToeState.O) == 1)
                statusText = "O has won :-)";
            else
                statusText = "No winner...";
        else
            statusText = "Next move: " + game.getPlayer(currState);
        if (searchMetrics != null)
            statusText += "    " + searchMetrics;
        statusLabel.setText(statusText);
    }

    /** Uses adversarial search for selecting the next action. */
    private void proposeMove() {
        AdversarialSearch<TicTacToeState, XYLocation> search;
        XYLocation action;
        switch (strategyCombo.getSelectionModel().getSelectedIndex()) {
            case 0:
                search = MinimaxSearch.createFor(game);
                break;
            case 1:
                search = AlphaBetaSearch.createFor(game);
                break;
            case 2:
                search = IterativeDeepeningAlphaBetaSearch.createFor(game, 0.0,
                        1.0, 1000);
                break;
            default:
                search = IterativeDeepeningAlphaBetaSearch.createFor(game, 0.0,
                        1.0, 1000);
                ((IterativeDeepeningAlphaBetaSearch<?, ?, ?>) search)
                        .setLogEnabled(true);
        }
        action = search.makeDecision(currState);
        searchMetrics = search.getMetrics();
        currState = game.getResult(currState, action);
        update();
    }

    /** Handles user moves. */
    private void handleSquareButtonEvent(ActionEvent ae) {
        for (int i = 0; i < 9; i++)
            if (ae.getSource() == squareBtns[i])
                currState = game.getResult(currState,
                        new XYLocation(i % 3, i / 3));
        searchMetrics = null;
        update();
    }
}
