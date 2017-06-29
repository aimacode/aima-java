package aima.gui.fx.views;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.Percept;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.VacuumEnvironment.LocationState;
import aima.core.environment.vacuum.VacuumEnvironmentState;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

import java.util.*;

/**
 * Environment view controller class which is specialized for vacuum
 * environments. It adds a vacuum environment state view to the text-based view
 * of the {@link SimpleEnvironmentViewCtrl}.
 *
 * @author Ruediger Lunde
 */
public class VacuumEnvironmentViewCtrl extends AbstractGridEnvironmentViewCtrl {

    private Set<Agent> agentsInSuckState = new HashSet<>();
    private Map<Agent, Integer> agentOrientations = new HashMap<>();

    public VacuumEnvironmentViewCtrl(StackPane viewRoot) {
        super(viewRoot, env -> ((VacuumEnvironment) env).getLocations().size(), env -> 1);
    }

    @Override
    public void initialize(Environment env) {
        agentsInSuckState.clear();
        agentOrientations.clear();
        super.initialize(env);
    }

    @Override
    public void agentActed(Agent agent, Percept percept, Action action, Environment source) {
        if (action == VacuumEnvironment.ACTION_SUCK)
            agentsInSuckState.add(agent);
        else
            agentsInSuckState.remove(agent);
        if (action == VacuumEnvironment.ACTION_MOVE_LEFT)
            agentOrientations.put(agent, 180);
        else if (action == VacuumEnvironment.ACTION_MOVE_RIGHT)
            agentOrientations.put(agent, 0);
        super.agentActed(agent, percept, action, source);
    }

    protected void update() {
        VacuumEnvironment vEnv = ((VacuumEnvironment) env);
        List<String> locations = vEnv.getLocations();
        for (int x = 1; x <= locations.size(); x++) {
            SquareButton btn = getSquareButton(x, 1);
            if (vEnv.getLocationState(locations.get(x-1)).equals(LocationState.Dirty))
                btn.getLabel().setText("Dirty");
            else
                btn.getLabel().setText(""); // "Clean"
            btn.getIdLabel().setText(locations.get((x - 1)));
            btn.getPane().getChildren().clear();
        }
        for (Agent agent : vEnv.getAgents()) {
            SquareButton btn = getSquareButton(locations.indexOf(vEnv.getAgentLocation(agent)) + 1, 1);
            Integer orientation = agentOrientations.get(agent);
            if (orientation == null)
                orientation = 0;
            btn.getPane().getChildren().add(createAgentSymbol(agentsInSuckState.contains(agent), orientation));
        }
    }

    @Override
    protected void onEdit(int x, int y) {
        if (!isEditingEnabled)
            actionLabel.setText("Environment can only be edited after initialization.");
        else {
            VacuumEnvironment vEnv = (VacuumEnvironment) env;
            VacuumEnvironmentState state = (VacuumEnvironmentState) vEnv.getCurrentState();
            String loc = vEnv.getLocations().get(x-1);
            state.setLocationState(loc,
                    state.getLocationState(loc) == LocationState.Clean ? LocationState.Dirty : LocationState.Clean);
            update();
        }
    }



    protected Node createAgentSymbol(boolean suck, int orientation) {
        Arc arc = new Arc();
        arc.radiusXProperty().bind(squareSize.multiply(0.3));
        arc.radiusYProperty().bind(squareSize.multiply(0.3));
        arc.setStartAngle(45.0f);
        arc.setLength(suck ? 360.0f : 270.0f);
        arc.setType(ArcType.ROUND);
        arc.setFill(Color.RED);
        arc.setRotate(orientation);
        return arc;
    }
}
