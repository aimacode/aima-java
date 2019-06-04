package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.DynamicAction;
import aima.core.agent.impl.DynamicPercept;
import aima.core.search.agent.NondeterministicSearchAgent;
import aima.core.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 58.<br>
 * <br>
 * Let the world contain just two locations. Each location may or may not
 * contain dirt, and the agent may be in one location or the other. There are 8
 * possible world states, as shown in Figure 3.2. The agent has three possible
 * actions in this version of the vacuum world: <em>Left</em>, <em>Right</em>,
 * and <em>Suck</em>. Assume for the moment, that sucking is 100% effective. The
 * goal is to clean up all the dirt.
 *
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class VacuumEnvironment extends AbstractEnvironment<DynamicPercept, Action> {
	// Allowable Actions within the Vacuum Environment
	public static final Action ACTION_MOVE_LEFT = new DynamicAction("Left");
	public static final Action ACTION_MOVE_RIGHT = new DynamicAction("Right");
	public static final Action ACTION_SUCK = new DynamicAction("Suck");
	public static final String LOCATION_A = "A";
	public static final String LOCATION_B = "B";

	public enum LocationState {
		Clean, Dirty
	}

    private final List<String> locations;
	protected VacuumEnvironmentState envState = null;
	protected boolean isDone = false;

	/**
	 * Constructs a vacuum environment with two locations A and B, in which dirt is
	 * placed at random.
	 */
	public VacuumEnvironment() {
		this(Util.randomBoolean() ? LocationState.Clean : LocationState.Dirty,
				Util.randomBoolean() ? LocationState.Clean : LocationState.Dirty);
	}

	/**
	 * Constructs a vacuum environment with two locations A and B, in which dirt is
	 * placed as specified.
	 * 
	 * @param locAState
	 *            the initial state of location A, which is either
	 *            <em>Clean</em> or <em>Dirty</em>.
	 * @param locBState
	 *            the initial state of location B, which is either
	 *            <em>Clean</em> or <em>Dirty</em>.
	 */
	public VacuumEnvironment(LocationState locAState, LocationState locBState) {
		this(Arrays.asList(LOCATION_A, LOCATION_B), locAState, locBState);
	}

	/**
	 * Constructor which allows subclasses to define a vacuum environment with an arbitrary number
	 * of squares. Two-dimensional grid environments can be defined by additionally overriding
	 * {@link #getXDimension()} and {@link #getYDimension()}.
	 */
	protected VacuumEnvironment(List<String> locations, LocationState... locStates) {
		this.locations = locations;
		envState = new VacuumEnvironmentState();
		for (int i = 0; i < locations.size() && i < locStates.length; i++)
			envState.setLocationState(locations.get(i), locStates[i]);
	}

	public List<String> getLocations() {
		return locations;
	}

	public EnvironmentState getCurrentState() {
		return envState;
	}

	public LocationState getLocationState(String location) {
		return envState.getLocationState(location);
	}

	public String getAgentLocation(Agent agent) {
		return envState.getAgentLocation(agent);
	}

	@Override
	public void addAgent(Agent<? super DynamicPercept, ? extends Action> agent) {
		int idx = new Random().nextInt(locations.size());
		envState.setAgentLocation(agent, locations.get(idx));
		super.addAgent(agent);
	}

	public void addAgent(Agent<? super DynamicPercept, ? extends Action> agent, String location) {
		// Ensure the agent state information is tracked before
		// adding to super, as super will notify the registered
		// EnvironmentViews that is was added.
		envState.setAgentLocation(agent, location);
		super.addAgent(agent);
	}

	@Override
	public DynamicPercept getPerceptSeenBy(Agent<?, ?> agent) {
		// Other agents get a local percept.
		DynamicPercept percept = new DynamicPercept();
		String loc = envState.getAgentLocation(agent);
		percept.setAttribute(AttNames.CURRENT_LOCATION, loc);
		percept.setAttribute(AttNames.CURRENT_STATE, envState.getLocationState(loc));
		if (agent instanceof NondeterministicSearchAgent) {
			// This agent expects a fully observable environment.
			percept.setAttribute(AttNames.STATE_LOCATION_A, envState.getLocationState(VacuumEnvironment.LOCATION_A));
			percept.setAttribute(AttNames.STATE_LOCATION_B, envState.getLocationState(VacuumEnvironment.LOCATION_B));
		}
		return percept;
	}

	@Override
	public void execute(Agent<?, ?> agent, Action action) {
		String loc = getAgentLocation(agent);
		if (ACTION_MOVE_RIGHT == action) {
			int x = getX(loc);
			if (x < getXDimension())
				envState.setAgentLocation(agent, getLocation(x + 1, getY(loc)));
			updatePerformanceMeasure(agent, -1);
		} else if (ACTION_MOVE_LEFT == action) {
			int x = getX(loc);
			if (x > 1)
				envState.setAgentLocation(agent, getLocation(x - 1, getY(loc)));
			updatePerformanceMeasure(agent, -1);
		} else if (ACTION_SUCK == action) {
			if (LocationState.Dirty == envState.getLocationState(envState
					.getAgentLocation(agent))) {
				envState.setLocationState(envState.getAgentLocation(agent),
						LocationState.Clean);
				updatePerformanceMeasure(agent, 10);
			}
		}
	}

	@Override
	protected void executeNoOp(Agent<?, ?> agent) {
		// In the Vacuum Environment we consider things done if
		// the agent's act method returns no action.
		isDone = true;
	}

	@Override
	public boolean isDone() {
		return super.isDone() || isDone;
	}


	// Information for grid listeners...

	public int getXDimension() {
		return locations.size();
	}

	public int getYDimension() {
		return 1;
	}

	// 1 means left
	public int getX(String location) {
		return getLocations().indexOf(location) % getXDimension() + 1;
	}

	// 1 means bottom
	public int getY(String location) {
		return getYDimension() - getLocations().indexOf(location) / getXDimension();
	}

	// (1, 1) is bottom left
	public String getLocation(int x, int y) {
		return locations.get((getYDimension() - y) * getXDimension() + x - 1);
	}
}