package aima.core.environment.vacuum;

import aima.core.agent.Percept;
import aima.core.search.framework.PerceptToStateFunction;

/**
 * Map fully observable state percepts to their corresponding state
 * representation.
 * 
 * @author Andrew Brown
 */
public class FullyObservableVacuumEnvironmentPerceptToStateFunction implements
		PerceptToStateFunction {

	/**
	 * Default Constructor.
	 */
	public FullyObservableVacuumEnvironmentPerceptToStateFunction() {

	}

	@Override
	public Object getState(Percept p) {
		// Note: VacuumEnvironmentState implements
		// FullyObservableVacuumEnvironmentPercept
		return (VacuumEnvironmentState) p;
	}
}
