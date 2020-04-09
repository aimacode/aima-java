package aima.gui.fx.applications.search.games;

import aima.gui.fx.framework.IntegrableApplication;
import javafx.beans.binding.Bindings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Starter of the JavaFX version of the Connect Four game with integrated
 * artificial intelligence player. It uses Minimax search with Alpha-Beta
 * pruning and an iterative deepening strategy to meet the specified time
 * constraints.
 * 
 * @author Ruediger Lunde
 */
public class ConnectFourApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	private ConnectFourModel model;

	// create attributes for all parts of the application which need to be
	// accessed by listeners, inner classes, and helper methods

	private ComboBox<String> strategyCombo;
	private ComboBox<String> timeCombo;
	private Label statusBar;

	private Button[] colBtns;
	private Circle[] disks;

	@Override
	public String getTitle() {
		return "Connect Four App";
	}

	@Override
	public Pane createRootPane() {
		model = new ConnectFourModel();
		BorderPane root = new BorderPane();

		ToolBar toolBar = new ToolBar();
		toolBar.setStyle("-fx-background-color: rgb(0, 0, 200)");
		Button clearBtn = new Button("Clear");
		clearBtn.setOnAction(ev -> model.initGame());

		strategyCombo = new ComboBox<>();
		strategyCombo.getItems().addAll("Iterative Deepening Alpha-Beta", "Advanced Alpha-Beta");
		strategyCombo.getSelectionModel().select(0);
		timeCombo = new ComboBox<>();
		timeCombo.getItems().addAll("2sec", "4sec", "6sec", "8sec");
		timeCombo.getSelectionModel().select(1);

		Button proposeBtn = new Button("Propose Move");
		proposeBtn.setOnAction(ev -> model.proposeMove((timeCombo.getSelectionModel().getSelectedIndex() + 1) * 2,
				strategyCombo.getSelectionModel().getSelectedIndex()));
		toolBar.getItems().addAll(clearBtn, new Separator(), strategyCombo, timeCombo, proposeBtn);
		root.setTop(toolBar);

		final int rows = model.getRows();
		final int cols = model.getCols();

		BorderPane boardPane = new BorderPane();
		ColumnConstraints colCons = new ColumnConstraints();
		colCons.setPercentWidth(100.0 / cols);

		GridPane btnPane = new GridPane();
		GridPane diskPane = new GridPane();
		btnPane.setHgap(10);
		btnPane.setPadding(new Insets(10, 10, 0, 10));
		btnPane.setStyle("-fx-background-color: rgb(0, 50, 255)");
		diskPane.setPadding(new Insets(10, 10, 10, 10));
		diskPane.setStyle("-fx-background-color: rgb(0, 50, 255)");

		colBtns = new Button[cols];
		for (int i = 0; i < cols; i++) {
			Button colBtn = new Button("" + (i + 1));
			colBtn.setId("" + (i + 1));
			colBtn.setMaxWidth(Double.MAX_VALUE);
			colBtn.setOnAction(ev -> {
				String id = ((Button) ev.getSource()).getId();
				int col = Integer.parseInt(id);
				model.makeMove(col - 1);
			});
			colBtns[i] = colBtn;
			btnPane.add(colBtn, i, 0);
			GridPane.setHalignment(colBtn, HPos.CENTER);
			btnPane.getColumnConstraints().add(colCons);
			diskPane.getColumnConstraints().add(colCons);
		}
		boardPane.setTop(btnPane);

		diskPane.setMinSize(0, 0);
		diskPane.setPrefSize(cols * 100, rows * 100);

		RowConstraints rowCons = new RowConstraints();
		rowCons.setPercentHeight(100.0 / rows);
		for (int i = 0; i < rows; i++)
			diskPane.getRowConstraints().add(rowCons);

		disks = new Circle[rows * cols];
		for (int i = 0; i < rows * cols; i++) {
			Circle disk = new Circle(30);
			disk.radiusProperty().bind(Bindings.min(Bindings.divide(diskPane.widthProperty(), cols * 3),
					Bindings.divide(diskPane.heightProperty(), rows * 3)));
			disks[i] = disk;
			diskPane.add(disk, i % cols, i / cols);
			GridPane.setHalignment(disk, HPos.CENTER);
		}
		boardPane.setCenter(diskPane);
		root.setCenter(boardPane);
		statusBar = new Label();
		statusBar.setMaxWidth(Double.MAX_VALUE);
		statusBar.setStyle("-fx-background-color: rgb(0, 0, 200); -fx-font-size: 20");
		root.setBottom(statusBar);
		model.addPropertyChangeListener(event -> update());
		return root;
	}

	@Override
	public void initialize() {
		update();

	}

	@Override
	public void cleanup() {
		// Nothing to do here...
	}

	/** Updates the view after the model state has changed. */
	private void update() {
		final int cols = model.getCols();
		for (int i = 0; i < disks.length; i++) {
			String player = model.getPlayerAt(i / cols, i % cols);
			Color color = Color.DARKBLUE;
			if (player != null)
				color = player.equals("red") ? Color.RED : Color.YELLOW;
			else {
				for (String p : model.getPlayers())
					if (model.isWinPositionFor(i / cols, i % cols, p))
						color = Color.BLACK;
			}
			disks[i].setFill(color);
		}

		String statusText;
		if (!model.isGameOver()) {
			String toMove = model.getPlayerForNextMove();
			statusText = "Next move: " + toMove;
			statusBar.setTextFill(toMove.equals("red") ? Color.RED : Color.YELLOW);
		} else {
			String winner = model.getWinner();
			if (winner != null)
				statusText = "Color " + winner + " has won. Congratulations!";
			else
				statusText = "No winner :-(";
			statusBar.setTextFill(Color.WHITE);
		}
		if (model.searchMetrics != null)
			statusText += "    " + model.searchMetrics;
		statusBar.setText(statusText);
	}

}
