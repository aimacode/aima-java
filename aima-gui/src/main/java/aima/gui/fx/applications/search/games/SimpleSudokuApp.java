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

    /** Example taken from https://en.wikipedia.org/wiki/Sudoku. */
    public final static String puzzle1 = "" + //
            "53..7...." + //
            "6..195..." + //
            ".98....6." + //
            "8...6...3" + //
            "4..8.3..1" + //
            "7...2...6" + //
            ".6....28." + //
            "...419..5" + //
            "....8..79";

    /** Example taken from https://de.wikipedia.org/wiki/Sudoku. */
    public final static String puzzle2 = "" + //
            ".3......." + //
            "...195..." + //
            "..8....6." + //
            "8...6...." + //
            "4..8....1" + //
            "....2...." + //
            ".6....28." + //
            "...419..5" + //
            ".......7.";

    /** Example taken from http://sudoku.tagesspiegel.de/sudoku-sehr-schwer. */
    public final static String puzzle3 = "" + //
            ".....9.7." + //
            "....82.5." + //
            "327....4." + //
            ".16.4...." + //
            ".5....3.." + //
            "....9.7.." + //
            "...6....5" + //
            "8.2......" + //
            "..42....8";

    public static void main(String[] args) {
        launch(args);
    }

    protected SudokuViewCtrl stateViewCtrl;

    @Override
    public String getTitle() {
        return "Simple Sudoku App";
    }

    @Override
    public Pane createRootPane() {
        StackPane root = new StackPane();
        stateViewCtrl = new SudokuViewCtrl(root);
        return root;
    }

    @Override
    public void initialize() {
        initView(puzzle1);
    }

    @Override
    public void cleanup() {
    }

    protected void initView(String puzzle) {
        stateViewCtrl.clear(true);
        for (int i = 0; i < puzzle.length(); i++) {
            char ch = puzzle.charAt(i);
            if (ch >= '1' && ch <= '9')
                stateViewCtrl.fixDigit(i % 9 + 1, i / 9 + 1, ch - '0');
        }
    }
}
