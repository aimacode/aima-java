package aima.gui.demo.search.tree.algorithm;

import aima.extra.instrument.search.TreeSearchCmdInstr;
import aima.gui.demo.search.problem.rectangular.AtVertex;
import aima.gui.demo.search.problem.rectangular.RectangularProblem;
import aima.gui.support.code.CodeCommand;
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
import javafx.scene.text.FontWeight;
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
    private static final Font _defaultPseudoCodeFont = Font.font(java.awt.Font.MONOSPACED, FontWeight.NORMAL, 11);
    private static final Font _boldPseudoCodeFont    = Font.font(java.awt.Font.MONOSPACED, FontWeight.BOLD, 11);
    private static final Font _defaultCodeFont       = Font.font(java.awt.Font.MONOSPACED, FontWeight.NORMAL, 11);
    private static final Font _boldCodeFont          = Font.font(java.awt.Font.MONOSPACED, FontWeight.BOLD, 11);
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
    @FXML private Button goFirstStepButton;
    @FXML private Button previousStepButton;
    @FXML private Button nextStepButton;
    @FXML private Button goLastStepButton;
    //
    private List<CodeRepresentationInfo> codeInfo = new ArrayList<>();
    private TreeSearchAlgoSimulator<S> simulator;
    private boolean inUpdateSliderCall = false;
    //
    private ScheduledService<Void> autoPlayBack = new ScheduledService<Void>() {
        protected Task<Void> createTask() {
            return new Task<Void>() {
                protected Void call() {
                    Platform.runLater(() -> {
                        if (!simulator.isCurrentExecutionIndexAtEnd()) {
                            simulator.incCurrentExecutionIndex();
                        }
                        else {
                            autoPlayButton.fire();
                        }
                    });
                    return null;
                }
            };
        }
    };

    public void setSimulator(TreeSearchAlgoSimulator<S> simulator) {
        this.simulator = simulator;
        simulator.currentExecutionIndexProperty().addListener((observable, oldExecutionIndex, currentExecutionIndex) -> {
            updateSlider();
            updateCodeRepresentations();
        });
        simulator.executedProperty().addListener((observable, oldValue, newValue) -> {
            updateSlider();
        });
    }


    @FXML
    private void initialize() {
        GlyphsDude.setIcon(listAlgorithmsButton, FontAwesomeIcons.BARS, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(autoPlayButton, FontAwesomeIcons.PLAY, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(goFirstStepButton, FontAwesomeIcons.FAST_BACKWARD, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(previousStepButton, FontAwesomeIcons.STEP_BACKWARD, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(nextStepButton, FontAwesomeIcons.STEP_FORWARD, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(goLastStepButton, FontAwesomeIcons.FAST_FORWARD, _iconSize, ContentDisplay.GRAPHIC_ONLY);

        listAlgorithmsButton.setTooltip(new Tooltip("Select algorithm"));
        executionStepSlider.setMin(0);
        executionStepSlider.setMax(1);
        autoPlayButton.setTooltip(new Tooltip("Auto play execution"));
        stepsASecondChoiceBox.setItems(FXCollections.<Integer>observableArrayList(1, 2, 3, 4, 5, 10, 20, 50));
        stepsASecondChoiceBox.setValue(1);
        goFirstStepButton.setTooltip(new Tooltip("Go to first execution step"));
        previousStepButton.setTooltip(new Tooltip("Go to previous execution step"));
        nextStepButton.setTooltip(new Tooltip("Got to next execution step"));
        goLastStepButton.setTooltip(new Tooltip("Go to last execution step"));

        stepsASecondChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            autoPlayBack.setPeriod(Duration.seconds(1.0 / newValue.doubleValue()));
        });

        executionStepSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!inUpdateSliderCall) {
                simulator.setCurrentExecutionIndex(newValue.intValue());
            }
        });

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

            codeInfo.add(new CodeRepresentationInfo(cr, text));

            tf.getChildren().addAll(text);

            ScrollPane sp = new ScrollPane(tf);
            sp.getStyleClass().add("contentPane");

            tab.setContent(sp);
            codeTabPane.getTabs().add(tab);
        });
    }

    @FXML
    private void autoPlay(ActionEvent event) {
        if (autoPlayBack.isRunning()) {
            // Stop auto-playing
            GlyphsDude.setIcon(autoPlayButton, FontAwesomeIcons.PLAY, _iconSize, ContentDisplay.GRAPHIC_ONLY);
            autoPlayButton.setTooltip(new Tooltip("Auto play execution"));
            autoPlayBack.cancel();
        }
        else {
            // Start auto-playing
            GlyphsDude.setIcon(autoPlayButton, FontAwesomeIcons.STOP, _iconSize, ContentDisplay.GRAPHIC_ONLY);
            autoPlayButton.setTooltip(new Tooltip("Stop auto playing execution"));
            autoPlayBack.setPeriod(Duration.seconds(1.0 / (double) stepsASecondChoiceBox.getValue()));
            autoPlayBack.restart();
        }
    }

    @FXML
    private void firstStep(ActionEvent ae) {
        simulator.setCurrentExecutionIndexFirst();
    }

    @FXML
    private void previousStep(ActionEvent ae) {
        simulator.decCurrentExecutionIndex();
    }

    @FXML
    private void nextStep(ActionEvent ae) {
        simulator.incCurrentExecutionIndex();
    }

    @FXML
    private void lastStep(ActionEvent ae) {
        simulator.setCurrentExecutionIndexLast();
    }

    private void updateSlider() {
        inUpdateSliderCall = true;
        if (simulator.isExecutionStarted()) {
            executionStepSlider.setMin(0);
            executionStepSlider.setMax(simulator.getExecuted().size()-1);
            executionStepSlider.setValue(simulator.getCurrentExecutionIndex());
        }
        inUpdateSliderCall = false;
    }

    private void updateCodeRepresentations() {
        if (simulator.isExecutionStarted() && simulator.getCurrentExecutionIndex() >= 0) {
            codeInfo.forEach(ci -> {

                Font normal;
                Font bold;
                if (ci.representation.codeTypeName.equalsIgnoreCase("Pseudo-Code")) {
                    normal = _defaultPseudoCodeFont;
                    bold   = _boldPseudoCodeFont;
                }
                else {
                    normal = _defaultCodeFont;
                    bold   = _boldCodeFont;
                }

                ci.lastHightlighted.forEach(t -> {
                    t.setUnderline(false);
                    t.setFont(normal);
                });
                ci.lastHightlighted.clear();
                TreeSearchCmdInstr.Cmd<S> cmd = simulator.getExecuted().get(simulator.getCurrentExecutionIndex());
                CodeCommand codeCommand = ci.representation.commandIdToCommand.get(cmd.commandId());
                codeCommand.sourceIndexes.forEach(si -> {
                    for (int i = si.startIdx; i < si.endIdx; i++) {
                        ci.lastHightlighted.add(ci.text.get(i));
                    }
                });
                ci.lastHightlighted.forEach(t -> {
                    t.setUnderline(true);
                    t.setFont(bold);
                });
            });
        }
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

    private class CodeRepresentationInfo {
        public final CodeRepresentation representation;
        public final List<Text> text;
        public final List<Text> lastHightlighted = new ArrayList<>();

        CodeRepresentationInfo(CodeRepresentation codeRepresentation, List<Text> text) {
            this.representation = codeRepresentation;
            this.text           = new ArrayList<>(text);
        }
    }
}
