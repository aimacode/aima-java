package aima.test.coretest;

import java.util.ArrayList;

import junit.framework.TestCase;
import aima.basic.Agent;
import aima.basic.MockAgent;
import aima.basic.Wall;
import aima.basic.XYEnvironment;
import aima.basic.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class XYEnvironmentTest extends TestCase {
	XYEnvironment env;

	Agent a;

	private static String LOCATION = XYEnvironment.LOCATION;

	public XYEnvironmentTest(String name) {
		super(name);
	}

	@Override
	public void setUp() {
		env = new XYEnvironment(10, 12);
		a = new MockAgent();
		env.addAgent(a, new XYLocation(3, 4));
	}

	public void testAddObject() {

		assertEquals(1, env.getAgents().size());
		assertEquals(new XYLocation(3, 4), a.getAttribute(LOCATION));
	}

	public void testAddObject2() {
		env.addObject(new Wall(), new XYLocation(9, 9));
		assertEquals(1, env.getAgents().size());
		assertEquals(1, env.getObjects().size());
		assertEquals(1, env.getObjectsAt(new XYLocation(9, 9)).size());
	}

	public void testAddObjectTwice() {
		assertEquals(1, env.getAgents().size());
		XYLocation loc = new XYLocation(5, 5);
		XYLocation loc2 = new XYLocation(6, 6);
		Agent b = new MockAgent();
		env.addAgent(b, loc);
		assertEquals(2, env.getAgents().size());

		assertEquals(loc, b.getAttribute(LOCATION));
	}

	public void testAbsoluteMoveObject() {
		XYLocation loc = new XYLocation(5, 5);
		env.moveObjectToAbsoluteLocation(a, loc);
		assertEquals(new XYLocation(5, 5), a.getAttribute(LOCATION));

	}

	public void testMoveObject() {
		XYLocation loc = new XYLocation(5, 5);
		env.moveObjectToAbsoluteLocation(a, loc);
		assertEquals(new XYLocation(5, 5), a.getAttribute(LOCATION));
		env.moveObject(a, "North");
		assertEquals(new XYLocation(5, 4), a.getAttribute(LOCATION));
		env.moveObject(a, "East");
		assertEquals(new XYLocation(6, 4), a.getAttribute(LOCATION));
		env.moveObject(a, "South");
		assertEquals(new XYLocation(6, 5), a.getAttribute(LOCATION));
		env.moveObject(a, "West");
		assertEquals(new XYLocation(5, 5), a.getAttribute(LOCATION));
	}

	public void testIsBlocked() {
		XYLocation loc = new XYLocation(5, 5);
		assertEquals(0, env.getObjectsAt(loc).size());
		assertEquals(false, env.isBlocked(loc));
		env.addObject(new Wall(), loc);
		assertEquals(1, env.getObjectsAt(loc).size());
		assertEquals(true, env.isBlocked(loc));
	}

	public void testMoveWithBlockingWalls() {
		XYLocation loc = new XYLocation(5, 5);
		env.moveObjectToAbsoluteLocation(a, loc);
		XYLocation northLoc = new XYLocation(5, 6);
		XYLocation southLoc = new XYLocation(5, 4);
		XYLocation westLoc = new XYLocation(4, 5);

		env.addObject(new Wall(), northLoc); // wall to the north of object
		assertTrue(env.isBlocked(northLoc));
		env.addObject(new Wall(), southLoc); // wall to the south of object
		env.addObject(new Wall(), westLoc); // wall to the west of object
		assertEquals(3, env.getObjects().size());

		env.moveObject(a, "North"); // should not move
		env.moveObject(a, "South"); // should not move
		env.moveObject(a, "West"); // should not move
		env.moveObject(a, "East"); // SHOULD move
		assertEquals(new XYLocation(6, 5), a.getAttribute(LOCATION));

	}

	public void testGetObjectsAt() {
		XYLocation loc = new XYLocation(5, 7);
		env.moveObjectToAbsoluteLocation(a, loc);
		assertEquals(1, env.getObjectsAt(loc).size());
		Agent b = new MockAgent();
		env.addAgent(b, loc);
		assertEquals(2, env.getObjectsAt(loc).size());
	}

	public void testGetObjectsNear() {
		XYLocation loc = new XYLocation(5, 5);
		env.moveObjectToAbsoluteLocation(a, loc);
		Agent b = new MockAgent();
		Agent c = new MockAgent();
		Wall w1 = new Wall();

		env.addAgent(b, new XYLocation(7, 4));
		env.addAgent(c, new XYLocation(5, 7));
		env.addObject(w1, new XYLocation(3, 10));

		// at this point agent A shouldbe able to see B and C but not the wall
		// with a "vision radius" of 3
		ArrayList visibleToA = env.getObjectsNear(a, 3);
		assertEquals(2, visibleToA.size());
		// agent B shouldbe able to see A only
		ArrayList visibleToB = env.getObjectsNear(b, 3);
		assertEquals(1, visibleToB.size());

		// move B north and west
		env.moveObject(b, "North");
		env.moveObject(b, "West");
		// at this point both a and c should be visible to b
		// TODO fix this
		visibleToB = env.getObjectsNear(b, 3);
		assertEquals(1, visibleToB.size());
		// move c near the wall
		env.moveObjectToAbsoluteLocation(c, new XYLocation(3, 11));
		// only the wall should be visible
		ArrayList visibleToC = env.getObjectsNear(b, 3);
		assertEquals(1, visibleToC.size());

		//

	}

	public void testMakePerimeter() {
		env.makePerimeter();
		assertTrue(env.isBlocked(new XYLocation(0, 0)));
		assertTrue(env.isBlocked(new XYLocation(0, 6)));
		assertTrue(env.isBlocked(new XYLocation(0, 11)));
		assertTrue(env.isBlocked(new XYLocation(6, 0)));
		assertTrue(env.isBlocked(new XYLocation(9, 0)));
		assertTrue(env.isBlocked(new XYLocation(9, 6)));
		assertTrue(env.isBlocked(new XYLocation(9, 11)));
		assertTrue(env.isBlocked(new XYLocation(6, 11)));

	}

}
