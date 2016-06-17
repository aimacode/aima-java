package aima.gui.demo.search.tree.info;

import aima.core.search.api.Node;
import aima.extra.instrument.search.TreeSearchInstrumented;
import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

/**
 * @author Ciaran O'Reilly
 */
public class SummaryInfoController<A, S> implements TreeSearchAlgoSimulator.Observer<A, S> {
	@FXML
	private ProgressBar frontierProgress;
	@FXML
	private Label currentFrontierCountLabel;
	@FXML
	private Label maxFrontierCountLabel;
	@FXML
	private ProgressBar frontierAddRemoveProgress;
	@FXML
	private Label removedFrontierCountLabel;
	@FXML
	private Label addedFrontierCountLabel;
	@FXML
	private Label stateLabel;
	@FXML
	private Label parentLabel;
	@FXML
	private Label actionLabel;
	@FXML
	private Label pathCostLabel;
	//

	@Override
	public void setSimulator(TreeSearchAlgoSimulator<A, S> simulator) {
		simulator.currentExecutionIndexProperty()
				.addListener((observable, oldExecutionIndex, currentExecutionIndex) -> {
					int currentCount = 0;
					int maxCount = 0;
					double frontierMax = 0;
					int removedCount = 0;
					int addedCount = 0;
					double frontierAdded = 0;
					String state = "-";
					String patent = "-";
					String action = "-";
					String cost = "-";

					if (currentExecutionIndex.intValue() >= 0) {
						TreeSearchInstrumented.Cmd<A, S> cmd = simulator.getExecuted()
								.get(currentExecutionIndex.intValue());

						currentCount = cmd.currentFrontierSize();
						maxCount = cmd.maxFrontierSize();
						Node<A, S> node = cmd.node();

						if (maxCount > 0) {
							frontierMax = ((double) currentCount) / ((double) maxCount);
						}

						addedCount = cmd.numberAddedToFrontier();
						removedCount = cmd.numberRemovedFromFrontier();

						if (addedCount > 0) {
							frontierAdded = ((double) removedCount) / ((double) addedCount);
						}

						state = node == null ? "-" : node.state().toString();
						patent = node == null ? "-" : node.parent() == null ? "null" : node.parent().state().toString();
						action = node == null ? "-" : ("" + node.action());
						cost = node == null ? "-" : "" + node.pathCost();
					}

					currentFrontierCountLabel.textProperty().set("" + currentCount);
					maxFrontierCountLabel.textProperty().set("" + maxCount);
					frontierProgress.setProgress(frontierMax);
					removedFrontierCountLabel.textProperty().set("" + removedCount);
					addedFrontierCountLabel.textProperty().set("" + addedCount);
					frontierAddRemoveProgress.setProgress(frontierAdded);
					stateLabel.textProperty().set(state);
					parentLabel.textProperty().set(patent);
					actionLabel.textProperty().set(action);
					pathCostLabel.textProperty().set(cost);
				});
	}

	@FXML
	private void initialize() {
	}
}
