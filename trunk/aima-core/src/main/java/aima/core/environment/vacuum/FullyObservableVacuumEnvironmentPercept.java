package aima.core.environment.vacuum;

import aima.core.agent.Agent;
import aima.core.agent.Percept;

/**
 * Implements a fully observable environment percept, in accordance with page
 * 134, AIMAv3.
 *
 * @author Andrew Brown
 */
public interface FullyObservableVacuumEnvironmentPercept extends Percept {
	/**
     * Returns the agent location
     *
     * @param a
     * @return
     */
    String getAgentLocation(Agent a);
    
    /**
     * Returns the location state
     *
     * @param location
     * @return
     */
    VacuumEnvironment.LocationState getLocationState(String location);
}
