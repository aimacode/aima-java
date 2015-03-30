package aima.gui.demo.search.problem.rectangular;

import aima.gui.demo.search.tree.algorithm.TreeSearchAlgoSimulator;
import aima.gui.demo.search.tree.info.rectangular.RectangularStateSpaceInfoController;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
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
    private Vertex[][]  vertexes;
    //
    private int nodeRadius              = 12;
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
    private RectangularStateSpaceInfoController problemStateSpaceController;
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
        problemStateSpaceController.setSimulator(simulator);
    }

    public Pane createSearchSpaceInfoRepresentation() throws IOException {
        FXMLLoader stateSpaceInfoLoader = new FXMLLoader(RectangularStateSpaceInfoController.class.getResource("rectangularstatespaceinfo.fxml"));
        Pane stateSpaceInfoPane = stateSpaceInfoLoader.load();

        problemStateSpaceController = stateSpaceInfoLoader.getController();
        problemStateSpaceController.setProblemController(this);

        return stateSpaceInfoPane;
    }

    public Vertex[][] setupGrid(Pane pane, ScrollPane containingScrollPane, Consumer<Vertex> vertexConsumer) {
        pane.getChildren().clear();

        Vertex[][] nodes = new Vertex[getXDimensionSize()][getYDimensionSize()];

        int width  = getXDimensionSize() * nodeRadius * nodeRadiusSpacingFactor;
        int height = getYDimensionSize() * nodeRadius * nodeRadiusSpacingFactor;
        int xInset = 0;
        int yInset = 0;
        int vpWidth  =  (int) containingScrollPane.getWidth() - viewportPadding;
        int vpHeight =  (int) containingScrollPane.getHeight() - viewportPadding;
        if (width < vpWidth) {
            xInset = (vpWidth - width) / 2;
            pane.setPrefWidth(vpWidth);
        }
        else {
            pane.setPrefWidth(width);
        }
        if (height < vpHeight) {
            yInset = (vpHeight - height) / 2;
            pane.setPrefHeight(vpHeight);
        }
        else {
            pane.setPrefHeight(height);
        }

        for (int x = 0; x < nodes.length; x++) {
            for (int y = 0; y < nodes[x].length; y++) {
                int centerX = xInset + (nodeRadius + borderPadding) + (x * (nodeRadius * nodeRadiusSpacingFactor));
                int centerY = yInset + (nodeRadius + borderPadding) + (y * (nodeRadius * nodeRadiusSpacingFactor));
                nodes[x][y] = new Vertex(x, y, centerX, centerY, nodeRadius);
                nodes[x][y].setFill(defaultPaint);
                nodes[x][y].setStroke(Color.BLACK);
                nodes[x][y].setStrokeWidth(1);

                pane.getChildren().add(nodes[x][y]);

                if (x > 0) {
                    Line horizLine = new Line(centerX - (nodeRadius*(nodeRadiusSpacingFactor-1)), centerY, centerX - nodeRadius, centerY);
                    pane.getChildren().add(horizLine);
                }

                if (y > 0) {
                    Line vertLine = new Line(centerX, centerY - (nodeRadius*(nodeRadiusSpacingFactor-1)), centerX, centerY - nodeRadius);
                    pane.getChildren().add(vertLine);
                }

                vertexConsumer.accept(nodes[x][y]);
            }
        }

        return nodes;
    }

    public void setupProblem() {
        startNode = null;
        goalNodes.clear();

        vertexes = setupGrid(problemViewPane, problemViewScrollPane, vertex -> {
            Tooltip t = new Tooltip("("+vertex.x+","+vertex.y+")");
            Tooltip.install(vertex, t);

            vertex.setOnMouseClicked(me -> {
                Vertex clicked = (Vertex) me.getSource();

                if (startNode == null) {
                    startNode = clicked;
                    startNode.setFill(startPaint);
                    goalNodes.remove(startNode); // Ensure is not a goal
                } else if (startNode != null && clicked == startNode) {
                    if (goalNodes.contains(clicked)) {
                        startNode = null;
                        goalNodes.remove(clicked);
                        clicked.setFill(defaultPaint);
                    } else {
                        goalNodes.add(clicked);
                        clicked.setFill(startGoalPaint);
                    }
                } else {
                    if (goalNodes.contains(clicked)) {
                        clicked.setFill(defaultPaint);
                        goalNodes.remove(clicked);
                    } else {
                        clicked.setFill(goalPaint);
                        goalNodes.add(clicked);
                    }
                }

                if (startNode == null) {
                    simulator.setProblem(null);
                } else {
                    simulator.setProblem(new RectangularProblem(getXDimensionSize(), getYDimensionSize(),
                            new AtVertex(startNode.x, startNode.y),
                            goalNodes.stream().map(v -> new AtVertex(v.x, v.y)).collect(Collectors.toList())));
                }
            });
        });
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
