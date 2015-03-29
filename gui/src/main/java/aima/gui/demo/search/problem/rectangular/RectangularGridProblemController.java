package aima.gui.demo.search.problem.rectangular;

import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.shape.Line;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularGridProblemController implements TreeSearchAlgoSimulator.Observer<AtVertex> {
    @FXML private Label  problemTypeLabel;
    @FXML private Button optionsButton;
    @FXML private Button listProblemsButton;
    @FXML private ScrollPane problemViewScrollPane;
    @FXML private Pane problemViewPane;

    //
    private static final String _iconSize = "16px";
    //
    private Vertex      startNode = null;
    private Set<Vertex> goalNodes = new HashSet<>();
    private Vertex[][]  nodes;
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
    //
    private TreeSearchAlgoSimulator<AtVertex> simulator;
    //
    private IntegerProperty xDimensionSize  = new SimpleIntegerProperty(5);
    private IntegerProperty yDimensionSize = new SimpleIntegerProperty(5);

    public int getXDimensionSize() {
        return xDimensionSize.get();
    }

    public IntegerProperty xDimensionSizeProperty() {
        return xDimensionSize;
    }

    public void setXDimensionSize(int xDimensionSize) {
        this.xDimensionSize.set(xDimensionSize);
    }


    public int getYDimensionSize() {
        return yDimensionSize.get();
    }

    public IntegerProperty yDimensionSizeProperty() {
        return yDimensionSize;
    }

    public void setYDimensionSize(int yDimensionSize) {
        this.yDimensionSize.set(yDimensionSize);
    }

    @Override
    public void setSimulator(TreeSearchAlgoSimulator<AtVertex> simulator) {
        this.simulator = simulator;
    }

    public void setupProblem() {
        // Ensure is clear of children on each setup
        problemViewPane.getChildren().clear();
        startNode = null;
        goalNodes.clear();

        int width  = getXDimensionSize()  * nodeRadius * nodeRadiusSpacingFactor;
        int height = getYDimensionSize() * nodeRadius * nodeRadiusSpacingFactor;
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

        nodes = new Vertex[getXDimensionSize()][getYDimensionSize()];
        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                int centerX = xInset + (nodeRadius + borderPadding) + (x * (nodeRadius * nodeRadiusSpacingFactor));
                int centerY = yInset + (nodeRadius + borderPadding) + (y * (nodeRadius * nodeRadiusSpacingFactor));
                nodes[x][y] = new Vertex(x, y, centerX, centerY, nodeRadius);
                nodes[x][y].setFill(defaultPaint);
                nodes[x][y].setStroke(Color.BLACK);
                nodes[x][y].setStrokeWidth(1);

                Tooltip t = new Tooltip("("+x+","+y+")");
                Tooltip.install(nodes[x][y], t);

                nodes[x][y].setOnMouseClicked(me -> {
                    Vertex clicked = (Vertex) me.getSource();

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

                    if (startNode == null) {
                        simulator.setProblem(null);
                    }
                    else {
                        simulator.setProblem(new RectangularProblem(getXDimensionSize(), getYDimensionSize(),
                                new AtVertex(startNode.x, startNode.y),
                                goalNodes.stream().map(v -> new AtVertex(v.x, v.y)).collect(Collectors.toList())));
                    }
                });

                problemViewPane.getChildren().add(nodes[x][y]);

                if (x > 0) {
                    Line horizLine = new Line(centerX - (nodeRadius*(nodeRadiusSpacingFactor-1)), centerY, centerX - nodeRadius, centerY);
                    problemViewPane.getChildren().add(horizLine);
                }

                if (y > 0) {
                    Line vertLine = new Line(centerX, centerY - (nodeRadius*(nodeRadiusSpacingFactor-1)), centerX, centerY - nodeRadius);
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
