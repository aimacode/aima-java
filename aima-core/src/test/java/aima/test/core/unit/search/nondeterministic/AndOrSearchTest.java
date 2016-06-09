package aima.test.core.unit.search.nondeterministic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.vacuum.FullyObservableVacuumEnvironmentPerceptToStateFunction;
import aima.core.environment.vacuum.NondeterministicVacuumAgent;
import aima.core.environment.vacuum.NondeterministicVacuumEnvironment;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.VacuumEnvironmentState;
import aima.core.environment.vacuum.VacuumWorldActions;
import aima.core.environment.vacuum.VacuumWorldGoalTest;
import aima.core.environment.vacuum.VacuumWorldResults;
import aima.core.search.framework.problem.DefaultStepCostFunction;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.core.search.nondeterministic.Path;

/**
 * Tests the AND-OR search algorithm using the erratic vacuum world of page 133,
 * AIMA3e. In essence, a two-square office is cleaned by a vacuum that randomly
 * (1) cleans the square, (2) cleans both squares, or (3) dirties the square it
 * meant to clean.
 *
 * @author Andrew Brown
 */
public class AndOrSearchTest {

    NondeterministicVacuumAgent agent;
    NondeterministicVacuumEnvironment world;
    NondeterministicProblem problem;

    /**
     * Create the vacuum world with the classes defined in this file.
     */
    @Before
    public void setUp() {
        
        this.agent = new NondeterministicVacuumAgent(
        		new FullyObservableVacuumEnvironmentPerceptToStateFunction());
        // create state: both rooms are dirty and the vacuum is in room A
        VacuumEnvironmentState state = new VacuumEnvironmentState();
        state.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        state.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty);
        state.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_A);
        // create problem
        this.problem = new NondeterministicProblem(
                state,
                new VacuumWorldActions(),
                new VacuumWorldResults(this.agent),
                new VacuumWorldGoalTest(this.agent),
                new DefaultStepCostFunction());
        // set the problem and agent
        this.agent.setProblem(this.problem);
        
        // create world
        this.world = new NondeterministicVacuumEnvironment(VacuumEnvironment.LocationState.Dirty, VacuumEnvironment.LocationState.Dirty);
        this.world.addAgent(this.agent, VacuumEnvironment.LOCATION_A);
    }

    /**
     * Test whether two identically-initialized states will equals() each other.
     */
    @Test
    public void testStateEquality() {
        // create state 1
        VacuumEnvironmentState s1 = new VacuumEnvironmentState();
        s1.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        s1.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty);
        s1.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_A);
        // create state 2
        VacuumEnvironmentState s2 = new VacuumEnvironmentState();
        s2.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        s2.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty);
        s2.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_A);
        // test
        boolean expected = true;
        boolean actual = s1.equals(s2);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test whether a Path contains() a state; uses state enumeration from page
     * 134, AIMA3e.
     */
    @Test
    public void testPathContains() {
        // create state 1
        VacuumEnvironmentState s1 = new VacuumEnvironmentState();
        s1.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        s1.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty);
        s1.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_A);
        // create state 2
        VacuumEnvironmentState s2 = new VacuumEnvironmentState();
        s2.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        s2.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty);
        s2.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_B);
        // create state 3
        VacuumEnvironmentState s3 = new VacuumEnvironmentState();
        s3.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        s3.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean);
        s3.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_A);
        // create state 4
        VacuumEnvironmentState s4 = new VacuumEnvironmentState();
        s4.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        s4.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean);
        s4.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_B);
        // create test state 1
        VacuumEnvironmentState test1 = new VacuumEnvironmentState();
        test1.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty);
        test1.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean);
        test1.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_A);
        // create test state 2
        VacuumEnvironmentState test2 = new VacuumEnvironmentState();
        test2.setLocationState(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean);
        test2.setLocationState(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean);
        test2.setAgentLocation(this.agent, VacuumEnvironment.LOCATION_B);
        // add to path
        Path path = new Path();
        path = path.append(s1, s2, s3, s4);
        // test
        Assert.assertEquals(true, path.contains(test1));
        Assert.assertEquals(false, path.contains(test2));
    }

    /**
     * Use AND-OR search to create a contingency plan; execute the plan and
     * verify that it successfully cleans the NondeterministicVacuumWorld.
     */
    @Test
    public void testSearchExecutesSuccessfully() {
        // execute plan
        this.world.stepUntilDone();
        // test
        VacuumEnvironmentState endState = (VacuumEnvironmentState) this.world.getCurrentState();
        VacuumEnvironment.LocationState a = endState.getLocationState(VacuumEnvironment.LOCATION_A);
        VacuumEnvironment.LocationState b = endState.getLocationState(VacuumEnvironment.LOCATION_B);
        Assert.assertEquals(VacuumEnvironment.LocationState.Clean, a);
        Assert.assertEquals(VacuumEnvironment.LocationState.Clean, b);
    }
}