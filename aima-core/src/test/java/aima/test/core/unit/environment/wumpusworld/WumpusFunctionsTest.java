package aima.test.core.unit.environment.wumpusworld;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.environment.wumpusworld.WumpusCave;
import aima.core.environment.wumpusworld.WumpusFunctionFunctions;
import aima.core.environment.wumpusworld.AgentPosition;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.TurnRight;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.ResultFunction;

/**
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class WumpusFunctionsTest {
	
	private ActionsFunction<AgentPosition, Action> actionFn;
	private ResultFunction<AgentPosition, Action> resultFn;

	@Before
	public void setUp() {
		WumpusCave completeCave = new WumpusCave(4, 4);

		actionFn = WumpusFunctionFunctions.createActionsFunction(completeCave);
		resultFn = WumpusFunctionFunctions.createResultFunction();
	}

	@Test
	public void testSuccessors() {
		ArrayList<AgentPosition> succPositions = new ArrayList<>();
		ArrayList<AgentPosition.Orientation> succOrientation = new ArrayList<>();
		
		// From every position the possible actions are:
		//    - Turn right (change orientation, not position)
		//    - Turn left (change orientation, not position)
		//    - Forward (change position, not orientation)
		AgentPosition P11U = new AgentPosition(1, 1, AgentPosition.Orientation.FACING_NORTH);
		succPositions.add(new AgentPosition(1, 2, AgentPosition.Orientation.FACING_NORTH));
		succOrientation.add(AgentPosition.Orientation.FACING_EAST);
		succOrientation.add(AgentPosition.Orientation.FACING_WEST);
		for (Action a : actionFn.apply(P11U)) {
			if (a instanceof Forward) {
				Assert.assertTrue(succPositions.contains(((Forward)a).getToPosition()));
				Assert.assertTrue(succPositions.contains(resultFn.apply(P11U, a)));
			}
			else if (a instanceof TurnLeft) { 
				Assert.assertTrue(succOrientation.contains(((TurnLeft)a).getToOrientation()));
				Assert.assertEquals("[1,1]->FacingWest", resultFn.apply(P11U, a).toString());
			}
			else if (a instanceof TurnRight) {
				Assert.assertTrue(succOrientation.contains(((TurnRight)a).getToOrientation()));
				Assert.assertEquals("[1,1]->FacingEast", resultFn.apply(P11U, a).toString());
			}
		}
		
		
		//If you are in front of a wall forward action is not possible
		AgentPosition P31D = new AgentPosition(3, 1, AgentPosition.Orientation.FACING_SOUTH);
		AgentPosition P41R = new AgentPosition(4, 1, AgentPosition.Orientation.FACING_EAST);
		for (Action a : actionFn.apply(P31D)) {
			Assert.assertFalse(a instanceof Forward);
		}
		
		for (Action a : actionFn.apply(P41R)) {
			Assert.assertFalse(a instanceof Forward);
		}
	}
}
