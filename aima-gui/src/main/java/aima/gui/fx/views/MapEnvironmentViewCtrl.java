package aima.gui.fx.views;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.environment.map.Map;
import aima.core.environment.map.MapEnvironment;
import aima.core.util.datastructure.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
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
	protected Map map;
	protected String goal;
	protected List<String> track;

	public MapEnvironmentViewCtrl(StackPane parent) {
		super(parent);
		envStateView = new Pane();
		splitPane.getItems().add(0, envStateView);
		splitPane.setDividerPosition(0, 0.7);
		splitPane.setStyle("-fx-background-color: white");
		envStateView.setMinWidth(0.0);
		envStateView.widthProperty().addListener((obs, o, n) -> adjustTransform());
		envStateView.heightProperty().addListener((obs, o, n) -> adjustTransform());
		track = new ArrayList<String>();
	}
	
	public void setGoal(String goal) {
		this.goal = goal;
	}

	@Override
	public void initialize(Environment env) {
		if (env instanceof MapEnvironment) {
			map = ((MapEnvironment) env).getMap();
			track.clear();
		}
		super.initialize(env);
	}

	@Override
	protected void updateEnvStateView(Environment env) {
		envStateView.getChildren().clear();
		if (env instanceof MapEnvironment) {
			MapEnvironment mEnv = (MapEnvironment) env;
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
			if (!mEnv.getAgents().isEmpty()) {
				String aLoc = mEnv.getAgentLocation(mEnv.getAgents().get(0));
				if (track.isEmpty() || track.get(track.size()-1) != aLoc)
					track.add(aLoc);
				for (int i = 1; i < track.size(); i++) {
					Point2D pt1 = map.getPosition(track.get(i-1));
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
				envStateView.getChildren().add(new Text(point.getX() + 5, point.getY() + 10, loc));
				envStateView.getChildren().add(new Circle(point.getX(), point.getY(), 2));
			}
			// print agent locations
			for (Agent agent : mEnv.getAgents()) {
				String loc = mEnv.getAgentLocation(agent);
				if (loc != null) {
					Point2D pt = map.getPosition(loc);
					envStateView.getChildren().add(new Circle(pt.getX(), pt.getY(), 6, Color.RED));
				}
			}
			// print goal
			if (goal != null) {
				Point2D pt = map.getPosition(goal);
				envStateView.getChildren().add(new Circle(pt.getX(), pt.getY(), 4, Color.GREEN));
			}
			adjustTransform();
		}
	}

	private void adjustTransform() {
		if (map != null) {
			double xMin = Double.POSITIVE_INFINITY;
			double xMax = Double.NEGATIVE_INFINITY;
			double yMin = Double.POSITIVE_INFINITY;
			double yMax = Double.NEGATIVE_INFINITY;
			for (String loc : map.getLocations()) {
				Point2D point = map.getPosition(loc);
				xMin = Math.min(xMin, point.getX());
				xMax = Math.max(xMax, point.getX());
				yMin = Math.min(yMin, point.getY());
				yMax = Math.max(yMax, point.getY());
			}
			double scale = Math.min((envStateView.getWidth() - 150) / (xMax - xMin),
					(envStateView.getHeight() - 60) / (yMax - yMin));

			envStateView.setScaleY(scale);
			envStateView.setTranslateX((scale * (envStateView.getWidth() - xMin - xMax) / 2.0 - 30));
			envStateView.setTranslateY((scale * (envStateView.getHeight() - yMin - yMax) / 2.0 - 10));
			envStateView.setScaleX(scale);
			envStateView.setScaleY(scale);
		}
	}
}
