package aima.gui.fx.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.environment.map.Map;
import aima.core.environment.map.MapEnvironment;
import aima.core.util.math.geom.shapes.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Environment view controller class which is specialized for map environments.
 * It adds a map environment state view to the text-based view of the
 * {@link SimpleEnvironmentViewCtrl}.
 * 
 * @author Ruediger Lunde
 */
public class MapEnvironmentViewCtrl extends SimpleEnvironmentViewCtrl {

	protected Pane envStateView;
	protected MapEnvironment env;
	protected String goal;
	protected List<String> track;

	public MapEnvironmentViewCtrl(StackPane viewRoot) {
		super(viewRoot);
		envStateView = new Pane();
		splitPane.getItems().add(0, envStateView);
		splitPane.setDividerPosition(0, 0.7);
		splitPane.setStyle("-fx-background-color: white");
		envStateView.setMinWidth(0.0);
		envStateView.widthProperty().addListener((obs, o, n) -> update());
		envStateView.heightProperty().addListener((obs, o, n) -> update());
		track = new ArrayList<String>();
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	@Override
	public void initialize(Environment env) {
		if (env instanceof MapEnvironment)
			this.env = (MapEnvironment) env;
		track.clear();
		super.initialize(env);
	}

	@Override
	protected void updateEnvStateView(Environment env) {
		update();
	}

	protected void update() {
		envStateView.getChildren().clear();
		if (env != null) {
			double scale = adjustTransform();
			Map map = env.getMap();
			// print connections
			for (String loc1 : map.getLocations()) {
				Point2D pt1 = map.getPosition(loc1);
				for (String loc2 : map.getPossibleNextLocations(loc1)) {
					Point2D pt2 = map.getPosition(loc2);
					Shape line = new Line(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY());
					line.setStroke(Color.LIGHTGRAY);
					envStateView.getChildren().add(line);
				}
			}
			// print track of first agent
			if (!env.getAgents().isEmpty()) {
				String aLoc = env.getAgentLocation(env.getAgents().get(0));
				if (track.isEmpty() || !Objects.equals(track.get(track.size() - 1), aLoc))
					track.add(aLoc);
				for (int i = 1; i < track.size(); i++) {
					Point2D pt1 = map.getPosition(track.get(i - 1));
					Point2D pt2 = map.getPosition(track.get(i));
					Shape line = new Line(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY());
					line.setStroke(Color.RED);
					line.setStrokeWidth(2);
					envStateView.getChildren().add(line);
				}
			}
			// print locations
			for (String loc : map.getLocations()) {
				Point2D point = map.getPosition(loc);
				Text text = new Text(point.getX() + 10 / scale, point.getY(), loc);
				text.setFont(new Font(12.0 / scale));
				envStateView.getChildren().add(text);
				envStateView.getChildren().add(new Circle(point.getX(), point.getY(), 2 / scale));
			}
			// print agent locations
			for (Agent agent : env.getAgents()) {
				String loc = env.getAgentLocation(agent);
				if (loc != null) {
					Point2D pt = map.getPosition(loc);
					envStateView.getChildren().add(new Circle(pt.getX(), pt.getY(), 8 / scale, Color.RED));
				}
			}
			// print goal
			if (goal != null) {
				Point2D pt = map.getPosition(goal);
				envStateView.getChildren().add(new Circle(pt.getX(), pt.getY(), 6 / scale, Color.GREEN));
			}
		}
	}

	/**
	 * Computes transforms (translations and scaling) and applies them to the
	 * environment state view. Those transforms map location positions in the
	 * map to screen positions in the viewer pane.
	 * @return The scale value.
	 */
	private double adjustTransform() {
		double xMin = Double.POSITIVE_INFINITY;
		double xMax = Double.NEGATIVE_INFINITY;
		double yMin = Double.POSITIVE_INFINITY;
		double yMax = Double.NEGATIVE_INFINITY;
		for (String loc : env.getMap().getLocations()) {
			Point2D point = env.getMap().getPosition(loc);
			xMin = Math.min(xMin, point.getX());
			xMax = Math.max(xMax, point.getX());
			yMin = Math.min(yMin, point.getY());
			yMax = Math.max(yMax, point.getY());
		}
		double scale = Math.min((envStateView.getWidth() - 150) / (xMax - xMin),
				(envStateView.getHeight() - 60) / (yMax - yMin));

		envStateView.setTranslateX((scale * (envStateView.getWidth() - xMin - xMax) / 2.0 - 30));
		envStateView.setTranslateY((scale * (envStateView.getHeight() - yMin - yMax) / 2.0 - 10));
		envStateView.setScaleX(scale);
		envStateView.setScaleY(scale);
		return scale;
	}
}
