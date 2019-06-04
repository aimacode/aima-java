package aima.test.core.unit.environment.wumpusworld;

import aima.core.agent.impl.SimpleActionTracker;
import aima.core.environment.wumpusworld.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 *
 */
public class HybridWumpusAgentTest {

	@SuppressWarnings("serial")
	@Test
	public void testPlanRoute() {
		HybridWumpusAgent hwa;

		hwa = new HybridWumpusAgent(4, 4, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST));
		// Should be a NoOp plan as we are already at the goal.
		Assert.assertEquals(Collections.emptyList(),
			hwa.planRouteToRooms(
				new LinkedHashSet<Room>() {{
					add(new Room(1,1));
				}},
				allRooms(4)
		));

		hwa = new HybridWumpusAgent(4, 4, new AgentPosition(2, 1, AgentPosition.Orientation.FACING_EAST));
		Assert.assertEquals(Arrays.asList(WumpusAction.TURN_LEFT, WumpusAction.TURN_LEFT, WumpusAction.FORWARD),
			hwa.planRouteToRooms(
				new LinkedHashSet<Room>() {{
					add(new Room(1,1));
				}},
				allRooms(4)
		));

		hwa = new HybridWumpusAgent(4, 4, new AgentPosition(3, 1, AgentPosition.Orientation.FACING_EAST));
		Assert.assertEquals(Arrays.asList(WumpusAction.TURN_LEFT, WumpusAction.FORWARD, WumpusAction.FORWARD,
				WumpusAction.TURN_LEFT, WumpusAction.FORWARD, WumpusAction.FORWARD, WumpusAction.TURN_LEFT,
				WumpusAction.FORWARD, WumpusAction.FORWARD),
			hwa.planRouteToRooms(
				new LinkedHashSet<Room>() {{
					add(new Room(1,1));
				}},
				new LinkedHashSet<Room>() {{
					addAll(allRooms(4));
					remove(new Room(2, 1));
					remove(new Room(2, 2));
				}}
	    ));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testPlanShot() {
		HybridWumpusAgent hwa;
		hwa = new HybridWumpusAgent(4, 4, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST));
		// Should be just shoot as are facing the Wumpus
		Assert.assertEquals(Collections.singletonList(WumpusAction.SHOOT),
			hwa.planShot(
				new LinkedHashSet<Room>() {{
					add(new Room(3,1));
				}},
				allRooms(4)
		));

		hwa = new HybridWumpusAgent(4, 4, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST));
		Assert.assertEquals(Arrays.asList(WumpusAction.TURN_LEFT, WumpusAction.SHOOT),
			hwa.planShot(
				new LinkedHashSet<Room>() {{
					add(new Room(1,2));
				}},
				allRooms(4)
		));

		hwa = new HybridWumpusAgent(4, 4, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST));
		Assert.assertEquals(Arrays.asList(WumpusAction.FORWARD, WumpusAction.TURN_LEFT, WumpusAction.SHOOT),
			hwa.planShot(
				new LinkedHashSet<Room>() {{
					add(new Room(2,2));
				}},
				allRooms(4)
		));	
	}
	
	@Test
	public void testGrabAndClimb() {
		HybridWumpusAgent hwa =
				new HybridWumpusAgent(2, 2, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_NORTH));
		// The gold is in the first square
		Optional<WumpusAction> a = hwa.act(new WumpusPercept().setStench().setBreeze().setGlitter());
		Assert.assertEquals(WumpusAction.GRAB, a.orElse(null));
		a = hwa.act(new WumpusPercept().setStench().setBreeze().setGlitter());
		Assert.assertEquals(WumpusAction.CLIMB, a.orElse(null));
	}

	@Test
	public void testSimulation2x2Cave() {
		WumpusCave cave = new WumpusCave(2, 2, ""
				+ "W . "
				+ "S G ");
		WumpusEnvironment env = new WumpusEnvironment(cave);
		SimpleActionTracker view = new SimpleActionTracker();
		env.addEnvironmentListener(view);
		HybridWumpusAgent a = new HybridWumpusAgent
				(cave.getCaveXDimension(), cave.getCaveYDimension(), cave.getStart());
		env.addAgent(a);
		env.stepUntilDone();
		Assert.assertEquals(view.getActions(),
				"SHOOT, FORWARD, TURN_RIGHT, FORWARD, TURN_RIGHT, FORWARD, GRAB, TURN_RIGHT, FORWARD, CLIMB");
	}

	private static Set<Room> allRooms(int caveDimensions) {
		Set<Room> allRooms = new LinkedHashSet<>();
		for (int x = 1; x <= caveDimensions; x++) {
			for (int y = 1; y <= caveDimensions; y++) {
				allRooms.add(new Room(x, y));
			}
		}
		return allRooms;
	}
}
