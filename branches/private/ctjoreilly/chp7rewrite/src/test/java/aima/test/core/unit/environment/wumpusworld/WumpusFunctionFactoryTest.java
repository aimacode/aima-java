package aima.test.core.unit.environment.wumpusworld;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.environment.wumpusworld.ForwardAction;
import aima.core.environment.wumpusworld.TurnAction;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusFunctionFactory;
import aima.core.environment.wumpusworld.WumpusPosition;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

/**
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 */
public class WumpusFunctionFactoryTest {
	
	ActionsFunction af;
	ResultFunction rf;

	@Before
	public void setUp() {
		WumpusCave completeField = new WumpusCave(4, 4);

		af = WumpusFunctionFactory.getActionsFunction(completeField);
		rf = WumpusFunctionFactory.getResultFunction();
	}

	@Test
	public void testSuccessors() {
		ArrayList<WumpusPosition> succPositions = new ArrayList<WumpusPosition>();
		ArrayList<Integer> succOrientation = new ArrayList<Integer>();
		
		// From every position the possible actions are:
		//    - Turn right (change orientation, not position)
		//    - Turn left (change orientation, not position)
		//    - Forward (change position, not orientation)
		WumpusPosition P11U = new WumpusPosition(1, 1, WumpusPosition.ORIENTATION_NORTH);
		succPositions.add(new WumpusPosition(1, 2, WumpusPosition.ORIENTATION_NORTH));
		succOrientation.add(WumpusPosition.ORIENTATION_EAST);
		succOrientation.add(WumpusPosition.ORIENTATION_WEST);
		for (Action a : af.actions(P11U)) {
			if (a instanceof ForwardAction) {
				Assert.assertTrue(succPositions.contains(((ForwardAction)a).getToPosition()));
				Assert.assertTrue(succPositions.contains(rf.result(P11U, a)));
			}
			else { 
				Assert.assertTrue(succOrientation.contains(((TurnAction)a).getToOrientation()));
				Assert.assertEquals("11"+((TurnAction)a).getToOrientation(),rf.result(P11U, a).toString());
			}
		}
		
		
		//If you are in front of a wall forward action is not possible
		WumpusPosition P31D = new WumpusPosition(3, 1, WumpusPosition.ORIENTATION_SOUTH);
		WumpusPosition P41R = new WumpusPosition(4, 1, WumpusPosition.ORIENTATION_EAST);
		for (Action a : af.actions(P31D))
			Assert.assertFalse(a instanceof ForwardAction);
		
		for (Action a : af.actions(P41R)) {
			Assert.assertFalse(a instanceof ForwardAction);
		}
	}
}
