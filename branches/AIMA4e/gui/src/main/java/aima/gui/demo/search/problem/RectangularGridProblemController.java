package aima.gui.demo.search.problem;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularGridProblemController {
    @FXML private Label  problemTypeLabel;
    @FXML private Button listProblemsButton;
    @FXML private Button optionsButton;
    @FXML private ScrollPane problemViewScrollPane;
    @FXML private Pane problemViewPane;

    private Circle[][] nodes;
    //
    private int numWidthNodes  = 5;
    private int numHeightNodes = 5;
    //
    private int nodeRadius              = 8;
    private int borderPadding           = 10;
    private int nodeRadiusSpacingFactor = 5;

    @FXML
    private void initialize() {
        // Ensure the initial preferred width and height
        // dimensions are available fo calculating padding
        problemViewPane.autosize();

        int width  = numWidthNodes  * nodeRadius * nodeRadiusSpacingFactor;
        int height = numHeightNodes * nodeRadius * nodeRadiusSpacingFactor;
        int xInset = 0;
        int yInset = 0;
        int vpWidth  =  (int) problemViewPane.getWidth();
        int vpHeight =  (int) problemViewPane.getHeight();
        if (width < vpWidth) {
            xInset = (vpWidth - width) / 2;
        }
        else {
            problemViewPane.setPrefWidth(width);
        }
        if (height < vpHeight) {
            yInset = (vpHeight - height) / 2;
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
                nodes[i][j].setFill(null);
                nodes[i][j].setStroke(Color.BLACK);
                nodes[i][j].setStrokeWidth(1);
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

        problemViewScrollPane.layout();
    }
}
