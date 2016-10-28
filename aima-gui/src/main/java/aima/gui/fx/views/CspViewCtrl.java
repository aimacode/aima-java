package aima.gui.fx.views;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;
import aima.core.util.math.geom.shapes.Point2D;
import javafx.geometry.VPos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Hashtable;
import java.util.List;

/**
 * Controller class which provides functionality to visualize a binary CSP state in a pane.
 *
 * @author Ruediger Lunde
 */
public class CspViewCtrl {
    protected Pane pane;
    protected CSP csp;
    protected Assignment assignment;

    /**
     * Maintains logical 2D-coordinates for the variables of the CSP.
     */
    protected Hashtable<Variable, int[]> positionMapping = new Hashtable<Variable, int[]>();
    /**
     * Maps domain values to colors.
     */
    protected Hashtable<Object, Color> colorMapping = new Hashtable<Object, Color>();

    public CspViewCtrl(StackPane viewRoot) {
        pane = new Pane();
        viewRoot.getChildren().add(pane);
        viewRoot.setStyle("-fx-background-color: white");
        pane.setMinWidth(0.0);
        pane.widthProperty().addListener((obs, o, n) -> update());
        pane.heightProperty().addListener((obs, o, n) -> update());
    }

    public void clearMappings() {
        positionMapping.clear();
        colorMapping.clear();
    }

    /**
     * Defines a logical 2D-position for a variable. If no position is given for
     * a certain variable, the viewer selects a position on a grid.
     */
    public void setPositionMapping(Variable var, int x, int y) {
        positionMapping.put(var, new int[]{x, y});
    }

    /**
     * Defines a color for a domain value. It is used to visualize the current
     * assignment. If no color mapping is found, the node coloring feature is
     * disabled for the corresponding variable.
     *
     * @param value
     * @param color
     */
    public void setColorMapping(Object value, Color color) {
        colorMapping.put(value, color);
    }


    public void initialize(CSP csp) {
        this.csp = csp;
        this.assignment = new Assignment();
        update();
    }

    public void update(CSP csp, Assignment assignment) {
        if (csp != null)
            this.csp = csp;
        if (assignment != null)
            this.assignment = assignment;
        update();
    }

    protected void update() {
        adjustTransform();
        pane.getChildren().clear();
        if (csp != null) {
            for (Constraint cons : csp.getConstraints())
                visualize(cons);
            for (Variable var : csp.getVariables())
                visualize(var);
        }
    }

    protected void visualize(Variable var) {
        Point2D pos = getPosition(var);
        String label = var.getName();
        Object value = null;
        Color fillColor = null;
        if (assignment != null)
            value = assignment.getAssignment(var);
        if (value != null) {
            label += " = " + value;
            fillColor = colorMapping.get(value);
        }
        Circle circle = new Circle(pos.getX(), pos.getY(), 20);
        circle.setStroke(Color.BLACK);
        circle.setFill(fillColor != null ? fillColor : Color.WHITE);
        Text t1 = new Text(pos.getX() + 25, pos.getY(), label);
        t1.setTextOrigin(VPos.CENTER);
        Text t2 = new Text(pos.getX(), pos.getY() + 40, csp.getDomain(var).toString());
        pane.getChildren().addAll(circle, t1, t2);
    }

    protected void visualize(Constraint constraint) {
        List<Variable> scope = constraint.getScope();
        if (scope.size() == 2) { // we show only binary constraints...
            Point2D pos0 = getPosition(scope.get(0));
            Point2D pos1 = getPosition(scope.get(1));
            Line line = new Line(pos0.getX(), pos0.getY(), pos1.getX(), pos1.getY());
            pane.getChildren().add(line);
            //g2.drawLine(pos0[0] + 20, pos0[1] + 20, pos1[0] + 20, pos1[1] + 20);
        }
    }

    /**
     * Computes transforms (translations and scaling) and applies them to the environment state view. Those transforms
     * map logical positions to screen positions in the viewer pane. The purpose is to show the graphical state
     * representation as large as possible.
     *
     * @return The scale value.
     */
    private double adjustTransform() {
        double xMin = Double.POSITIVE_INFINITY;
        double xMax = Double.NEGATIVE_INFINITY;
        double yMin = Double.POSITIVE_INFINITY;
        double yMax = Double.NEGATIVE_INFINITY;
        for (Variable var : csp.getVariables()) {
            Point2D point = getPosition(var);
            xMin = Math.min(xMin, point.getX());
            xMax = Math.max(xMax, point.getX());
            yMin = Math.min(yMin, point.getY());
            yMax = Math.max(yMax, point.getY());
        }
        double scale = Math.min(pane.getWidth() / (xMax - xMin + 300),
                pane.getHeight() / (yMax - yMin + 150));

        pane.setTranslateX((scale * (pane.getWidth() - xMin - xMax) / 2.0));
        pane.setTranslateY((scale * (pane.getHeight() - yMin - yMax) / 2.0));
        pane.setScaleX(scale);
        pane.setScaleY(scale);
        return scale;
    }

    /**
     * Provides a 2D-position for each variable. If no mapping is given, a simple grid position is computed.
     */
    protected Point2D getPosition(Variable var) {
        int[] pos = positionMapping.get(var);
        if (pos != null)
            return new Point2D(pos[0], pos[1]);
        else {
            int vIndex = csp.indexOf(var);
            int rows = Math.max((int) (pane.getHeight() / 100), 1);
            int x = (vIndex / rows) * 160 + 40;
            int y = (vIndex % rows) * 100 + 40;
            return new Point2D(x, y);
        }
    }
}
