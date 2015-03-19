package aima.gui.demo.search.tree.algorithm;

import aima.gui.support.code.CodeReader;
import aima.gui.support.code.CodeRepresentation;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Ciaran O'Reilly
 */
public class GeneralTreeSearchController {
    @FXML private Slider executionStepSlider;
    @FXML private TabPane codeTabPane;
    @FXML private Button autoPlayButton;
    @FXML private ChoiceBox<Integer> stepsASecondChoiceBox;
    @FXML private Button startButton;
    @FXML private Button backButton;
    @FXML private Button forwardButton;
    @FXML private Button endButton;
    @FXML private Button resetButton;
    //
    private static final Font _defaultPseudoCodeFont = Font.font(java.awt.Font.SANS_SERIF, 12);
    private static final Font _defaultCodeFont       = Font.font(java.awt.Font.MONOSPACED, 12);

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

        List<CodeRepresentation> codeRepresentations = CodeReader.read("tree-search.code");
        codeRepresentations.forEach(cr -> {
            Tab tab = new Tab(cr.codeTypeName);
            TextFlow tf = new TextFlow();

            List<Text> text = new ArrayList<>();
            for (int i = 0; i < cr.source.length(); i++) {
                Text t = new Text(cr.source.substring(i, i+1));
                text.add(t);
            }
            styleText(cr, text);

            tf.getChildren().addAll(text);

            ScrollPane sp = new ScrollPane(tf);
            sp.getStyleClass().add("contentPane");

            tab.setContent(sp);
            codeTabPane.getTabs().add(tab);
        });
    }

    private void styleText(CodeRepresentation cr, List<Text> text) {
        if (cr.codeTypeName.equalsIgnoreCase("Pseudo-Code")) {
            text.forEach(t -> t.setFont(_defaultPseudoCodeFont));
        }
        else if (cr.codeTypeName.equalsIgnoreCase("Java")) {
            text.forEach(t -> t.setFont(_defaultCodeFont));
            int s = 0;
            while (s < cr.source.length()) {
                s = cr.source.indexOf("//", s);
                if (s < 0) {
                    s = cr.source.length();
                }
                else {
                    int e = cr.source.indexOf("\n", s);
                    for (int i = s; i < e; i++) {
                        text.get(i).setFill(Color.DARKGREEN);
                    }
                    s = e;
                }
            }
        }
    }
}
