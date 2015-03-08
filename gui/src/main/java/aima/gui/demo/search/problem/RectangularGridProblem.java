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
public class RectangularGridProblem {
    @FXML private Label  problemTypeLabel;
    @FXML private Button listProblemsButton;
    @FXML private Button optionsButton;
    @FXML private ScrollPane problemViewScrollPane;
    @FXML private Pane problemViewPane;

    private int numWidthNodes  = 12;
    private int numHeightNodes = 14;
    private Circle[][] nodes;
    private int nodeRadius = 10;
    private int borderPadding = 10;

    @FXML
    private void initialize() {
        problemViewScrollPane.layout();

        int width  = numWidthNodes * nodeRadius * 4;
        int height = numHeightNodes * nodeRadius * 4;
        int xInset = 0;
        int yInset = 0;
        int vpWidth  =  (int) problemViewScrollPane.getWidth();
        int vpHeight =  (int) problemViewScrollPane.getHeight();
        if (width < vpWidth) {
            xInset = (int) (width - vpWidth) / 2;
        }
        if (height < vpHeight) {
            yInset = (int) (height - vpHeight) / 2;
        }

        problemViewScrollPane.setPannable(true);
 System.out.println("w"+problemViewPane.getWidth());
 System.out.println("h"+problemViewPane.getHeight());

 System.out.println("width="+width+", vpwidth="+vpWidth+", xInset="+xInset);
 System.out.println("height="+height+", vpheight="+vpHeight+", yInset="+yInset);

        problemViewPane.setMinWidth(width);
        problemViewPane.setMinHeight(height);

        nodes = new Circle[numWidthNodes][numHeightNodes];
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                int x = xInset + (nodeRadius + borderPadding) + (i * (nodeRadius * 4));
                int y = yInset + (nodeRadius + borderPadding) + (j * (nodeRadius * 4));
                nodes[i][j] = new Circle(x, y, nodeRadius);
                nodes[i][j].setFill(null);
                nodes[i][j].setStroke(Color.BLACK);
                nodes[i][j].setStrokeWidth(1);
                problemViewPane.getChildren().add(nodes[i][j]);

                if (i > 0) {
                    Line horizLine = new Line(x - (nodeRadius*3), y, x - nodeRadius, y);
                    problemViewPane.getChildren().add(horizLine);
                }

                if (j > 0) {
                    Line vertLine = new Line(x, y - (nodeRadius*3), x, y - nodeRadius);
                    problemViewPane.getChildren().add(vertLine);
                }
            }
        }

        problemViewScrollPane.layout();
    }
}
