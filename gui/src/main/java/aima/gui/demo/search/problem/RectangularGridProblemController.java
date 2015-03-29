package aima.gui.demo.search.problem;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularGridProblemController {
    @FXML private Label  problemTypeLabel;
    @FXML private Button optionsButton;
    @FXML private Button listProblemsButton;
    @FXML private ScrollPane problemViewScrollPane;
    @FXML private Pane problemViewPane;

    //
    private static final String _iconSize = "16px";
    //
    private Circle      startNode = null;
    private Set<Circle> goalNodes = new HashSet<>();
    private Circle[][] nodes;
    //
    private int numWidthNodes  = 5;
    private int numHeightNodes = 5;
    //
    private int nodeRadius              = 10;
    private int borderPadding           = 10;
    private int nodeRadiusSpacingFactor = 5;
    private int viewportPadding         = 8;
    //
    private Paint defaultPaint    = Color.WHITE;
    private Image goalImage       = new Image(RectangularGridProblemController.class.getResourceAsStream("goal.png"));
    private Image startGoalImage  = new Image(RectangularGridProblemController.class.getResourceAsStream("startgoal.png"));
    private Paint startPaint      = Color.DARKGREEN;
    private Paint goalPaint       = new ImagePattern(goalImage, 0, 0, 16, 16, false);
    private Paint startGoalPaint  = new ImagePattern(startGoalImage, 0, 0, 16, 16, false);

    public void setupProblem() {
        // Ensure is clear of children on each setup
        problemViewPane.getChildren().clear();
        startNode = null;
        goalNodes.clear();

        int width  = numWidthNodes  * nodeRadius * nodeRadiusSpacingFactor;
        int height = numHeightNodes * nodeRadius * nodeRadiusSpacingFactor;
        int xInset = 0;
        int yInset = 0;
        int vpWidth  =  (int) problemViewScrollPane.getWidth() - viewportPadding;
        int vpHeight =  (int) problemViewScrollPane.getHeight() - viewportPadding;
        if (width < vpWidth) {
            xInset = (vpWidth - width) / 2;
            problemViewPane.setPrefWidth(vpWidth);
        }
        else {
            problemViewPane.setPrefWidth(width);
        }
        if (height < vpHeight) {
            yInset = (vpHeight - height) / 2;
            problemViewPane.setPrefHeight(vpHeight);
        }
        else {
            problemViewPane.setPrefHeight(height);
        }

        nodes = new Circle[numWidthNodes][numHeightNodes];
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                int x = xInset + (nodeRadius + borderPadding) + (i * (nodeRadius * nodeRadiusSpacingFactor));
                int y = yInset + (nodeRadius + borderPadding) + (j * (nodeRadius * nodeRadiusSpacingFactor));
                nodes[i][j] = new Circle(x, y, nodeRadius);
                nodes[i][j].setFill(defaultPaint);
                nodes[i][j].setStroke(Color.BLACK);
                nodes[i][j].setStrokeWidth(1);

                Tooltip t = new Tooltip("("+i+","+j+")");
                Tooltip.install(nodes[i][j], t);

                nodes[i][j].setOnMouseClicked(me -> {
                    Circle clicked = (Circle) me.getSource();

                    if (startNode == null) {
                        startNode = clicked;
                        startNode.setFill(startPaint);
                        goalNodes.remove(startNode); // Ensure is not a goal
                    }
                    else if (startNode != null && clicked == startNode) {
                        if (goalNodes.contains(clicked)) {
                            startNode = null;
                            goalNodes.remove(clicked);
                            clicked.setFill(defaultPaint);
                        }
                        else {
                            goalNodes.add(clicked);
                            clicked.setFill(startGoalPaint);
                        }
                    }
                    else {
                        if (goalNodes.contains(clicked)) {
                            clicked.setFill(defaultPaint);
                            goalNodes.remove(clicked);
                        }
                        else {
                            clicked.setFill(goalPaint);
                            goalNodes.add(clicked);
                        }
                    }
                });

                problemViewPane.getChildren().add(nodes[i][j]);

                if (i > 0) {
                    Line horizLine = new Line(x - (nodeRadius*(nodeRadiusSpacingFactor-1)), y, x - nodeRadius, y);
                    problemViewPane.getChildren().add(horizLine);
                }

                if (j > 0) {
                    Line vertLine = new Line(x, y - (nodeRadius*(nodeRadiusSpacingFactor-1)), x, y - nodeRadius);
                    problemViewPane.getChildren().add(vertLine);
                }
            }
        }
    }

    @FXML
    private void initialize() {
        GlyphsDude.setIcon(optionsButton, FontAwesomeIcons.GEAR, _iconSize, ContentDisplay.GRAPHIC_ONLY);
        GlyphsDude.setIcon(listProblemsButton, FontAwesomeIcons.BARS, _iconSize, ContentDisplay.GRAPHIC_ONLY);

        optionsButton.setTooltip(new Tooltip("Configure Problem"));
        listProblemsButton.setTooltip(new Tooltip("Select Problem"));

        problemViewScrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {

                setupProblem();

                // Only do once
                problemViewScrollPane.viewportBoundsProperty().removeListener(this);
            }
        });

    }
}
