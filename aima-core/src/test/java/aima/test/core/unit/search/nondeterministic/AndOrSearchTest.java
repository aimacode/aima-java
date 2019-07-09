package aima.test.core.unit.search.nondeterministic;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicPercept;
import aima.core.environment.vacuum.NondeterministicVacuumEnvironment;
import aima.core.environment.vacuum.VacuumEnvironmentState;
import aima.core.environment.vacuum.VacuumPercept;
import aima.core.environment.vacuum.VacuumWorldFunctions;
import aima.core.search.agent.NondeterministicSearchAgent;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.core.search.nondeterministic.Path;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static aima.core.environment.vacuum.VacuumEnvironment.*;

/**
 * Tests the AND-OR search algorithm using the erratic vacuum world of page 133,
 * AIMA3e. In essence, a two-square office is cleaned by a vacuum that randomly
 * (1) cleans the square, (2) cleans both squares, or (3) dirties the square it
 * meant to clean.
 *
 * @author Andrew Brown
 * @author Ruediger Lunde
 */
public class AndOrSearchTest {

    /** State is used as percept! */
    private NondeterministicSearchAgent<VacuumPercept, VacuumEnvironmentState, Action> agent;
    private NondeterministicVacuumEnvironment world;

    /**
     * Create the vacuum world with the classes defined in this file.
     */
    @Before
    public void setUp() {
        // create agent and world (init state: both rooms are dirty and the vacuum is in room A)
        agent = new NondeterministicSearchAgent<>(VacuumWorldFunctions::getState);
        world = new NondeterministicVacuumEnvironment(LocationState.Dirty, LocationState.Dirty);
        world.addAgent(agent, LOCATION_A);

        // create problem
        NondeterministicProblem<VacuumEnvironmentState, Action> problem =
                new NondeterministicProblem<>(
                (VacuumEnvironmentState) world.getCurrentState(),
                VacuumWorldFunctions::getActions,
                VacuumWorldFunctions.createResultsFunctionFor(agent),
                VacuumWorldFunctions::testGoal,
                (s, a, sPrimed) -> 1.0);
        // set the problem and agent
        agent.makePlan(problem);
    }

    /**
     * Test whether two identically-initialized states will equals() each other.
     */
    @Test
    public void testStateEquality() {
        // create state 1
        VacuumEnvironmentState s1 = new VacuumEnvironmentState();
        s1.setLocationState(LOCATION_A, LocationState.Dirty);
        s1.setLocationState(LOCATION_B, LocationState.Dirty);
        s1.setAgentLocation(agent, LOCATION_A);
        // create state 2
        VacuumEnvironmentState s2 = new VacuumEnvironmentState();
        s2.setLocationState(LOCATION_A, LocationState.Dirty);
        s2.setLocationState(LOCATION_B, LocationState.Dirty);
        s2.setAgentLocation(agent, LOCATION_A);
        // test
        boolean actual = s1.equals(s2);
        Assert.assertTrue(actual);
    }

    /**
     * Test whether a Path contains() a state; uses state enumeration from page
     * 134, AIMA3e.
     */
    @Test
    public void testPathContains() {
        // create state 1
        VacuumEnvironmentState s1 = new VacuumEnvironmentState();
        s1.setLocationState(LOCATION_A, LocationState.Dirty);
        s1.setLocationState(LOCATION_B, LocationState.Dirty);
        s1.setAgentLocation(agent, LOCATION_A);
        // create state 2
        VacuumEnvironmentState s2 = new VacuumEnvironmentState();
        s2.setLocationState(LOCATION_A, LocationState.Dirty);
        s2.setLocationState(LOCATION_B, LocationState.Dirty);
        s2.setAgentLocation(agent, LOCATION_B);
        // create state 3
        VacuumEnvironmentState s3 = new VacuumEnvironmentState();
        s3.setLocationState(LOCATION_A, LocationState.Dirty);
        s3.setLocationState(LOCATION_B, LocationState.Clean);
        s3.setAgentLocation(agent, LOCATION_A);
        // create state 4
        VacuumEnvironmentState s4 = new VacuumEnvironmentState();
        s4.setLocationState(LOCATION_A, LocationState.Dirty);
        s4.setLocationState(LOCATION_B, LocationState.Clean);
        s4.setAgentLocation(agent, LOCATION_B);
        // create test state 1
        VacuumEnvironmentState test1 = new VacuumEnvironmentState();
        test1.setLocationState(LOCATION_A, LocationState.Dirty);
        test1.setLocationState(LOCATION_B, LocationState.Clean);
        test1.setAgentLocation(agent, LOCATION_A);
        // create test state 2
        VacuumEnvironmentState test2 = new VacuumEnvironmentState();
        test2.setLocationState(LOCATION_A, LocationState.Clean);
        test2.setLocationState(LOCATION_B, LocationState.Clean);
        test2.setAgentLocation(agent, LOCATION_B);
        // add to path
        Path<VacuumEnvironmentState> path = new Path<>();
        path.addAll(Arrays.asList(s1, s2, s3, s4));
        // test
        Assert.assertTrue(path.contains(test1));
        Assert.assertFalse(path.contains(test2));
    }

    /**
     * Use AND-OR search to create a contingency plan; act the plan and
     * verify that it successfully cleans the NondeterministicVacuumWorld.
     */
    @Test
    public void testSearchExecutesSuccessfully() {
        // act plan
        world.stepUntilDone();
        // test
        VacuumEnvironmentState endState = (VacuumEnvironmentState) world.getCurrentState();
        LocationState a = endState.getLocationState(LOCATION_A);
        LocationState b = endState.getLocationState(LOCATION_B);
        Assert.assertEquals(LocationState.Clean, a);
        Assert.assertEquals(LocationState.Clean, b);
    }
}