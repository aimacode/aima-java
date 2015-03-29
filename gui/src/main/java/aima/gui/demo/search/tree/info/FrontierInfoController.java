package aima.gui.demo.search.tree.info;

import aima.core.api.search.Node;
import aima.extra.instrument.search.TreeSearchCmdInstr;
import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
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

        simulator.currentExecutionIndexProperty().addListener((observable, oldExecutionIndex, currentExecutionIndex) -> {
            if (currentExecutionIndex.intValue() >= 0) {
                TreeSearchCmdInstr.Cmd<S> cmd  = simulator.getExecuted().get(currentExecutionIndex.intValue());

                int current  = cmd.frontierSize();
                int max      = cmd.maxFrontierSize();
                Node<S> node = cmd.node();

                currentFrontierCountLabel.textProperty().set("" + current);
                maxFrontierCountLabel.textProperty().set("" + max);

                if (max > 0) {
                    frontierProgress.setProgress(((double) current) / ((double) max));
                } else {
                    frontierProgress.setProgress(0);
                }

                String state  = node == null ? "-" : node.state().toString();
                String patent = node == null ? "-" : node.parent() == null ? "null" : node.parent().state().toString();
                String action = node == null ? "-" : node.action().toString();
                String cost   = node == null ? "-" : "" + node.pathCost();

                stateLabel.textProperty().set(state);
                parentLabel.textProperty().set(patent);
                actionLabel.textProperty().set(action);
                pathCostLabel.textProperty().set(cost);
            }
        });
    }

    @FXML
    private void initialize() {

    }
}
