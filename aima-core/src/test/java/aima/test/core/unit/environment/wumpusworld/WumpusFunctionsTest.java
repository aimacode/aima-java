package aima.test.core.unit.environment.wumpusworld;

import aima.core.agent.Action;
import aima.core.environment.wumpusworld.AgentPosition;
import aima.core.environment.wumpusworld.WumpusAction;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusFunctions;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class WumpusFunctionsTest {
	
	private ActionsFunction<AgentPosition, WumpusAction> actionsFn;
	private ResultFunction<AgentPosition, WumpusAction> resultFn;

	@Before
	public void setUp() {
		WumpusCave completeCave = new WumpusCave(4, 4);
		actionsFn = WumpusFunctions.createActionsFunction(completeCave);
		resultFn = WumpusFunctions.createResultFunction(completeCave);
	}

	@Test
	public void testSuccessors() {
		// From every position the possible actions are:
		//    - Turn right (change orientation, not position)
		//    - Turn left (change orientation, not position)
		//    - Forward (change position, not orientation)
		AgentPosition pos11n = new AgentPosition(1, 1, AgentPosition.Orientation.FACING_NORTH);

		List<WumpusAction> actions = actionsFn.apply(pos11n);
		Assert.assertEquals(actions.size(), 3);
		Assert.assertTrue(actions.contains(WumpusAction.FORWARD));
		Assert.assertTrue(actions.contains(WumpusAction.TURN_LEFT));
		Assert.assertTrue(actions.contains(WumpusAction.TURN_RIGHT));

		Assert.assertEquals("[1,2]->FacingNorth", resultFn.apply(pos11n, WumpusAction.FORWARD).toString());
		Assert.assertEquals("[1,1]->FacingWest", resultFn.apply(pos11n, WumpusAction.TURN_LEFT).toString());
		Assert.assertEquals("[1,1]->FacingEast", resultFn.apply(pos11n, WumpusAction.TURN_RIGHT).toString());

		//If you are in front of a wall forward action is not possible
		AgentPosition pos31s = new AgentPosition(3, 1, AgentPosition.Orientation.FACING_SOUTH);
		AgentPosition pos41e = new AgentPosition(4, 1, AgentPosition.Orientation.FACING_EAST);
		for (Action a : actionsFn.apply(pos31s))
			Assert.assertNotEquals(a, WumpusAction.FORWARD);
		
		for (Action a : actionsFn.apply(pos41e))
			Assert.assertNotEquals(a, WumpusAction.FORWARD);
	}
}
