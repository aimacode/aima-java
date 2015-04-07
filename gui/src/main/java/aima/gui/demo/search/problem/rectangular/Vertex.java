package aima.gui.demo.search.problem.rectangular;

import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 */
public class Vertex extends Circle {
    public final int x;
    public final int y;

    private List<Edge> edges = new ArrayList<>();

    public Vertex(int x, int y, double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        this.x = x;
        this.y = y;
    }

    public void addEdge(Edge edge) {
       edges.add(edge);
    }

    public List<Edge> getEdges() {
        return edges;
    }
}
