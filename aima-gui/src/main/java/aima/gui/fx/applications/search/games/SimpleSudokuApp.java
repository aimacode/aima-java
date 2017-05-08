package aima.gui.fx.applications.search.games;

import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.views.SudokuViewCtrl;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Simple Sudoku puzzle application. It provides a playground for programming
 * experiments with CSP algorithms. But note that no CSP implementation is included here.
 *
 * @author Ruediger Lunde
 */
public class SimpleSudokuApp extends IntegrableApplication {

    SudokuViewCtrl viewCtrl;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getTitle() {
        return "Simple Sudoku App";
    }

    @Override
    public Pane createRootPane() {
        StackPane root = new StackPane();
        viewCtrl = new SudokuViewCtrl(root);
        return root;
    }

    @Override
    public void initialize() {
        createPuzzle1(viewCtrl);
    }

    @Override
    public void cleanup() {

    }

    public static void createPuzzle1 (SudokuViewCtrl viewCtrl) {
        // taken from https://en.wikipedia.org/wiki/Sudoku.
        viewCtrl.clear(true);
        viewCtrl.fixDigit(1, 1, 5);
        viewCtrl.fixDigit(2, 1, 3);
        viewCtrl.fixDigit(1, 2, 6);
        viewCtrl.fixDigit(2, 3, 9);
        viewCtrl.fixDigit(3, 3, 8);

        viewCtrl.fixDigit(5, 1, 7);
        viewCtrl.fixDigit(4, 2, 1);
        viewCtrl.fixDigit(5, 2, 9);
        viewCtrl.fixDigit(6, 2, 5);

        viewCtrl.fixDigit(8, 3, 6);

        viewCtrl.fixDigit(1, 4, 8);
        viewCtrl.fixDigit(1, 5, 4);
        viewCtrl.fixDigit(1, 6, 7);

        viewCtrl.fixDigit(5, 4, 6);
        viewCtrl.fixDigit(4, 5, 8);
        viewCtrl.fixDigit(6, 5, 3);
        viewCtrl.fixDigit(5, 6, 2);

        viewCtrl.fixDigit(9, 4, 3);
        viewCtrl.fixDigit(9, 5, 1);
        viewCtrl.fixDigit(9, 6, 6);

        viewCtrl.fixDigit(2, 7, 6);

        viewCtrl.fixDigit(4, 8, 4);
        viewCtrl.fixDigit(5, 8, 1);
        viewCtrl.fixDigit(6, 8, 9);
        viewCtrl.fixDigit(5, 9, 8);

        viewCtrl.fixDigit(7, 7, 2);
        viewCtrl.fixDigit(8, 7, 8);
        viewCtrl.fixDigit(9, 8, 5);
        viewCtrl.fixDigit(8, 9, 7);
        viewCtrl.fixDigit(9, 9, 9);
    }
}
