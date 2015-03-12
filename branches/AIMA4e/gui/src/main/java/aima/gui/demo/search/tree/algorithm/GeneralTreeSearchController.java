package aima.gui.demo.search.tree.algorithm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.fxml.FXML;
import javafx.scene.control.*;


/**
 * @author Ciaran O'Reilly
 */
public class GeneralTreeSearchController {
    @FXML private Slider executionStepSlider;
    @FXML private ListView<String> pseudoCodeList;
    @FXML private ListView<String> javaCodeList;
    @FXML private Button autoPlayButton;
    @FXML private ChoiceBox<Integer> stepsASecondChoiceBox;
    @FXML private Button startButton;
    @FXML private Button backButton;
    @FXML private Button forwardButton;
    @FXML private Button endButton;
    @FXML private Button resetButton;

    @FXML
    private void initialize() {
        executionStepSlider.setMin(0);
        executionStepSlider.setBlockIncrement(1);
        executionStepSlider.setMax(1);
        autoPlayButton.setTooltip(new Tooltip("Auto Play"));
        stepsASecondChoiceBox.setItems(FXCollections.<Integer>observableArrayList(1, 2, 3, 4, 5, 10, 20, 50));
        stepsASecondChoiceBox.setValue(1);
        startButton.setTooltip(new Tooltip("Start"));
        backButton.setTooltip(new Tooltip("Back"));
        forwardButton.setTooltip(new Tooltip("Forward"));
        endButton.setTooltip(new Tooltip("End"));
    }
}
