package aima.test.unit.environment.wumpusworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import aima.core.environment.wumpusworld.AgentPercept;
import aima.core.environment.wumpusworld.AgentPosition;
import aima.core.environment.wumpusworld.HybridWumpusAgent;
import aima.core.environment.wumpusworld.Room;
import aima.core.environment.wumpusworld.action.Climb;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.Grab;
import aima.core.environment.wumpusworld.action.Shoot;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.WWAction;
import aima.extra.logic.propositional.parser.PLParserWrapper;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class HybridWumpusAgentTest {

	@SuppressWarnings("serial")
	@Test
	public void testPlanRoute() {
		HybridWumpusAgent hwa = new HybridWumpusAgent(4, new PLParserWrapper());
		
		// Should be a NoOp plan as we are already at the goal.
		Assert.assertEquals(Collections.<WWAction>emptyList(), 
			hwa.planRoute(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), 
				new LinkedHashSet<Room>() {{
					add(new Room(1,1));
				}},
				allRooms(4)
		));

		Assert.assertEquals(Arrays.asList(
				new TurnLeft(AgentPosition.Orientation.FACING_EAST),
				new TurnLeft(AgentPosition.Orientation.FACING_NORTH),
				new Forward(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_WEST))
			),hwa.planRoute(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_EAST), 
				new LinkedHashSet<Room>() {{
					add(new Room(1,1));
				}},
				allRooms(4)
		));

		Assert.assertEquals(Arrays.asList(
				new TurnLeft(AgentPosition.Orientation.FACING_EAST),
				new Forward(new AgentPosition(3, 1, AgentPosition.Orientation.FACING_NORTH)),
				new Forward(new AgentPosition(3, 2, AgentPosition.Orientation.FACING_NORTH)),
				new TurnLeft(AgentPosition.Orientation.FACING_NORTH),
				new Forward(new AgentPosition(3, 3, AgentPosition.Orientation.FACING_WEST)),
				new Forward(new AgentPosition(2, 3, AgentPosition.Orientation.FACING_WEST)),
				new TurnLeft(AgentPosition.Orientation.FACING_WEST),
				new Forward(new AgentPosition(1, 3, AgentPosition.Orientation.FACING_SOUTH)),
				new Forward(new AgentPosition(1, 2, AgentPosition.Orientation.FACING_SOUTH))
			), 
			hwa.planRoute(new AgentPosition(3, 1, AgentPosition.Orientation.FACING_EAST), 
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
		HybridWumpusAgent hwa = new HybridWumpusAgent(4, new PLParserWrapper());
		
		ArrayList<WWAction> a = new ArrayList<>(Arrays.asList(new Shoot()));

		// Should be just shoot as are facing the Wumpus
		Assert.assertEquals(a, 
			hwa.planShot(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), 
				new LinkedHashSet<Room>() {{
					add(new Room(3,1));
				}},
				allRooms(4)
		));	
		Assert.assertEquals(Arrays.asList(
				new TurnLeft(AgentPosition.Orientation.FACING_EAST),
				new Shoot()
			), 
			hwa.planShot(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), 
				new LinkedHashSet<Room>() {{
					add(new Room(1,2));
				}},
				allRooms(4)
		));	
		Assert.assertEquals(Arrays.asList(
				new Forward(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST)),
				new TurnLeft(AgentPosition.Orientation.FACING_EAST),
				new Shoot()
			), 
			hwa.planShot(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), 
				new LinkedHashSet<Room>() {{
					add(new Room(2,2));
				}},
				allRooms(4)
		));	
	}
	
	@Test
	public void testGrabAndClimb() {
		HybridWumpusAgent hwa = new HybridWumpusAgent(2, new PLParserWrapper());
		// The gold is in the first square
		WWAction a = hwa.perceive(new AgentPercept(true, true, true, false, false));
		Assert.assertTrue(a instanceof Grab);
		a = hwa.perceive(new AgentPercept(true, true, true, false, false));
		Assert.assertTrue(a instanceof Climb);
	}
	
	private static Set<Room> allRooms(int caveXandYDimensions) {
		Set<Room> allRooms = new LinkedHashSet<Room>();		
		for (int x = 1; x <= caveXandYDimensions; x++) {
			for (int y = 1; y <= caveXandYDimensions; y++) {
				allRooms.add(new Room(x, y));
			}
		}
		
		return allRooms;
	}
}
