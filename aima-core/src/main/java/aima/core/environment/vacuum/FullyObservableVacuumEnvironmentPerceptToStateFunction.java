package aima.core.environment.vacuum;

import aima.core.agent.Percept;

import java.util.function.Function;

/**
 * Map fully observable state percepts to their corresponding state
 * representation.
 * 
 * @author Andrew Brown
 */
public class FullyObservableVacuumEnvironmentPerceptToStateFunction implements Function<Percept, Object> {

	/**
	 * Default Constructor.
	 */
	public FullyObservableVacuumEnvironmentPerceptToStateFunction() {
	}

	@Override
	public Object apply(Percept p) {
		// Note: VacuumEnvironmentState implements
		// FullyObservableVacuumEnvironmentPercept
		return (VacuumEnvironmentState) p;
	}
}
