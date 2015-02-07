package aima.test.core.unit.environment.vacuum.agent;

import aima.core.api.agent.Action;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.agent.TableDrivenVacuumAgent;
import aima.core.environment.vacuum.perceive.LocalPercept;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ciaran O'Reilly
 */
public class TableDrivenVacuumAgentTest {
    private TableDrivenVacuumAgent agent;

    @Before
    public void setUp() {
        agent = new TableDrivenVacuumAgent();
    }

    @Test
    public void testACleanAClean() {
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_RIGHT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_RIGHT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
    }


    @Test
    public void testACleanBClean() {
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_RIGHT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_LEFT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_RIGHT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
    }

    @Test
    public void testACleanBDirty() {
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_RIGHT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_SUCK,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_LEFT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean))
        );
        // Table is only defined for max 3 percepts in a sequence, so will generate a NoOp.
        Assert.assertEquals(
                Action.NoOp,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
    }

    @Test
    public void testADirtyBClean() {
        Assert.assertEquals(
                VacuumEnvironment.ACTION_SUCK,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_RIGHT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_LEFT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Clean))
        );
        // Table is only defined for max 3 percepts in a sequence, so will generate a NoOp.
        Assert.assertEquals(
                Action.NoOp,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
    }

    @Test
    public void testADirtyBDirty() {
        Assert.assertEquals(
                VacuumEnvironment.ACTION_SUCK,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Dirty))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_MOVE_RIGHT,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
        Assert.assertEquals(
                VacuumEnvironment.ACTION_SUCK,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_B, VacuumEnvironment.LocationState.Dirty))
        );
        // Table is only defined for max 3 percepts in a sequence, so will generate a NoOp.
        Assert.assertEquals(
                Action.NoOp,
                agent.perceive(new LocalPercept(VacuumEnvironment.LOCATION_A, VacuumEnvironment.LocationState.Clean))
        );
    }
}
