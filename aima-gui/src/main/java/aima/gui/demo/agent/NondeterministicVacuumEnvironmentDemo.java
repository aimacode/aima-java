package aima.gui.demo.agent;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleActionTracker;
import aima.core.environment.vacuum.*;
import aima.core.environment.vacuum.VacuumEnvironment.*;
import aima.core.search.agent.NondeterministicSearchAgent;
import aima.core.search.nondeterministic.NondeterministicProblem;

/**
 * Applies AND-OR-GRAPH-SEARCH to a non-deterministic version of the Vacuum World.
 * @author Andrew Brown
 * @author Ruediger Lunde
 */
public class NondeterministicVacuumEnvironmentDemo {
	public static void main(String[] args) {
		System.out.println("NON-DETERMINISTIC-VACUUM-ENVIRONMENT DEMO");
		System.out.println("");
		startAndOrSearch();
	}

	private static void startAndOrSearch() {
		System.out.println("AND-OR-GRAPH-SEARCH");

		// create environment and agent
		VacuumEnvironment world = new NondeterministicVacuumEnvironment(LocationState.Dirty, LocationState.Dirty);
		NondeterministicSearchAgent<VacuumPercept, VacuumEnvironmentState, Action> agent =
				new NondeterministicSearchAgent<>(VacuumWorldFunctions::getState);
		world.addAgent(agent, VacuumEnvironment.LOCATION_A);
		// add a listener to the environment to store executed actions
		SimpleActionTracker actionTracker = new SimpleActionTracker();
		world.addEnvironmentListener(actionTracker);

		// provide the agent with a problem formulation so that a contingency plan can be computed
		NondeterministicProblem<VacuumEnvironmentState, Action> problem = new NondeterministicProblem<>(
				world.getCurrentState(),
				VacuumWorldFunctions::getActions,
				VacuumWorldFunctions.createResultsFunctionFor(agent),
				VacuumWorldFunctions::testGoal,
				(s, a, sPrimed) -> 1.0);
		agent.makePlan(problem);

		// show and execute the plan
		System.out.println("initial plan: " + agent.getPlan());
		world.stepUntilDone();
		System.out.println("actions taken: " + actionTracker.getActions());
		System.out.println("final plan: " + agent.getPlan());
		System.out.println("final state: " + world.getCurrentState());
	}
}
