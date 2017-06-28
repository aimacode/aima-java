package aima.gui.fx.views;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.MouseButton.SECONDARY;

/**
 * Provides a view for Sudoku puzzles. Digit positions are specified by column and row indices between 1 (top / left)
 * and 9 (bottom / right). Fixed digits are shown in brackets.
 *
 * @author Ruediger Lunde
 */
public class SudokuViewCtrl {

    private List<ComboBox<String>> combos = new ArrayList<>(81);

    /**
     * Adds a grid pane with combo boxes to the provided root pane and returns
     * a controller class instance containing application logic.
     */
    public SudokuViewCtrl(StackPane viewRoot) {
        // create grid layout
        GridPane gridPane = new GridPane();
        viewRoot.getChildren().add(gridPane);
        viewRoot.setAlignment(Pos.BOTTOM_CENTER);
        gridPane.maxWidthProperty().bind(viewRoot.widthProperty().subtract(20));
        gridPane.maxHeightProperty().bind(viewRoot.heightProperty().subtract(10));
        for (int i = 0; i < 9; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPercentHeight(100.0 / 9);
            rc.setValignment(i % 3 == 1 ? VPos.CENTER : (i % 3) == 0 ? VPos.BOTTOM : VPos.TOP);
            gridPane.getRowConstraints().add(rc);
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 9);
            cc.setHalignment(i % 3 == 1 ? HPos.CENTER : (i % 3) == 0 ? HPos.RIGHT : HPos.LEFT);
            gridPane.getColumnConstraints().add(cc);
        }

        // create digit combo boxes
        List<String> values = new ArrayList<>();
        values.add("   ");
        for (int i = 1; i <= 9; i++)
            values.add(" " + i + " ");
        for (int i = 1; i <= 9; i++)
            values.add("(" + i + ")");
        for (int i = 0; i < 81; i++) {
            ComboBox<String> combo = new ComboBox<>();
            combo.setStyle("-fx-font: 18px \"Courier New\"; -fx-font-weight: bold;");
            combo.getItems().addAll(values);
            combo.getSelectionModel().select(0);
            combos.add(combo);
            gridPane.add(combo, i % 9, i / 9);
        }
        gridPane.setOnMousePressed((ev) -> { if (ev.getButton() == SECONDARY) clear(false); });
    }

    public void clear(boolean allDigits) {
        for (int i = 0; i < 81; i++) {
            ComboBox<String> combo = combos.get(i);
            if (allDigits || !isFixed(i % 9 + 1, i / 9 + 1))
                combo.getSelectionModel().select(0);
        }
    }

    public void clearDigit(int col, int row) {
        getCombo(col, row).getSelectionModel().select(0);
    }

    public void setDigit(int col, int row, int digit) {
        getCombo(col, row).getSelectionModel().select(digit);
    }

    public void fixDigit(int col, int row, int digit) {
        getCombo(col, row).getSelectionModel().select(digit + 9);
    }

    /** Return a digit between 1 and 9 or -1 if no value has been selected yet. */
    public int getDigit(int col, int row) {
        int selIdx = getCombo(col, row).getSelectionModel().getSelectedIndex();
        if (selIdx == 0)
            return -1;
        else if (selIdx < 10)
            return selIdx;
        else
            return selIdx - 9;
    }

    public boolean isFixed(int col, int row) {
        int selIdx = getCombo(col, row).getSelectionModel().getSelectedIndex();
        return selIdx >= 10;
    }

    private ComboBox<String> getCombo(int col, int row) {
        assert col >= 1 && col <= 9;
        assert row >= 1 && row <= 9;
        return combos.get((row-1) * 9 + (col-1));
    }
}
