package aima.gui.demo.search.tree;

import aima.gui.demo.search.problem.RectangularGridProblemController;
import aima.gui.demo.search.tree.algorithm.GeneralTreeSearchController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchController {
    @FXML private AnchorPane problemPane;
    @FXML private AnchorPane searchAlgoPane;
    @FXML private AnchorPane searchInfoPane;

    @FXML
    private void initialize() throws IOException {
        Pane problem = FXMLLoader.load(RectangularGridProblemController.class.getResource("rectangulargridproblem.fxml"));
        anchor(problem);
        problemPane.getChildren().addAll(problem);

        Pane algo = FXMLLoader.load(GeneralTreeSearchController.class.getResource("generaltreesearch.fxml"));
        anchor(algo);
        searchAlgoPane.getChildren().add(algo);
    }

    private void anchor(Pane pane) {
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
    }
}
