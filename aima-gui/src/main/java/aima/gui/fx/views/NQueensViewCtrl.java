package aima.gui.fx.views;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.util.datastructure.XYLocation;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

/**
 * Controller class which provides functionality for using a stack pane as a
 * state view for the N-Queens problem.
 * 
 * @author Ruediger Lunde
 */
public class NQueensViewCtrl {

	private GridPane gridPane = new GridPane();
	private Polygon[] queens = new Polygon[0];

	/**
	 * Adds a grid pane to the provided pane and creates a controller class
	 * instance which is responsible for queen symbol positioning on the grid.
	 */
	public NQueensViewCtrl(StackPane viewRoot) {
		viewRoot.getChildren().add(gridPane);
		viewRoot.setAlignment(Pos.BOTTOM_CENTER);
		gridPane.maxWidthProperty().bind(Bindings.min(viewRoot.widthProperty(), viewRoot.heightProperty()).subtract(20));
		gridPane.maxHeightProperty().bind(Bindings.min(viewRoot.widthProperty(), viewRoot.heightProperty()).subtract(10));
	}

	/** Updates the view. */
	public void update(NQueensBoard board) {
		int size = board.getSize();
		if (queens.length != size * size) {
			gridPane.getChildren().clear();
			gridPane.getColumnConstraints().clear();
			gridPane.getRowConstraints().clear();

			queens = new Polygon[size * size];
			RowConstraints c1 = new RowConstraints();
			c1.setPercentHeight(100.0 / size);
			ColumnConstraints c2 = new ColumnConstraints();
			c2.setPercentWidth(100.0 / size);
			for (int i = 0; i < board.getSize(); i++) {
				gridPane.getRowConstraints().add(c1);
				gridPane.getColumnConstraints().add(c2);
			}
			for (int i = 0; i < queens.length; i++) {
				StackPane field = new StackPane();
				queens[i] = createQueen();
				field.getChildren().add(queens[i]);
				int col = i % size;
				int row = i / size;
				field.setBackground(new Background(
						new BackgroundFill((col % 2 == row % 2) ? Color.WHITE : Color.LIGHTGRAY, null, null)));
				gridPane.add(field, col, row);
			}
		}
		double scale = 0.2 * gridPane.getWidth() / gridPane.getColumnConstraints().size();
		for (int i = 0; i < queens.length; i++) {
			Polygon queen = queens[i];
			queen.setScaleX(scale);
			queen.setScaleY(scale);
			XYLocation loc = new XYLocation(i % size, i / size);
			if (board.queenExistsAt(loc)) {
				queen.setVisible(true);
				queen.setFill(board.isSquareUnderAttack(loc) ? Color.RED : Color.BLACK);

			} else {
				queen.setVisible(false);
			}
		}
	}

	private Polygon createQueen() {
		return new Polygon(1, 0, 3, 0, 4, -2, 3, -1, 3, -2.2, 2.4, -1, 2, -2.3, 1.6, -1, 1, -2.2, 1, -1, 0, -2);
	}
}
