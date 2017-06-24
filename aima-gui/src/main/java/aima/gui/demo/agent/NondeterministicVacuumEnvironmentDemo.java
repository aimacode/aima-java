package aima.gui.demo.agent;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleActionTracker;
import aima.core.environment.vacuum.*;
import aima.core.search.agent.NondeterministicSearchAgent;
import aima.core.search.nondeterministic.NondeterministicProblem;

/**
 * Applies AND-OR-GRAPH-SEARCH to a non-deterministic version of the Vacuum World.
 * 
 * 
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

        // create agent and world
        NondeterministicSearchAgent<VacuumEnvironmentState, Action> agent = new NondeterministicSearchAgent<>
                (percept -> (VacuumEnvironmentState) percept); // percept == env state!
        NondeterministicVacuumEnvironment world = new NondeterministicVacuumEnvironment
                (VacuumEnvironment.LocationState.Dirty, VacuumEnvironment.LocationState.Dirty);
        world.addAgent(agent, VacuumEnvironment.LOCATION_A);

        // provide the agent with a problem formulation so that a contingency plan can be computed.
        NondeterministicProblem<VacuumEnvironmentState, Action> problem = new NondeterministicProblem<>(
                (VacuumEnvironmentState) world.getCurrentState(),
                VacuumWorldFunctions::getActions,
                VacuumWorldFunctions.createResultsFunction(agent),
                VacuumWorldFunctions::testGoal,
                (s, a, sPrimed) -> 1.0);
        agent.makePlan(problem);
        SimpleActionTracker actionTracker = new SimpleActionTracker();
        world.addEnvironmentView(actionTracker);

        // show and execute the plan
        System.out.println("Initial Plan: " + agent.getPlan());
        world.stepUntilDone();
        System.out.println("Actions Taken: " + actionTracker.getActions());
        System.out.println("Final Plan: " + agent.getPlan());
        System.out.println("Final State: " + world.getCurrentState());
	}
}
