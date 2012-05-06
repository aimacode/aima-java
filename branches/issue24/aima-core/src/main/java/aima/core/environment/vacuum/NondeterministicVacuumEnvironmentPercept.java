package aima.core.environment.vacuum;

import aima.core.agent.Percept;

/**
 * Implements a fully observable environment percept, in accordance with page
 * 134, AIMAv3.
 *
 * @author Andrew Brown
 */
public class NondeterministicVacuumEnvironmentPercept implements Percept {

    VacuumEnvironmentState state;

    /**
     * Constructor
     *
     * @param state
     */
    public NondeterministicVacuumEnvironmentPercept(VacuumEnvironmentState state) {
        this.state = state;
    }

    /**
     * Return state; fully observable.
     *
     * @return
     */
    public VacuumEnvironmentState getState() {
        return this.state;
    }
}
