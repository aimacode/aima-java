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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Environment view controller class which is specialized for vacuum
 * environments. It adds a vacuum environment state view to the text-based view
 * of the {@link SimpleEnvironmentViewCtrl}.
 *
 * @author Ruediger Lunde
 */
public class VacuumEnvironmentViewCtrl extends AbstractGridEnvironmentViewCtrl {

    private Set<Agent> agentsInSuckState = new HashSet<>();
    private Map<Agent, Double> agentOrientations = new HashMap<>();
    private Function<Action, Double> actionToOrientationFn;
    private Map<Agent, Arc> agentSymbols = new HashMap<>();

    public VacuumEnvironmentViewCtrl(StackPane viewRoot) {
        this(viewRoot, action -> {
            if (action == VacuumEnvironment.ACTION_MOVE_LEFT) return 270.0;
            else if (action == VacuumEnvironment.ACTION_MOVE_RIGHT) return 90.0;
            else return null;
        });
    }

    /**
     * Constructor with additional functional argument to control the rotation of the agent symbol.
     * @param viewRoot the pane where the button grid is added
     * @param actionToOrientationFn maps actions to direction values.
     *                              Value interpretation: 0 = facing up, 90 = facing right, null = no change.
     */
    public VacuumEnvironmentViewCtrl(StackPane viewRoot, Function<Action, Double> actionToOrientationFn) {
        super(viewRoot, env -> ((VacuumEnvironment) env).getXDimension(),
                env -> ((VacuumEnvironment) env).getYDimension());
        this.actionToOrientationFn = actionToOrientationFn;
    }

    @Override
    public void initialize(Environment env) {
        agentsInSuckState.clear();
        agentOrientations.clear();
        agentSymbols.clear();
        super.initialize(env);
        VacuumEnvironment vEnv = (VacuumEnvironment) env;
        for (String loc : vEnv.getLocations()) {
            SquareButton btn = getSquareButton(vEnv.getX(loc), vEnv.getY(loc));
            btn.getIdLabel().setText(loc);
        }
    }

    @Override
    public void agentActed(Agent agent, Percept percept, Action action, Environment source) {
        if (action == VacuumEnvironment.ACTION_SUCK)
            agentsInSuckState.add(agent);
        else
            agentsInSuckState.remove(agent);
        Double orientation = actionToOrientationFn.apply(action);
        if (orientation != null)
            agentOrientations.put(agent, orientation);
        super.agentActed(agent, percept, action, source);
    }

    @Override
    protected void update() {
        VacuumEnvironment vEnv = ((VacuumEnvironment) env);
        for (String loc : vEnv.getLocations()) {
            SquareButton btn = getSquareButton(vEnv.getX(loc), vEnv.getY(loc));
            if (vEnv.getLocationState(loc).equals(LocationState.Dirty))
                btn.getLabel().setText("Dirty");
            else
                btn.getLabel().setText(""); // "Clean"
            btn.getPane().getChildren().clear();
        }
        for (Agent agent : vEnv.getAgents()) {
            String loc = vEnv.getAgentLocation(agent);
            SquareButton btn = getSquareButton(vEnv.getX(loc), vEnv.getY(loc));
            Double orientation = agentOrientations.get(agent);
            if (orientation == null)
                orientation = 90.0;
            btn.getPane().getChildren().add(getAgentSymbol(agent, orientation));
        }
    }

    @Override
    protected void onEdit(int x, int y) {
        if (!isEditingEnabled)
            perceptLabel.setText("Environment can only be edited after initialization.");
        else {
            VacuumEnvironment vEnv = (VacuumEnvironment) env;
            VacuumEnvironmentState state = (VacuumEnvironmentState) vEnv.getCurrentState();
            String loc = vEnv.getLocation(x, y);
            state.setLocationState(loc,
                    state.getLocationState(loc) == LocationState.Clean ? LocationState.Dirty : LocationState.Clean);
            update();
            perceptLabel.setText("");
        }
    }

    private Node getAgentSymbol(Agent agent, double orientation) {
        Arc result = agentSymbols.get(agent);
        if (result == null) {
            result = new Arc();
            result.radiusXProperty().bind(squareSize.multiply(0.75 / 2));
            result.radiusYProperty().bind(squareSize.multiply(0.75 / 2));
            result.setStartAngle(135.0f);
            result.setType(ArcType.ROUND);
            result.setFill(Color.RED);
            agentSymbols.put(agent, result);
        }

        result.setLength(agentsInSuckState.contains(agent) ? 360.0f : 270.0f);
        result.setRotate(orientation);
        return result;
    }
}
