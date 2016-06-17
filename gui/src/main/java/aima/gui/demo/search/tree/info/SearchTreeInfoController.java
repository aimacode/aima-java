package aima.gui.demo.search.tree.info;

import aima.core.search.basic.support.BasicNode;
import aima.extra.instrument.search.TreeSearchInstrumented;
import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import aima.gui.support.fx.FXUtil;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.Collections;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 */
public class SearchTreeInfoController<A, S> implements TreeSearchAlgoSimulator.Observer<A, S> {
    @FXML private GridPane   searchTreeViewGridPane;

    @Override
    public void setSimulator(TreeSearchAlgoSimulator<A, S> simulator) {

        simulator.currentExecutionIndexProperty().addListener((observable, oldExecutionIndex, currentExecutionIndex) -> {
            List<Integer> levelCounts      = Collections.emptyList();
            List<Integer> unexploredCounts = Collections.emptyList();
            int           currentLevel     = 0;
            if (currentExecutionIndex.intValue() >= 0) {
                TreeSearchInstrumented.Cmd<A, S> cmd = simulator.getExecuted().get(currentExecutionIndex.intValue());
                levelCounts      = cmd.searchSpaceLevelCounts();
                unexploredCounts = cmd.searchSpaceLevelRemainingCounts();
                currentLevel     = cmd.lastNodeVisited() != null ? BasicNode.depth(cmd.lastNodeVisited()) : 0;
            }

            int childCount = searchTreeViewGridPane.getChildren().size();
            if ((childCount / 4) > (levelCounts.size()+1)) {
                searchTreeViewGridPane.getChildren().remove((levelCounts.size()+1)*4, searchTreeViewGridPane.getChildren().size());
            }
            int offset = 0;
            for (int i = 0; i < levelCounts.size(); i++) {
                offset += 4;
                if (offset >= searchTreeViewGridPane.getChildren().size()) {
                    Label arrow = new Label("");
                    if (currentLevel == i) {
                        FXUtil.setDefaultLabelIcon(arrow, FontAwesomeIcons.ARROW_RIGHT);
                    }
                    searchTreeViewGridPane.add(arrow, 0, i+1);
                    searchTreeViewGridPane.add(new Label(""+i), 1, i+1);
                    searchTreeViewGridPane.add(new Label(""+levelCounts.get(i)), 2, i+1);
                    searchTreeViewGridPane.add(new Label(""+unexploredCounts.get(i)), 3, i+1);
                }
                else {
                    Label arrow = (Label) searchTreeViewGridPane.getChildren().get(offset);
                    if (currentLevel == i) {
                        FXUtil.setDefaultLabelIcon(arrow, FontAwesomeIcons.ARROW_RIGHT);
                    }
                    else {
                        arrow.setGraphic(null);
                    }
                    ((Label)searchTreeViewGridPane.getChildren().get(offset+1)).setText(""+i);
                    ((Label)searchTreeViewGridPane.getChildren().get(offset+2)).setText(""+levelCounts.get(i));
                    ((Label)searchTreeViewGridPane.getChildren().get(offset+3)).setText(""+unexploredCounts.get(i));
                }
            }
        });
    }

    @FXML
    private void initialize() {

    }
}
