package aima.gui.demo.search.problem.rectangular;

import javafx.scene.shape.Circle;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class Vertex extends Circle {
    public final int x;
    public final int y;

    private List<Edge> edges             = new ArrayList<>();
    private Map<Vertex, Edge> neighbours = new LinkedHashMap<>();

    public Vertex(int x, int y, double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        this.x = x;
        this.y = y;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
        if (edge.vertex1 != this) {
            neighbours.put(edge.vertex1, edge);
        }
        if (edge.vertex2 != this) {
            neighbours.put(edge.vertex2, edge);
        }
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Edge getEdgeFor(Vertex neighbour) {
        return neighbours.get(neighbour);
    }

    public Set<Vertex> getNeighbours() {
        return neighbours.keySet();
    }

    @Override
    public String toString() {
        return "Vertex@"+x+":"+y;
    }
}
