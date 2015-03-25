package aima.gui.demo.search.tree.info;

import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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


    public void setSimulator(TreeSearchAlgoSimulator<S> simulator) {
        this.simulator = simulator;

        simulator.currentExecutionIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int max = 0;
                for (int i = 0; i < newValue.intValue(); i++) {
                    int fs = simulator.getExecuted().get(i).frontierSize();
                    if (fs > max) {
                        max = fs;
                    }
                }
                int current = simulator.getExecuted().get(newValue.intValue()-1).frontierSize();
// TODO - remove
System.out.println("idx="+newValue+", current="+current+", max="+max+", size="+simulator.getExecuted().size());
            }
        });
    }

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
