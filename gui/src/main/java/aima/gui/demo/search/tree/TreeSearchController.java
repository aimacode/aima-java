package aima.gui.demo.search.tree;

import aima.gui.demo.search.problem.rectangular.RectangularGridProblemController;
import aima.gui.demo.search.problem.rectangular.AtVertex;
import aima.gui.demo.search.problem.rectangular.RectangularProblem;
import aima.gui.demo.search.tree.algorithm.GeneralTreeSearchController;
import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import aima.gui.demo.search.tree.info.TreeSearchInfoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchController {
    @FXML private AnchorPane problemPane;
    @FXML private AnchorPane searchAlgoPane;
    @FXML private AnchorPane searchInfoPane;
    //
    private TreeSearchAlgoSimulator<AtVertex> simulator = new TreeSearchAlgoSimulator<>();

    @FXML
    private void initialize() throws IOException {
        Pane problem = FXMLLoader.load(RectangularGridProblemController.class.getResource("rectangulargridproblem.fxml"));
        anchor(problem);
        problemPane.getChildren().addAll(problem);

        FXMLLoader treeSearchLoader = new FXMLLoader(GeneralTreeSearchController.class.getResource("generaltreesearch.fxml"));
        Pane algo = treeSearchLoader.load();
        anchor(algo);
        searchAlgoPane.getChildren().add(algo);
        TreeSearchAlgoSimulator.Observer<AtVertex> treeSearchController = treeSearchLoader.getController();
        treeSearchController.setSimulator(simulator);

        FXMLLoader treeInfoLoader = new FXMLLoader(TreeSearchInfoController.class.getResource("treesearchinfo.fxml"));
        Pane info = treeInfoLoader.load();
        anchor(info);
        searchInfoPane.getChildren().add(info);
        TreeSearchAlgoSimulator.Observer<AtVertex> treeInfoController = treeInfoLoader.getController();
        treeInfoController.setSimulator(simulator);


 // TODO - move into problem definition controller
        simulator.setProblem(new RectangularProblem(2, 2, new AtVertex(0, 0), Collections.singletonList(new AtVertex(1, 1))));
        simulator.start();
    }

    private void anchor(Pane pane) {
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
    }
}
