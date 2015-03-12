package aima.gui.demo.search.problem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
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
    @FXML private Button listProblemsButton;
    @FXML private Button optionsButton;
    @FXML private ScrollPane problemViewScrollPane;
    @FXML private Pane problemViewPane;

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
    //
    private Paint defaultPaint = Color.WHITE;
    private Image goalImage    = new Image(RectangularGridProblemController.class.getResourceAsStream("goal.png"));
    private Paint goalPaint    = new ImagePattern(goalImage, 0, 0, 16, 16, false);
    private Paint startPaint   = Color.DARKGREEN;

    public void setupProblem() {
        // Ensure is clear of children on each setup
        problemViewPane.getChildren().clear();
        startNode = null;
        goalNodes.clear();

        int width  = numWidthNodes  * nodeRadius * nodeRadiusSpacingFactor;
        int height = numHeightNodes * nodeRadius * nodeRadiusSpacingFactor;
        int xInset = 0;
        int yInset = 0;
        int vpWidth  =  (int) problemViewScrollPane.getWidth() - 2;
        int vpHeight =  (int) problemViewScrollPane.getHeight() - 2;
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
                        startNode = null;
                        clicked.setFill(defaultPaint);
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
