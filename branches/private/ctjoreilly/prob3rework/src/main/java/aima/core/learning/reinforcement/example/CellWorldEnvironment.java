package aima.core.learning.reinforcement.example;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldPossibleOutcomesFunction;
import aima.core.probability.mdp.TransitionProbabilityFunction;
import aima.core.util.Randomizer;

/**
 * Implementation of the Cell World Environment, supporting the execution of
 * trials for reinforcement learning agents.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public class CellWorldEnvironment extends AbstractEnvironment {
	private Cell<Double> startingCell = null;
	private CellWorldPossibleOutcomesFunction<Double> cwpof;
	private TransitionProbabilityFunction<Cell<Double>, CellWorldAction> tpf;
	private Randomizer r = null;
	private CellWorldEnvironmentState currentState = new CellWorldEnvironmentState();

	/**
	 * Constructor.
	 * 
	 * @param startingCell
	 *            the cell that agent(s) are to start from at the beginning of
	 *            each trial within the environment.
	 * @param cwpof
	 *            the cell world possible outcomes function, which list possible
	 *            outcomes for a particular action within a cell. This works in
	 *            unison with the transition probability function.
	 * @param tpf
	 *            the transition probability function that simulates how the
	 *            environment is meant to behave in response to an agent action.
	 * @param r
	 *            a Randomizer used to sample actions that are actually to be
	 *            executed based on the transition probabilities for actions.
	 */
	public CellWorldEnvironment(Cell<Double> startingCell,
			CellWorldPossibleOutcomesFunction<Double> cwpof,
			TransitionProbabilityFunction<Cell<Double>, CellWorldAction> tpf,
			Randomizer r) {
		this.startingCell = startingCell;
		this.cwpof = cwpof;
		this.tpf = tpf;
		this.r = r;
	}

	/**
	 * Execute N trials.
	 * 
	 * @param n
	 *            the number of trials to execute.
	 */
	public void executeTrials(int n) {
		for (int i = 0; i < n; i++) {
			executeTrial();
		}
	}

	/**
	 * Execute a single trial.
	 */
	public void executeTrial() {
		currentState.reset();
		for (Agent a : agents) {
			a.setAlive(true);
			currentState.setAgentLocation(a, startingCell);
		}
		stepUntilDone();
	}

	@Override
	public EnvironmentState getCurrentState() {
		return currentState;
	}

	@Override
	public EnvironmentState executeAction(Agent agent, Action action) {
		if (!action.isNoOp()) {
			Cell<Double> s = currentState.getAgentLocation(agent);
			double probabilityChoice = r.nextDouble();
			double total = 0;
			for (Cell<Double> sDelta : cwpof.possibleOutcomes(s,
					(CellWorldAction) action)) {
				total += tpf.probability(sDelta, s, (CellWorldAction) action);
				if (probabilityChoice <= total) {
					currentState.setAgentLocation(agent, sDelta);
					break;
				}
			}
		}

		return currentState;
	}

	@Override
	public Percept getPerceptSeenBy(Agent anAgent) {
		return currentState.getPerceptFor(anAgent);
	}
}