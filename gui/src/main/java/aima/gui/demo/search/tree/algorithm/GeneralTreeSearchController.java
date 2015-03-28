package aima.gui.demo.search.tree.algorithm;

import aima.extra.instrument.search.TreeSearchCmdInstr;
import aima.gui.demo.search.problem.rectangular.AtVertex;
import aima.gui.demo.search.problem.rectangular.RectangularProblem;
import aima.gui.support.code.CodeReader;
import aima.gui.support.code.CodeRepresentation;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Ciaran O'Reilly
 */
public class GeneralTreeSearchController<S> implements TreeSearchAlgoSimulator.Observer<S> {
    private static final String _iconSize = "16px";
    private static final Font _defaultPseudoCodeFont = Font.font(java.awt.Font.SANS_SERIF, 12);
    private static final Font _defaultCodeFont       = Font.font(java.awt.Font.MONOSPACED, 12);
    //
    private static final String[] JAVA_KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };
    private static final String JAVA_KEYWORD_PATTERN = "\\b(" + String.join("|", JAVA_KEYWORDS) + ")\\b";
    private static final Pattern JAVA_PATTERN = Pattern.compile(JAVA_KEYWORD_PATTERN);
    //
    @FXML private Slider executionStepSlider;
    @FXML private Button listAlgorithmsButton;
    @FXML private TabPane codeTabPane;
    @FXML private Button autoPlayButton;
    @FXML private ChoiceBox<Integer> stepsASecondChoiceBox;
    @FXML private Button startButton;
    @FXML private Button backButton;
    @FXML private Button forwardButton;
    @FXML private Button endButton;
    @FXML private Button resetButton;
    //
    private TreeSearchAlgoSimulator<S> simulator;
    //
    private ScheduledService<Void> autoPlayBack = new ScheduledService<Void>() {
        protected Task<Void> createTask() {
            return new Task<Void>() {
                protected Void call() {
                    Platform.runLater(() -> {
                        if (simulator.getCurrentExecutionIndex() < simulator.getExecuted().size()) {
                            simulator.incCurrentExecutionIndex();
                        }
                    });
                    return null;
                }
            };
        }
    };


    public void setSimulator(TreeSearchAlgoSimulator<S> simulator) {
        this.simulator = simulator;
    }


    @FXML
    protected void autoPlay(ActionEvent event) {
        if (autoPlayBack.isRunning()) {
            // Stop auto-playing
            GlyphsDude.setIcon(autoPlayButton, FontAwesomeIcons.PLAY, _iconSize, ContentDisplay.GRAPHIC_ONLY);
            autoPlayButton.setTooltip(new Tooltip("Auto Play"));
            autoPlayBack.cancel();
        }
        else {
            // Start auto-playing
            GlyphsDude.setIcon(autoPlayButton, FontAwesomeIcons.STOP, _iconSize, ContentDisplay.GRAPHIC_ONLY);
            autoPlayButton.setTooltip(new Tooltip("Stop Playing"));
            autoPlayBack.setPeriod(Duration.seconds(1.0 / (double) stepsASecondChoiceBox.getValue()));
            autoPlayBack.restart();
        }
    }

    @FXML
    private void initialize() {
        GlyphsDude.setIcon(listAlgorithmsButton, FontAwesomeIcons.BARS, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(autoPlayButton, FontAwesomeIcons.PLAY, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(startButton, FontAwesomeIcons.FAST_BACKWARD, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(backButton, FontAwesomeIcons.STEP_BACKWARD, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(forwardButton, FontAwesomeIcons.STEP_FORWARD, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(endButton, FontAwesomeIcons.FAST_FORWARD, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(resetButton, FontAwesomeIcons.EJECT, _iconSize, ContentDisplay.GRAPHIC_ONLY);

        listAlgorithmsButton.setTooltip(new Tooltip("Select Algorithm"));
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
        resetButton.setTooltip(new Tooltip("Reset"));

        List<CodeRepresentation> codeRepresentations = CodeReader.read("tree-search.code", TreeSearchCmdInstr.CMDS);
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
            // Handle keywords
            Matcher matcher = JAVA_PATTERN.matcher(cr.source);
            while (matcher.find()) {
                for (int i = matcher.start(); i < matcher.end(); i++) {
                    text.get(i).setFill(Color.BLUEVIOLET);
                }
            }

            // Handle comments
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
