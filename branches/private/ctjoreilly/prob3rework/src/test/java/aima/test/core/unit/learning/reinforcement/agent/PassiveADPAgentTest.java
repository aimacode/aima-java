package aima.test.core.unit.learning.reinforcement.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.AgentProgram;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.AbstractEnvironment;
import aima.core.agent.impl.NoOpAction;
import aima.core.environment.cellworld.next.Cell;
import aima.core.environment.cellworld.next.CellWorld;
import aima.core.environment.cellworld.next.CellWorldAction;
import aima.core.environment.cellworld.next.CellWorldFactory;
import aima.core.learning.reinforcement.next.PerceptStateReward;
import aima.core.learning.reinforcement.next.agent.PassiveADPAgent;
import aima.core.probability.mdp.next.ActionsFunction;
import aima.core.probability.mdp.next.TransitionProbabilityFunction;
import aima.core.probability.mdp.next.impl.ModifiedPolicyEvaluation;
import aima.core.util.JavaRandomizer;
import aima.core.util.MockRandomizer;
import aima.core.util.Randomizer;

public class PassiveADPAgentTest {
	public static final double DELTA_THRESHOLD = 1e-1;
	//
	private CellWorld<Double> cw = null;
	private CellWorldEnvironment cwe = null;
	private PassiveADPAgent<Cell<Double>, CellWorldAction> padpa = null;

	@Before
	public void setUp() {
		cw = CellWorldFactory.createCellWorldForFig17_1();
		cwe = new CellWorldEnvironment(cw, cw.getCellAt(1, 1),
				new CellWorldTransitionProbabilityFunction(cw),
				// new MockRandomizer(new double[] {0.75, 0.75, 0.75, 0.75,
				// 0.75, 0.75, 0.75, 0.75, 0.85, 0.95,
				// 0.85, 0.95, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75, 0.75})
				new JavaRandomizer());

		Map<Cell<Double>, CellWorldAction> fixedPolicy = new HashMap<Cell<Double>, CellWorldAction>();
		fixedPolicy.put(cw.getCellAt(1, 1), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(1, 2), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(1, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(2, 1), CellWorldAction.Left);
		fixedPolicy.put(cw.getCellAt(2, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(3, 1), CellWorldAction.Left);
		fixedPolicy.put(cw.getCellAt(3, 2), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(3, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(4, 1), CellWorldAction.Left);

		padpa = new PassiveADPAgent<Cell<Double>, CellWorldAction>(fixedPolicy,
				cw.getCells(), cw.getCellAt(1, 1),
				new ActionsFunction<Cell<Double>, CellWorldAction>() {
					private Set<Cell<Double>> terminals = null;

					@Override
					public Set<CellWorldAction> actions(Cell<Double> s) {
						if (null == terminals) {
							terminals = new HashSet<Cell<Double>>();
							terminals.add(cw.getCellAt(4, 3));
							terminals.add(cw.getCellAt(4, 2));
						}
						// All actions can be performed in each cell
						// (except terminal states)
						if (terminals.contains(s)) {
							return Collections.emptySet();
						}
						return CellWorldAction.actions();
					}
				}, new ModifiedPolicyEvaluation<Cell<Double>, CellWorldAction>(
						10, 1.0));
		cwe.addAgent(padpa);
	}

	@Test
	public void test() {
		Map<Cell<Double>, Double> U = null;
		List<Map<Cell<Double>, Double>> runs = new ArrayList<Map<Cell<Double>,Double>>();
		for (int r = 0; r < 20; r++) {
			cwe.executeTrials(100);
			U = padpa.getUtility();
			runs.add(U);
			padpa.reset();
		}
		
		// Calculate the Root Mean Square Error for utility of 1,1
		double xSsquared = 0;
		for (Map<Cell<Double>, Double> u : runs) {
			System.out.println("1,1="+u.get(cw.getCellAt(1, 1)));
			xSsquared += Math.pow(0.705 - u.get(cw.getCellAt(1, 1)), 2);
		}
		double rmse = Math.sqrt(xSsquared / runs.size());
		System.out.println("rmse="+rmse);

		Assert.assertNotNull(U.get(cw.getCellAt(1, 1)));
		// Note:
		// These are not reachable when starting at 1,1 using
		// the policy and default transition model
		// (i.e. 80% intended, 10% each right angle from intended).
		Assert.assertNull(U.get(cw.getCellAt(3, 1)));
		Assert.assertNull(U.get(cw.getCellAt(4, 1)));
		Assert.assertEquals(9, U.size());

		Assert.assertEquals(0.705, U.get(cw.getCellAt(1, 1)), DELTA_THRESHOLD);
		Assert.assertEquals(0.762, U.get(cw.getCellAt(1, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(0.812, U.get(cw.getCellAt(1, 3)), DELTA_THRESHOLD);

		Assert.assertEquals(0.655, U.get(cw.getCellAt(2, 1)), DELTA_THRESHOLD);
		Assert.assertEquals(0.868, U.get(cw.getCellAt(2, 3)), DELTA_THRESHOLD);

		// Assert.assertEquals(0.611, U.get(cw.getCellAt(3, 1)),
		// DELTA_THRESHOLD);
		Assert.assertEquals(0.660, U.get(cw.getCellAt(3, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(0.918, U.get(cw.getCellAt(3, 3)), DELTA_THRESHOLD);

		// Assert.assertEquals(0.388, U.get(cw.getCellAt(4, 1)),
		// DELTA_THRESHOLD);
		Assert.assertEquals(-1.0, U.get(cw.getCellAt(4, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, U.get(cw.getCellAt(4, 3)), DELTA_THRESHOLD);
	}

	class CellWorldEnvironment extends AbstractEnvironment {
		private CellWorld<Double> cw = null;
		private Cell<Double> startingCell = null;
		private TransitionProbabilityFunction<Cell<Double>, CellWorldAction> tpf;
		private Randomizer r = null;
		private CellWorldEnvironmentState currentState = new CellWorldEnvironmentState();

		public CellWorldEnvironment(
				CellWorld<Double> cw,
				Cell<Double> startingCell,
				TransitionProbabilityFunction<Cell<Double>, CellWorldAction> tpf,
				Randomizer r) {
			this.cw = cw;
			this.startingCell = startingCell;
			this.tpf = tpf;
			this.r = r;
		}

		public void executeTrials(int n) {
			for (int i = 0; i < n; i++) {
				executeTrial();
			}
		}

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
				// System.out.println("pc="+probabilityChoice);
				double total = 0;
				for (Cell<Double> sDelta : outcomes(s, (CellWorldAction) action)) {
					total += tpf.probability(sDelta, s,
							(CellWorldAction) action);
					if (probabilityChoice <= total) {
						// System.out.println("s="+s+", a="+action+", s'="+sDelta+", total="+total);
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

		@SuppressWarnings("unchecked")
		private Cell<Double>[] outcomes(Cell<Double> s, CellWorldAction a) {
			// There can be three possible outcomes for the planned action
			Cell<Double>[] outcomes = new Cell[3];

			outcomes[0] = cw.result(s, a);
			outcomes[1] = cw.result(s, a.getFirstRightAngledAction());
			outcomes[2] = cw.result(s, a.getSecondRightAngledAction());

			return outcomes;
		}
	}

	class CellWorldEnvironmentState implements EnvironmentState {
		private Map<Agent, CellWorldPercept> agentLocations = new HashMap<Agent, CellWorldPercept>();

		public CellWorldEnvironmentState() {
		}

		public void reset() {
			agentLocations.clear();
		}

		public void setAgentLocation(Agent anAgent, Cell<Double> location) {
			CellWorldPercept percept = agentLocations.get(anAgent);
			if (null == percept) {
				percept = new CellWorldPercept(location);
				agentLocations.put(anAgent, percept);
			} else {
				percept.setCell(location);
			}
		}

		public Cell<Double> getAgentLocation(Agent anAgent) {
			return agentLocations.get(anAgent).getCell();
		}

		public CellWorldPercept getPerceptFor(Agent anAgent) {
			return agentLocations.get(anAgent);
		}
	}

	class CellWorldPercept implements PerceptStateReward<Cell<Double>> {
		private Cell<Double> cell = null;

		public CellWorldPercept(Cell<Double> cell) {
			this.cell = cell;
		}

		public Cell<Double> getCell() {
			return cell;
		}

		public void setCell(Cell<Double> cell) {
			this.cell = cell;
		}

		@Override
		public double reward() {
			return cell.getContent().doubleValue();
		}

		@Override
		public Cell<Double> state() {
			return cell;
		}
	}

	class CellWorldTransitionProbabilityFunction implements
			TransitionProbabilityFunction<Cell<Double>, CellWorldAction> {
		private CellWorld<Double> cw = null;
		private double[] distribution = new double[] { 0.8, 0.1, 0.1 };

		public CellWorldTransitionProbabilityFunction(CellWorld<Double> cw) {
			this.cw = cw;
		}

		@Override
		public double probability(Cell<Double> sDelta, Cell<Double> s,
				CellWorldAction a) {
			double prob = 0;

			Cell<Double>[] outcomes = outcomes(s, a);
			for (int i = 0; i < outcomes.length; i++) {
				if (sDelta.equals(outcomes[i])) {
					// Note: You have to sum the matches to
					// sDelta as the different actions
					// could have the same effect (i.e.
					// staying in place due to there being
					// no adjacent cells), which increases
					// the probability of the transition for
					// that state.
					prob += distribution[i];
				}
			}

			return prob;
		}

		@SuppressWarnings("unchecked")
		private Cell<Double>[] outcomes(Cell<Double> s, CellWorldAction a) {
			// There can be three possible outcomes for the planned action
			Cell<Double>[] outcomes = new Cell[3];

			outcomes[0] = cw.result(s, a);
			outcomes[1] = cw.result(s, a.getFirstRightAngledAction());
			outcomes[2] = cw.result(s, a.getSecondRightAngledAction());

			return outcomes;
		}
	};
}
