package aima.core.probability.example;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import aima.core.environment.cellworld.next.Cell;
import aima.core.environment.cellworld.next.CellWorld;
import aima.core.environment.cellworld.next.CellWorldAction;
import aima.core.probability.mdp.next.ActionsFunction;
import aima.core.probability.mdp.next.MarkovDecisionProcess;
import aima.core.probability.mdp.next.RewardFunction;
import aima.core.probability.mdp.next.TransitionProbabilityFunction;
import aima.core.probability.mdp.next.impl.MDP;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class MDPFactory {

	public static MarkovDecisionProcess<Cell<Double>, CellWorldAction> createMDPForFigure17_3(
			final CellWorld<Double> cw) {
		final Set<Cell<Double>> terminals = new HashSet<Cell<Double>>();
		terminals.add(cw.getCellAt(4, 3));
		terminals.add(cw.getCellAt(4, 2));
		ActionsFunction<Cell<Double>, CellWorldAction> af = new ActionsFunction<Cell<Double>, CellWorldAction>() {

			@Override
			public Set<CellWorldAction> actions(Cell<Double> s) {
				// All actions can be performed in each cell 
				// (except terminal states)
				if (terminals.contains(s)) {
					return Collections.emptySet();
				}
				return CellWorldAction.actions();
			}
		};

		TransitionProbabilityFunction<Cell<Double>, CellWorldAction> tf = new TransitionProbabilityFunction<Cell<Double>, CellWorldAction>() {
			private double[] distribution = new double[] { 0.8, 0.1, 0.1 };

			@Override
			public double probability(Cell<Double> sDelta, Cell<Double> s,
					CellWorldAction a) {
				double prob = 0;

				Cell<Double>[] outcomes = outcomes(sDelta, s, a);
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
			private Cell<Double>[] outcomes(Cell<Double> sDelta,
					Cell<Double> s, CellWorldAction a) {
				// There can be three possible outcomes for the planned action
				Cell<Double>[] outcomes = new Cell[3];

				outcomes[0] = cw.result(s, a);
				outcomes[1] = cw.result(s, a.getFirstRightAngledAction());
				outcomes[2] = cw.result(s, a.getSecondRightAngledAction());

				return outcomes;
			}
		};

		RewardFunction<Cell<Double>> rf = new RewardFunction<Cell<Double>>() {
			@Override
			public double reward(Cell<Double> s) {
				return s.getContent();
			}
		};

		return new MDP<Cell<Double>, CellWorldAction>(cw.getCells(), cw
				.getCellAt(1, 1), af, tf, rf);
	}
}
