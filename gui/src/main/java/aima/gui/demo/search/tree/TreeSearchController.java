package aima.gui.demo.search.tree;

import aima.gui.demo.search.problem.rectangular.RectangularGridProblemController;
import aima.gui.demo.search.problem.rectangular.AtVertex;
import aima.gui.demo.search.tree.algorithm.GeneralTreeSearchController;
import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import aima.gui.demo.search.tree.info.FrontierInfoController;
import aima.gui.demo.search.tree.info.SearchSpaceInfoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchController {
    @FXML private AnchorPane problemPane;
    @FXML private AnchorPane searchAlgoPane;
    @FXML private AnchorPane stateSpaceInfo;
    @FXML private AnchorPane searchSpaceInfo;
    @FXML private AnchorPane frontierInfo;
    //
    private TreeSearchAlgoSimulator<AtVertex> simulator = new TreeSearchAlgoSimulator<>();

    @FXML
    private void initialize() throws IOException {
        FXMLLoader problemLoader = new FXMLLoader(RectangularGridProblemController.class.getResource("rectangulargridproblem.fxml"));
        Pane problem = problemLoader.load();
        anchor(problem);
        problemPane.getChildren().addAll(problem);
        RectangularGridProblemController problemController = problemLoader.getController();
        problemController.setSimulator(simulator);

        Pane state = problemController.createSearchSpaceInfoRepresentation();
        anchor(state);
        stateSpaceInfo.getChildren().add(state);

        FXMLLoader treeSearchLoader = new FXMLLoader(GeneralTreeSearchController.class.getResource("generaltreesearch.fxml"));
        Pane algo = treeSearchLoader.load();
        anchor(algo);
        searchAlgoPane.getChildren().add(algo);
        TreeSearchAlgoSimulator.Observer<AtVertex> treeSearchController = treeSearchLoader.getController();
        treeSearchController.setSimulator(simulator);

        Pane search = FXMLLoader.load(SearchSpaceInfoController.class.getResource("searchspaceinfo.fxml"));
        anchor(search);
        searchSpaceInfo.getChildren().add(search);

        FXMLLoader frontierInfoLoader = new FXMLLoader(FrontierInfoController.class.getResource("frontierinfo.fxml"));
        Pane frontierInfoPane = frontierInfoLoader.load();
        anchor(frontierInfoPane);
        frontierInfo.getChildren().add(frontierInfoPane);
        TreeSearchAlgoSimulator.Observer<AtVertex> frontierInfoController = frontierInfoLoader.getController();
        frontierInfoController.setSimulator(simulator);
    }

    private void anchor(Pane pane) {
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
    }
}
