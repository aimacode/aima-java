package aima.gui.demo.search.tree.info;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * @author Ciaran O'Reilly
 */
public class TreeSearchInfoController {
    @FXML private AnchorPane stateSpaceInfo;
    @FXML private AnchorPane searchSpaceInfo;
    @FXML private AnchorPane frontierInfo;

    @FXML
    private void initialize() throws IOException {
        Pane state = FXMLLoader.load(StateSpaceInfoController.class.getResource("statespaceinfo.fxml"));
        anchor(state);
        stateSpaceInfo.getChildren().add(state);

        Pane search = FXMLLoader.load(SearchSpaceInfoController.class.getResource("searchspaceinfo.fxml"));
        anchor(search);
        searchSpaceInfo.getChildren().add(search);

        Pane frontier = FXMLLoader.load(FrontierInfoController.class.getResource("frontierinfo.fxml"));
        anchor(frontier);
        frontierInfo.getChildren().add(frontier);
    }

    private void anchor(Pane pane) {
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
    }
}
