package aima.gui.demo.search.problem.rectangular;

import javafx.scene.shape.Circle;

/**
 * @author Ciaran O'Reilly
 */
public class Vertex extends Circle {
    public final int x;
    public final int y;
    public Vertex(int x, int y, double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        this.x = x;
        this.y = y;
    }
}
