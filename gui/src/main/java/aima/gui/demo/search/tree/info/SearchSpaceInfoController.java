package aima.gui.demo.search.tree.info;

import aima.core.search.BasicNode;
import aima.extra.instrument.search.TreeSearchInstrumented;
import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 */
public class SearchSpaceInfoController<S> implements TreeSearchAlgoSimulator.Observer<S> {
    @FXML private GridPane   searchSpaceViewGridPane;

    @Override
    public void setSimulator(TreeSearchAlgoSimulator<S> simulator) {

        simulator.currentExecutionIndexProperty().addListener((observable, oldExecutionIndex, currentExecutionIndex) -> {
            List<Integer> levelCounts      = Collections.emptyList();
            List<Integer> unexploredCounts = Collections.emptyList();
            int           currentLevel     = 0;
            if (currentExecutionIndex.intValue() >= 0) {
                TreeSearchInstrumented.Cmd<S> cmd = simulator.getExecuted().get(currentExecutionIndex.intValue());
                levelCounts      = cmd.searchSpaceLevelCounts();
                unexploredCounts = cmd.searchSpaceLevelRemainingCounts();
                currentLevel     = cmd.lastNodeVisited() != null ? BasicNode.depth(cmd.lastNodeVisited()) : 0;
            }

            int childCount = searchSpaceViewGridPane.getChildren().size();
            if ((childCount / 4) > (levelCounts.size()+1)) {
                searchSpaceViewGridPane.getChildren().remove((levelCounts.size()+1)*4, searchSpaceViewGridPane.getChildren().size());
            }
            int offset = 0;
            for (int i = 0; i < levelCounts.size(); i++) {
                offset += 4;
                if (offset >= searchSpaceViewGridPane.getChildren().size()) {
                    searchSpaceViewGridPane.add(new Label(currentLevel == i ? "->" : ""), 0, i+1);
                    searchSpaceViewGridPane.add(new Label(""+i), 1, i+1);
                    searchSpaceViewGridPane.add(new Label(""+levelCounts.get(i)), 2, i+1);
                    searchSpaceViewGridPane.add(new Label(""+unexploredCounts.get(i)), 3, i+1);
                }
                else {
                    ((Label)searchSpaceViewGridPane.getChildren().get(offset)).setText(currentLevel == i ? "->" : "");
                    ((Label)searchSpaceViewGridPane.getChildren().get(offset+1)).setText(""+i);
                    ((Label)searchSpaceViewGridPane.getChildren().get(offset+2)).setText(""+levelCounts.get(i));
                    ((Label)searchSpaceViewGridPane.getChildren().get(offset+3)).setText(""+unexploredCounts.get(i));
                }
            }
        });
    }

    @FXML
    private void initialize() {

    }
}
