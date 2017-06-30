package aima.test.core.unit.environment.wumpusworld;

import aima.core.environment.wumpusworld.*;
import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.propositional.inference.OptimizedDPLL;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 *
 */
@RunWith(Parameterized.class)
public class WumpusKnowledgeBaseTest {
	
	private DPLL dpll;
	
	@Parameters(name = "{index}: dpll={0}")
    public static Collection<Object[]> inferenceAlgorithmSettings() {
        return Arrays.asList(new Object[][] {
        		{new DPLLSatisfiable()}, 
        		{new OptimizedDPLL()}   
        });
    }
    
    public WumpusKnowledgeBaseTest(DPLL dpll) {
    	this.dpll = dpll;
    }
	
	@Test
	public void testAskCurrentPosition() {
		// Create very small cave in order to make inference for tests faster.
		WumpusKnowledgeBase KB = new WumpusKnowledgeBase
				(2, 2, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), dpll);
		// NOTE: in the 2x2 cave for this set of assertion tests, 
		// we are going to have no pits and the wumpus in [2,2]
		// this needs to be correctly set up in order to keep the KB consistent.
		int t = 0;
		AgentPosition current;
		step(KB, new WumpusPercept(), t);
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), current);
		KB.makeActionSentence(WumpusAction.FORWARD, t);
		
		t++;
		step(KB, new WumpusPercept().setStench(), t);
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_EAST), current);
		KB.makeActionSentence(WumpusAction.TURN_LEFT, t);
		
		t++;
		step(KB, new WumpusPercept().setStench(), t);
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_NORTH), current);
		KB.makeActionSentence(WumpusAction.TURN_LEFT, t);
		
		t++;
		step(KB, new WumpusPercept().setStench(), t);
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_WEST), current);
		KB.makeActionSentence(WumpusAction.FORWARD, t);
		
		t++;
		step(KB, new WumpusPercept(), t);
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_WEST), current);
		KB.makeActionSentence(WumpusAction.FORWARD, t);
		
		t++;
		step(KB, new WumpusPercept().setBump(), t);
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_WEST), current);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testAskSafeRooms() {
		WumpusKnowledgeBase KB;
		int t = 0;
		
		KB = new WumpusKnowledgeBase(2, 2, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), dpll);
		step(KB, new WumpusPercept(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1)); add(new Room(1,2)); add(new Room(2, 1));}}, KB.askSafeRooms(t));
		
		KB =  new WumpusKnowledgeBase(2, 2, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), dpll);
		step(KB, new WumpusPercept().setStench(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1));}}, KB.askSafeRooms(t));

		KB =  new WumpusKnowledgeBase(2, 2, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), dpll);
		step(KB, new WumpusPercept().setBreeze(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1));}}, KB.askSafeRooms(t));
		
		KB =  new WumpusKnowledgeBase(2, 2, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), dpll);
		step(KB, new WumpusPercept().setStench().setBreeze(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1));}}, KB.askSafeRooms(t));
	}
	
	@Test
	public void testAskGlitter() {
		WumpusKnowledgeBase KB =
				new WumpusKnowledgeBase(2, 2, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), dpll);
		step(KB, new WumpusPercept(), 0);
		Assert.assertFalse(KB.askGlitter(0));
		step(KB, new WumpusPercept(), 1);
		Assert.assertFalse(KB.askGlitter(1));
		step(KB, new WumpusPercept().setGlitter(), 2);
		Assert.assertTrue(KB.askGlitter(2));
		step(KB, new WumpusPercept(), 3);
		Assert.assertFalse(KB.askGlitter(3));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testAskUnvistedRooms() {
		WumpusKnowledgeBase KB;
		int t = 0;
		
		KB =  new WumpusKnowledgeBase(2, 2, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), dpll);
		step(KB, new WumpusPercept(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,2)); add(new Room(2, 1)); add(new Room(2,2));}}, KB.askUnvisitedRooms(t));
		KB.makeActionSentence(WumpusAction.FORWARD, t); // Move agent to [2,1]
		
		t++;
		step(KB, new WumpusPercept().setStench(), t); // NOTE: Wumpus in [2,2] so have stench
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,2)); add(new Room(2,2));}}, KB.askUnvisitedRooms(t));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testAskPossibleWumpusRooms() {
		WumpusKnowledgeBase KB;
		int t = 0;
		
		KB =  new WumpusKnowledgeBase(2, 2, dpll);
		step(KB, new WumpusPercept(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(2,2));}}, KB.askPossibleWumpusRooms(t));	
		
		KB =  new WumpusKnowledgeBase(2, 2, dpll);
		step(KB, new WumpusPercept().setStench(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,2)); add(new Room(2, 1));}}, KB.askPossibleWumpusRooms(t));		

		KB =  new WumpusKnowledgeBase(3, 3, dpll);
		step(KB, new WumpusPercept(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,3)); add(new Room(2,2)); add(new Room(2,3)); add(new Room(3,1)); add(new Room(3,2)); add(new Room(3,3));}}, KB.askPossibleWumpusRooms(t));
		KB.makeActionSentence(WumpusAction.FORWARD, t); // Move agent to [2,1]
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testAskNotUnsafeRooms() {
		WumpusKnowledgeBase KB;
		int t = 0;
		
		KB = new WumpusKnowledgeBase(2, 2, dpll);
		step(KB, new WumpusPercept(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1)); add(new Room(1,2)); add(new Room(2,1));}}, KB.askNotUnsafeRooms(t));	
		
		KB =  new WumpusKnowledgeBase(2, 2, dpll);
		step(KB, new WumpusPercept().setStench(), t);
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1)); add(new Room(1,2)); add(new Room(2, 1)); add(new Room(2,2));}}, KB.askNotUnsafeRooms(t));		
	}
	
	@Test
	public void testExampleInSection7_2_described_pg268_AIMA3e() {
		// Make smaller in order to reduce the inference time required, this still covers all the relevant rooms for the example
		WumpusKnowledgeBase KB = new WumpusKnowledgeBase
				(3, 3, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), dpll);
		int t = 0;
		// 0
		step(KB, new WumpusPercept(), t);
		KB.makeActionSentence(WumpusAction.FORWARD, t);
		
		t++; // 1
		step(KB, new WumpusPercept().setBreeze(), t);
		KB.makeActionSentence(WumpusAction.TURN_RIGHT, t);
		
		t++; // 2
		step(KB, new WumpusPercept().setBreeze(), t);
		KB.makeActionSentence(WumpusAction.TURN_RIGHT, t);
		
		t++; // 3
		step(KB, new WumpusPercept().setBreeze(), t);
		KB.makeActionSentence(WumpusAction.FORWARD, t);
	
		t++; // 4
		step(KB, new WumpusPercept(), t);
		KB.makeActionSentence(WumpusAction.TURN_RIGHT, t);
		
		t++; // 5
		step(KB, new WumpusPercept(), t);
		KB.makeActionSentence(WumpusAction.FORWARD, t);
		
		t++; // 6
		step(KB, new WumpusPercept().setStench(), t);
		
		Assert.assertTrue(KB.ask(KB.newSymbol(WumpusKnowledgeBase.LOCATION, t, 1, 2)));
		Assert.assertTrue(KB.ask(KB.newSymbol(WumpusKnowledgeBase.WUMPUS, 1, 3)));
		Assert.assertTrue(KB.ask(KB.newSymbol(WumpusKnowledgeBase.PIT, 3, 1)));
		Assert.assertTrue(KB.ask(KB.newSymbol(WumpusKnowledgeBase.OK_TO_MOVE_INTO, t, 2, 2)));
	}
	
	private void step(WumpusKnowledgeBase KB, WumpusPercept percept, int t) {
		KB.tellTemporalPhysicsSentences(t);
		KB.makePerceptSentence(percept, t);
	}	
}