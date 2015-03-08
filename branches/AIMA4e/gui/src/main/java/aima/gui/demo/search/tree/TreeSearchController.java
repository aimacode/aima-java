package aima.gui.demo.search.tree;

import aima.gui.demo.search.problem.RectangularGridProblemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchController {
    @FXML private BorderPane problemPane;
    @FXML private BorderPane searchAlgoPane;
    @FXML private BorderPane searchInfoPane;

    @FXML
    private void initialize() throws IOException {
        Pane problem = FXMLLoader.load(RectangularGridProblemController.class.getResource("rectangulargridproblem.fxml"));
        problemPane.setCenter(problem);
    }
}
