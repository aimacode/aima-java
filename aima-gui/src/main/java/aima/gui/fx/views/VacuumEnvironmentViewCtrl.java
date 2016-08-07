package aima.gui.fx.views;

import java.util.Arrays;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.VacuumEnvironment.LocationState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

/**
 * Environment view controller class which is specialized for vacuum
 * environments. Adds a vacuum environment state view to the text-based view of
 * the {@link UniversalEnvironmentViewCtrl}.
 * 
 * @author Ruediger Lunde
 */
public class VacuumEnvironmentViewCtrl extends UniversalEnvironmentViewCtrl {

	protected GridPane envStateView;
	protected List<String> locations;
	private Agent agentInAction;

	public VacuumEnvironmentViewCtrl(StackPane parent) {
		super(parent);
		envStateView = new GridPane();
		envStateView.setHgap(20);
		RowConstraints rowCons = new RowConstraints();
		rowCons.setPercentHeight(100.0);
		envStateView.getRowConstraints().add(rowCons);
		envStateView.setPadding(new Insets(10, 10, 10, 10));
		splitPane.getItems().add(0, envStateView);
		splitPane.setDividerPosition(0, 0.7);
	}

	public void setLocations(String... locations) {
		this.locations = Arrays.asList(locations);
		envStateView.getChildren().clear();
		envStateView.getColumnConstraints().clear();
		ColumnConstraints colCons = new ColumnConstraints();
		colCons.setPercentWidth(100.0 / locations.length);
		int i = 0;
		for (String loc : locations) {
			BorderPane pane = new BorderPane();
			pane.setTop(new Label(loc));
			pane.setStyle("-fx-background-color: white");
			envStateView.add(pane, i++, 0);
			envStateView.getColumnConstraints().add(colCons);
		}
	}

	@Override
	public void agentAdded(Agent agent, Environment source) {
		super.agentAdded(agent, source);
		Platform.runLater(() -> update(source));
	}

	@Override
	public void agentActed(Agent agent, Action action, Environment source) {
		super.agentActed(agent, action, source);
		agentInAction = (action == VacuumEnvironment.ACTION_SUCK) ? agent : null;
		Platform.runLater(() -> update(source));
	}

	protected void update(Environment env) {
		if (env instanceof VacuumEnvironment) {
			VacuumEnvironment vEnv = (VacuumEnvironment) env;
			for (String loc : locations) {
				BorderPane pane = getLocPane(loc);
				if (vEnv.getLocationState(loc).equals(LocationState.Dirty))
					pane.setStyle("-fx-background-color: lightgrey");
				else
					pane.setStyle("-fx-background-color: white");
				pane.setCenter(null);
			}
			for (Agent agent : vEnv.getAgents()) {
				BorderPane pane = getLocPane(vEnv.getAgentLocation(agent));
				pane.setCenter(createAgentRep(agent == agentInAction));
			}
		}
	}

	protected BorderPane getLocPane(String location) {
		int idx = locations.indexOf(location);
		return (BorderPane) envStateView.getChildren().get(idx);
	}

	protected Node createAgentRep(boolean suck) {
		Arc arc = new Arc();
		arc.setRadiusX(0.3 * envStateView.getWidth() / locations.size());
		arc.setRadiusY(0.3 * envStateView.getWidth() / locations.size());
		arc.setStartAngle(45.0f);
		arc.setLength(suck ? 360.0f : 270.0f);
		arc.setType(ArcType.ROUND);
		arc.setFill(Color.RED);
		return arc;
	}
}
