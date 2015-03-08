package aima.gui.app;

import aima.gui.demo.search.problem.RectangularGridProblem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * @author Ciaran O'Reilly
 */
public class AppController {
    @FXML private ToolBar toolBar;
    @FXML private BorderPane mainBorderPane;
    @FXML private BorderPane notificationPane;

    @FXML
    private void initialize() throws IOException {
        Pane problem = FXMLLoader.load(RectangularGridProblem.class.getResource("rectangulargridproblem.fxml"));
        mainBorderPane.setCenter(problem);

    }
}
