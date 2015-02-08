package aima.test.core.unit.environment.vacuum.agent;

import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.agent.ReflexVacuumAgent;
import aima.core.environment.vacuum.perceive.LocalPercept;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ciaran O'Reilly
 */
public class ReflexVacuumAgentTest {
    private ReflexVacuumAgent agent;

    @Before
    public void setUp() {
        agent = new ReflexVacuumAgent();
    }

    @Test
    public void testACleanAClean() {
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
    }


    @Test
    public void testACleanBClean() {
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.Left,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.Status.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
    }

    @Test
    public void testACleanBDirty() {
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.Suck,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.Status.Dirty))
        );
        Assert.assertEquals(
                VacuumEnvironment.Left,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.Status.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
    }

    @Test
    public void testADirtyBClean() {
        Assert.assertEquals(
                VacuumEnvironment.Suck,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Dirty))
        );
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.Left,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.Status.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
    }

    @Test
    public void testADirtyBDirty() {
        Assert.assertEquals(
                VacuumEnvironment.Suck,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Dirty))
        );
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.Suck,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.Status.Dirty))
        );
        Assert.assertEquals(
                VacuumEnvironment.Right,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.Status.Clean))
        );
    }

}
