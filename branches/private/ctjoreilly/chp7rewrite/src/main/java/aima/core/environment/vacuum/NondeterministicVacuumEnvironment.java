package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;

/**
 * Create the erratic vacuum world from page 134, AIMA3e. In the erratic vacuum
 * world, the Suck action works as follows: 1) when applied to a dirty square
 * the action cleans the square and sometimes cleans up dirt in an adjacent
 * square too; 2) when applied to a clean square the action sometimes deposits
 * dirt on the carpet.
 *
 * @author Andrew Brown
 */
public class NondeterministicVacuumEnvironment extends VacuumEnvironment {

    /**
     * Construct a vacuum environment with two locations, in which dirt is
     * placed at random.
     */
    public NondeterministicVacuumEnvironment() {
        super();
    }

    /**
     * Construct a vacuum environment with two locations, in which dirt is
     * placed as specified.
     *
     * @param locAState the initial state of location A, which is either
     * <em>Clean</em> or <em>Dirty</em>.
     * @param locBState the initial state of location B, which is either
     * <em>Clean</em> or <em>Dirty</em>.
     */
    public NondeterministicVacuumEnvironment(LocationState locAState, LocationState locBState) {
        super(locAState, locBState);
    }

    /**
     * Execute the agent action
     *
     * @param a
     * @param agentAction
     * @return the environment state after the action is executed.
     */
    @Override
    public EnvironmentState executeAction(Agent a, Action agentAction) {
        if (ACTION_MOVE_RIGHT == agentAction) {
            envState.setAgentLocation(a, LOCATION_B);
            updatePerformanceMeasure(a, -1);
        } else if (ACTION_MOVE_LEFT == agentAction) {
            envState.setAgentLocation(a, LOCATION_A);
            updatePerformanceMeasure(a, -1);
        } else if (ACTION_SUCK == agentAction) {
            // case: square is dirty
            if (VacuumEnvironment.LocationState.Dirty == envState.getLocationState(envState.getAgentLocation(a))) {
                String current_location = envState.getAgentLocation(a);
                String adjacent_location = (current_location.equals("A")) ? "B" : "A";
                // always clean current square
                envState.setLocationState(current_location, VacuumEnvironment.LocationState.Clean);
                // possibly clean adjacent square
                if (Math.random() > 0.5) {
                    envState.setLocationState(adjacent_location, VacuumEnvironment.LocationState.Clean);
                }
            } // case: square is clean
            else if (VacuumEnvironment.LocationState.Clean == envState.getLocationState(envState.getAgentLocation(a))) {
                // possibly dirty current square
                if (Math.random() > 0.5) {
                    envState.setLocationState(envState.getAgentLocation(a), VacuumEnvironment.LocationState.Dirty);
                }
            }
        } else if (agentAction.isNoOp()) {
            isDone = true;
        }
        return envState;
    }
}
