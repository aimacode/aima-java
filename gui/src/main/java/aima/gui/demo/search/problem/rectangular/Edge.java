package aima.gui.demo.search.problem.rectangular;

import javafx.scene.shape.Line;

/**
 * @author Ciaran O'Reilly
 */
public class Edge extends Line {

    public final Vertex vertex1;
    public final Vertex vertex2;

    public Edge(Vertex vertex1, Vertex vertex2) {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;

        vertex1.addEdge(this);
        vertex2.addEdge(this);

        if (vertex1.x != vertex2.x) {
            // V1 <-> V2
            if (vertex1.x < vertex2.x) {
                setStartX(vertex1.getCenterX() + vertex1.getRadius());
                setEndX(vertex2.getCenterX() - vertex2.getRadius());
            }
            else { // V2 <-> V1
                setStartX(vertex2.getCenterX() + vertex2.getRadius());
                setEndX(vertex1.getCenterX() - vertex1.getRadius());
            }
            setStartY(vertex1.getCenterY());
            setEndY(vertex1.getCenterY());
        }
        else {
            setStartX(vertex1.getCenterX());
            setEndX(vertex1.getCenterX());
            // Same column so one is above or below the other
            if (vertex1.y < vertex2.y) {
                setStartY(vertex1.getCenterY()+vertex1.getRadius());
                setEndY(vertex2.getCenterY()- vertex2.getRadius());
            }
            else {
                setStartY(vertex2.getCenterY()+vertex2.getRadius());
                setEndY(vertex1.getCenterY()- vertex1.getRadius());
            }
        }
    }
}
