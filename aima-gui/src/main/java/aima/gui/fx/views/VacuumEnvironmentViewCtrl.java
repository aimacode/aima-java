package aima.gui.fx.views;

import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.VacuumEnvironment.LocationState;
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
 * environments. It adds a vacuum environment state view to the text-based view
 * of the {@link SimpleEnvironmentViewCtrl}.
 * 
 * @author Ruediger Lunde
 */
public class VacuumEnvironmentViewCtrl extends SimpleEnvironmentViewCtrl {

	protected GridPane envStateView;
	protected List<String> locations;
	private Agent agentInAction;

	public VacuumEnvironmentViewCtrl(StackPane viewRoot) {
		super(viewRoot);
		envStateView = new GridPane();
		envStateView.setHgap(20);
		RowConstraints rowCons = new RowConstraints();
		rowCons.setPercentHeight(100.0);
		envStateView.getRowConstraints().add(rowCons);
		envStateView.setPadding(new Insets(10, 10, 10, 10));
		splitPane.getItems().add(0, envStateView);
		splitPane.setDividerPosition(0, 0.7);
		envStateView.setMinWidth(0.0);
	}

	@Override
	public void initialize(Environment env) {
		if (env instanceof VacuumEnvironment) {
			this.locations = ((VacuumEnvironment) env).getLocations();
			envStateView.getChildren().clear();
			envStateView.getColumnConstraints().clear();
			ColumnConstraints colCons = new ColumnConstraints();
			colCons.setPercentWidth(100.0 / locations.size());
			int i = 0;
			for (String loc : locations) {
				BorderPane pane = new BorderPane();
				pane.setTop(new Label(loc));
				pane.setStyle("-fx-background-color: white");
				envStateView.add(pane, i++, 0);
				envStateView.getColumnConstraints().add(colCons);
			}
		}
		super.initialize(env);
	}

	@Override
	public void agentActed(Agent agent, Action action, Environment source) {
		agentInAction = (action == VacuumEnvironment.ACTION_SUCK) ? agent : null;
		super.agentActed(agent, action, source);
	}

	@Override
	protected void updateEnvStateView(Environment env) {
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

	private BorderPane getLocPane(String location) {
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
