package aima.gui.demo.agent;

import aima.core.environment.vacuum.FullyObservableVacuumEnvironmentPerceptToStateFunction;
import aima.core.environment.vacuum.NondeterministicVacuumAgent;
import aima.core.environment.vacuum.NondeterministicVacuumEnvironment;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.VacuumEnvironmentState;
import aima.core.environment.vacuum.VacuumEnvironmentViewActionTracker;
import aima.core.environment.vacuum.VacuumWorldActions;
import aima.core.environment.vacuum.VacuumWorldGoalTest;
import aima.core.environment.vacuum.VacuumWorldResults;
import aima.core.search.framework.problem.DefaultStepCostFunction;
import aima.core.search.nondeterministic.NondeterministicProblem;

/**
 * Applies AND-OR-GRAPH-SEARCH to a non-deterministic version of the Vacuum World.
 * 
 * 
 * @author Andrew Brown
 */
public class NondeterministicVacuumEnvironmentDemo {
	public static void main(String[] args) {
		System.out.println("NON-DETERMINISTIC-VACUUM-ENVIRONMENT DEMO");
		System.out.println("");
		startAndOrSearch();
	}

	private static void startAndOrSearch() {
		System.out.println("AND-OR-GRAPH-SEARCH");
	    
	    NondeterministicVacuumAgent agent = new NondeterministicVacuumAgent(
        		new FullyObservableVacuumEnvironmentPerceptToStateFunction());
        // create state: both rooms are dirty and the vacuum is in room A
        VacuumEnvironmentState state = new VacuumEnvironmentState();
        state.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        state.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty);
        state.setAgentLocation(agent, VacuumEnvironment.LOCATION_A);
        // create problem
        NondeterministicProblem problem = new NondeterministicProblem(
                state,
                new VacuumWorldActions(),
                new VacuumWorldResults(agent),
                new VacuumWorldGoalTest(agent),
                new DefaultStepCostFunction());
        // set the problem and agent
        agent.setProblem(problem);
        
        // create world
        NondeterministicVacuumEnvironment world = new NondeterministicVacuumEnvironment(VacuumEnvironment.LocationState.Dirty, VacuumEnvironment.LocationState.Dirty);
        world.addAgent(agent, VacuumEnvironment.LOCATION_A);
        
        // execute and show plan
        System.out.println("Initial Plan: " + agent.getContingencyPlan());
        StringBuilder sb = new StringBuilder();
        world.addEnvironmentView(new VacuumEnvironmentViewActionTracker(sb));
        world.stepUntilDone();
        System.out.println("Remaining Plan: " + agent.getContingencyPlan());
        System.out.println("Actions Taken: " + sb);
        System.out.println("Final State: " + world.getCurrentState());
	}
}
