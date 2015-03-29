package aima.gui.demo.search.tree.info;

import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import aima.gui.demo.search.tree.info.rectangular.RectangularStateSpaceInfoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchInfoController<S> implements TreeSearchAlgoSimulator.Observer<S> {
    @FXML private AnchorPane stateSpaceInfo;
    @FXML private AnchorPane searchSpaceInfo;
    @FXML private AnchorPane frontierInfo;
    //
    private TreeSearchAlgoSimulator<S> simulator;
    //
    private TreeSearchAlgoSimulator.Observer<S> frontierInfoController;


    public void setSimulator(TreeSearchAlgoSimulator<S> simulator) {
        this.simulator = simulator;

        frontierInfoController.setSimulator(simulator);
    }

    @FXML
    private void initialize() throws IOException {
        Pane state = FXMLLoader.load(RectangularStateSpaceInfoController.class.getResource("rectangularstatespaceinfo.fxml"));
        anchor(state);
        stateSpaceInfo.getChildren().add(state);

        Pane search = FXMLLoader.load(SearchSpaceInfoController.class.getResource("searchspaceinfo.fxml"));
        anchor(search);
        searchSpaceInfo.getChildren().add(search);

        FXMLLoader frontierInfoLoader = new FXMLLoader(FrontierInfoController.class.getResource("frontierinfo.fxml"));
        Pane frontierInfoPane = frontierInfoLoader.load();
        anchor(frontierInfoPane);
        frontierInfo.getChildren().add(frontierInfoPane);
        frontierInfoController = frontierInfoLoader.getController();
    }

    private void anchor(Pane pane) {
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
    }
}
