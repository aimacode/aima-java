package aima.test.core.unit.environment.xyenv;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.EnvironmentObject;
import aima.core.agent.impl.AbstractAgent;
import aima.core.environment.xyenv.Wall;
import aima.core.environment.xyenv.XYEnvironment;
import aima.core.util.datastructure.XYLocation;
import aima.test.core.unit.agent.impl.MockAgent;

/**
 * @author Ravi Mohan
 * 
 */
public class XYEnvironmentTest {
	XYEnvironment env;

	AbstractAgent a;

	@Before
	public void setUp() {
		env = new XYEnvironment(10, 12);
		a = new MockAgent();
		env.addObjectToLocation(a, new XYLocation(3, 4));
	}

	@Test
	public void testAddObject() {
		Assert.assertEquals(1, env.getAgents().size());
		Assert.assertEquals(new XYLocation(3, 4), env.getCurrentLocationFor(a));
	}

	@Test
	public void testAddObject2() {
		env.addObjectToLocation(new Wall(), new XYLocation(9, 9));
		Assert.assertEquals(1, env.getAgents().size());
		Assert.assertEquals(2, env.getEnvironmentObjects().size());
		Assert.assertEquals(1, env.getObjectsAt(new XYLocation(9, 9)).size());
	}

	@Test
	public void testAddObjectTwice() {
		Assert.assertEquals(1, env.getAgents().size());
		XYLocation loc = new XYLocation(5, 5);
		AbstractAgent b = new MockAgent();
		env.addObjectToLocation(b, loc);
		Assert.assertEquals(2, env.getAgents().size());

		Assert.assertEquals(loc, env.getCurrentLocationFor(b));
	}

	@Test
	public void testMoveObjectToAbsoluteLocation() {
		XYLocation loc = new XYLocation(5, 5);
		env.moveObjectToAbsoluteLocation(a, loc);
		Assert.assertEquals(new XYLocation(5, 5), env.getCurrentLocationFor(a));
	}

	@Test
	public void testMoveObject() {
		XYLocation loc = new XYLocation(5, 5);
		env.moveObjectToAbsoluteLocation(a, loc);
		Assert.assertEquals(new XYLocation(5, 5), env.getCurrentLocationFor(a));
		env.moveObject(a, XYLocation.Direction.North);
		Assert.assertEquals(new XYLocation(5, 4), env.getCurrentLocationFor(a));
		env.moveObject(a, XYLocation.Direction.East);
		Assert.assertEquals(new XYLocation(6, 4), env.getCurrentLocationFor(a));
		env.moveObject(a, XYLocation.Direction.South);
		Assert.assertEquals(new XYLocation(6, 5), env.getCurrentLocationFor(a));
		env.moveObject(a, XYLocation.Direction.West);
		Assert.assertEquals(new XYLocation(5, 5), env.getCurrentLocationFor(a));
	}

	@Test
	public void testIsBlocked() {
		XYLocation loc = new XYLocation(5, 5);
		Assert.assertEquals(0, env.getObjectsAt(loc).size());
		Assert.assertEquals(false, env.isBlocked(loc));
		env.addObjectToLocation(new Wall(), loc);
		Assert.assertEquals(1, env.getObjectsAt(loc).size());
		Assert.assertEquals(true, env.isBlocked(loc));
	}

	@Test
	public void testMoveWithBlockingWalls() {
		XYLocation loc = new XYLocation(5, 5);
		env.moveObjectToAbsoluteLocation(a, loc);
		XYLocation northLoc = new XYLocation(5, 6);
		XYLocation southLoc = new XYLocation(5, 4);
		XYLocation westLoc = new XYLocation(4, 5);

		env.addObjectToLocation(new Wall(), northLoc); // wall to the north of
		// object
		Assert.assertTrue(env.isBlocked(northLoc));
		env.addObjectToLocation(new Wall(), southLoc); // wall to the south of
		// object
		env.addObjectToLocation(new Wall(), westLoc); // wall to the west of
		// object
		Assert.assertEquals(4, env.getEnvironmentObjects().size());

		env.moveObject(a, XYLocation.Direction.North); // should not move
		env.moveObject(a, XYLocation.Direction.South); // should not move
		env.moveObject(a, XYLocation.Direction.West); // should not move
		env.moveObject(a, XYLocation.Direction.East); // SHOULD move
		Assert.assertEquals(new XYLocation(6, 5), env.getCurrentLocationFor(a));
	}

	@Test
	public void testGetObjectsAt() {
		XYLocation loc = new XYLocation(5, 7);
		env.moveObjectToAbsoluteLocation(a, loc);
		Assert.assertEquals(1, env.getObjectsAt(loc).size());
		AbstractAgent b = new MockAgent();
		env.addObjectToLocation(b, loc);
		Assert.assertEquals(2, env.getObjectsAt(loc).size());
	}

	@Test
	public void testGetObjectsNear() {
		XYLocation loc = new XYLocation(5, 5);
		env.moveObjectToAbsoluteLocation(a, loc);
		AbstractAgent b = new MockAgent();
		AbstractAgent c = new MockAgent();
		Wall w1 = new Wall();

		env.addObjectToLocation(b, new XYLocation(7, 4));
		env.addObjectToLocation(c, new XYLocation(5, 7));
		env.addObjectToLocation(w1, new XYLocation(3, 10));

		// at this point agent A should be able to see B and C but not the wall
		// with a "vision radius" of 3
		Set<EnvironmentObject> visibleToA = env.getObjectsNear(a, 3);
		Assert.assertEquals(2, visibleToA.size());
		// agent B should be able to see A only
		Set<EnvironmentObject> visibleToB = env.getObjectsNear(b, 3);
		Assert.assertEquals(1, visibleToB.size());

		// move B South
		env.moveObject(b, XYLocation.Direction.South);
		// at this point both a and c should be visible to b
		visibleToB = env.getObjectsNear(b, 3);
		Assert.assertEquals(2, visibleToB.size());
		// move c near the wall
		env.moveObjectToAbsoluteLocation(c, new XYLocation(3, 11));
		// only the wall should be visible
		Set<EnvironmentObject> visibleToC = env.getObjectsNear(c, 3);
		Assert.assertEquals(1, visibleToC.size());
	}

	@Test
	public void testMakePerimeter() {
		env.makePerimeter();
		Assert.assertTrue(env.isBlocked(new XYLocation(0, 0)));
		Assert.assertTrue(env.isBlocked(new XYLocation(0, 6)));
		Assert.assertTrue(env.isBlocked(new XYLocation(0, 11)));
		Assert.assertTrue(env.isBlocked(new XYLocation(6, 0)));
		Assert.assertTrue(env.isBlocked(new XYLocation(9, 0)));
		Assert.assertTrue(env.isBlocked(new XYLocation(9, 6)));
		Assert.assertTrue(env.isBlocked(new XYLocation(9, 11)));
		Assert.assertTrue(env.isBlocked(new XYLocation(6, 11)));
	}
}
