package aima.gui.demo.search.problem.rectangular;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularGrid {
    public final Vertex[][] vertexes;

    public RectangularGrid(Vertex[][] vertexes) {
        this.vertexes = vertexes;
    }

    public static RectangularGrid setupGrid(int xDimensionSize, int yDimensionSize,
                                            int vertexRadius,  int vertexRadiusSpacingFactor,
                                            int borderPadding,
                                            Pane pane, ScrollPane containingScrollPane,
                                            Consumer<Vertex> vertexConsumer, Consumer<Edge> edgeConsumer) {
        pane.getChildren().clear();

        Vertex[][] vertexes = new Vertex[xDimensionSize][yDimensionSize];

        int width  = xDimensionSize * vertexRadius * vertexRadiusSpacingFactor;
        int height = yDimensionSize * vertexRadius * vertexRadiusSpacingFactor;
        int xInset = 0;
        int yInset = 0;
        int vpWidth  =  (int) containingScrollPane.getViewportBounds().getWidth();
        int vpHeight =  (int) containingScrollPane.getViewportBounds().getHeight();
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

        for (int x = 0; x < vertexes.length; x++) {
            int centerX = xInset + (vertexRadius + borderPadding) + (x * (vertexRadius * vertexRadiusSpacingFactor));
            for (int y = 0; y < vertexes[x].length; y++) {
                int centerY = yInset + (vertexRadius + borderPadding) + (y * (vertexRadius * vertexRadiusSpacingFactor));
                vertexes[x][y] = new Vertex(x, y, centerX, centerY, vertexRadius);
            }
        }

        for (int x = 0; x < vertexes.length; x++) {
            for (int y = 0; y < vertexes[x].length; y++) {

                if (x != vertexes.length-1) {
                    Edge e = new Edge(vertexes[x][y], vertexes[x+1][y]);

                    pane.getChildren().add(e);

                    edgeConsumer.accept(e);
                }

                if (y != vertexes[x].length-1) {
                    Edge e = new Edge(vertexes[x][y], vertexes[x][y+1]);

                    pane.getChildren().add(e);

                    edgeConsumer.accept(e);
                }

                pane.getChildren().add(vertexes[x][y]);

                vertexConsumer.accept(vertexes[x][y]);
            }
        }

        return new RectangularGrid(vertexes);
    }
}
