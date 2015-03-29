package aima.gui.demo.search.tree.info;

import aima.core.api.search.Node;
import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * @author Ciaran O'Reilly
 */
public class FrontierInfoController<S> implements TreeSearchAlgoSimulator.Observer<S> {
    @FXML private ProgressBar frontierProgress;
    @FXML private Label currentFrontierCountLabel;
    @FXML private Label maxFrontierCountLabel;
    @FXML private Label stateLabel;
    @FXML private Label parentLabel;
    @FXML private Label actionLabel;
    @FXML private Label pathCostLabel;
    //
    private TreeSearchAlgoSimulator<S> simulator;

    public void setSimulator(TreeSearchAlgoSimulator<S> simulator) {
        this.simulator = simulator;

        simulator.currentExecutionIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int currentExecutionIndex = newValue.intValue();
                if (currentExecutionIndex >= 0) {
                    int max = 0;
                    for (int i = 0; i <= currentExecutionIndex; i++) {
                        int fs = simulator.getExecuted().get(i).frontierSize();
                        if (fs > max) {
                            max = fs;
                        }
                    }
                    int current = simulator.getExecuted().get(currentExecutionIndex).frontierSize();
                    Node<S> cNode = simulator.getExecuted().get(currentExecutionIndex).node();

                    currentFrontierCountLabel.textProperty().set("" + current);
                    maxFrontierCountLabel.textProperty().set("" + max);

                    if (max > 0) {
                        frontierProgress.setProgress(((double) current) / ((double) max));
                    } else {
                        frontierProgress.setProgress(0);
                    }

                    String state = cNode == null ? "-" : cNode.state().toString();
                    String patent = cNode == null ? "-" : cNode.parent() == null ? "null" : cNode.parent().state().toString();
                    String action = cNode == null ? "-" : cNode.action().toString();
                    String cost = cNode == null ? "-" : "" + cNode.pathCost();
                    stateLabel.textProperty().set(state);
                    parentLabel.textProperty().set(patent);
                    actionLabel.textProperty().set(action);
                    pathCostLabel.textProperty().set(cost);
                }
            }
        });
    }

    @FXML
    private void initialize() {

    }
}
