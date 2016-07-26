package aima.test.unit.environment.wumpusworld;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import aima.core.environment.wumpusworld.AgentPercept;
import aima.core.environment.wumpusworld.AgentPosition;
import aima.core.environment.wumpusworld.Room;
import aima.core.environment.wumpusworld.WumpusKnowledgeBase;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.TurnRight;
import aima.core.environment.wumpusworld.action.WWAction;
import aima.core.logic.api.propositional.DPLL;
import aima.core.logic.basic.propositional.inference.DPLLSatisfiable;
import aima.core.logic.basic.propositional.inference.OptimizedDPLL;
import aima.extra.logic.propositional.parser.PLParserWrapper;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Anurag Rai
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
		WumpusKnowledgeBase<WWAction> KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper()); // Create very small cave in order to make inference for tests faster.
		// NOTE: in the 2x2 cave for this set of assertion tests, 
		// we are going to have no pits and the wumpus in [2,2]
		// this needs to be correctly set up in order to keep the KB consistent.
		int t = 0;
		AgentPosition current;
		step(KB, new AgentPercept(false, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), current);
		KB.makeActionSentence(new Forward(current), t);
		
		t++;
		step(KB, new AgentPercept(true, false, false, false, false), t);			
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_EAST), current);
		KB.makeActionSentence(new TurnLeft(current.getOrientation()), t);
		
		t++;
		step(KB, new AgentPercept(true, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_NORTH), current);
		KB.makeActionSentence(new TurnLeft(current.getOrientation()), t);
		
		t++;
		step(KB, new AgentPercept(true, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_WEST), current);
		KB.makeActionSentence(new Forward(current), t);
		
		t++;
		step(KB, new AgentPercept(false, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_WEST), current);
		KB.makeActionSentence(new Forward(current), t);
		
		t++;
		step(KB, new AgentPercept(false, false, false, true, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_WEST), current);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testAskSafeRooms() {
		WumpusKnowledgeBase<WWAction> KB;
		int t = 0;
		
		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(false, false, false, false, false), t);		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1)); add(new Room(1,2)); add(new Room(2, 1));}}, KB.askSafeRooms(t));
		
		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(true, false, false, false, false), t);		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1));}}, KB.askSafeRooms(t));

		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(false, true, false, false, false), t);		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1));}}, KB.askSafeRooms(t));
		
		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(true, true, false, false, false), t);		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1));}}, KB.askSafeRooms(t));
	}
	
	@Test
	public void testAskGlitter() {
		WumpusKnowledgeBase<WWAction> KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper()); 
		step(KB, new AgentPercept(false, false, false, false, false), 0);
		Assert.assertFalse(KB.askGlitter(0));
		step(KB, new AgentPercept(false, false, false, false, false), 1);
		Assert.assertFalse(KB.askGlitter(1));
		step(KB, new AgentPercept(false, false, true, false, false), 2);
		Assert.assertTrue(KB.askGlitter(2));
		step(KB, new AgentPercept(false, false, false, false, false), 3);
		Assert.assertFalse(KB.askGlitter(3));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testAskUnvistedRooms() {
		WumpusKnowledgeBase<WWAction> KB;
		int t = 0;
		
		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(false, false, false, false, false), t);		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,2)); add(new Room(2, 1)); add(new Room(2,2));}}, KB.askUnvisitedRooms(t));
		KB.makeActionSentence(new Forward(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST)), t); // Move agent to [2,1]		
		
		t++;
		step(KB, new AgentPercept(true, false, false, false, false), t); // NOTE: Wumpus in [2,2] so have stench
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,2)); add(new Room(2,2));}}, KB.askUnvisitedRooms(t));
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testAskPossibleWumpusRooms() {
		WumpusKnowledgeBase<WWAction> KB;
		int t = 0;
		
		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(false, false, false, false, false), t);		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(2,2));}}, KB.askPossibleWumpusRooms(t));	
		
		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(true, false, false, false, false), t); 		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,2)); add(new Room(2, 1));}}, KB.askPossibleWumpusRooms(t));		

		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 3, new PLParserWrapper());
		step(KB, new AgentPercept(false, false, false, false, false), t);		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,3)); add(new Room(2,2)); add(new Room(2,3)); add(new Room(3,1)); add(new Room(3,2)); add(new Room(3,3));}}, KB.askPossibleWumpusRooms(t));
		KB.makeActionSentence(new Forward(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST)), t); // Move agent to [2,1]		
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testAskNotUnsafeRooms() {
		WumpusKnowledgeBase<WWAction> KB;
		int t = 0;
		
		KB = new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(false, false, false, false, false), t);		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1)); add(new Room(1,2)); add(new Room(2,1));}}, KB.askNotUnsafeRooms(t));	
		
		KB =  new WumpusKnowledgeBase<WWAction>(dpll, 2, new PLParserWrapper());
		step(KB, new AgentPercept(true, false, false, false, false), t); 		
		Assert.assertEquals(new HashSet<Room>() {{add(new Room(1,1)); add(new Room(1,2)); add(new Room(2, 1)); add(new Room(2,2));}}, KB.askNotUnsafeRooms(t));		
	}
	
	@Test
	public void testExampleInSection7_2_described_pg268_AIMA3e() {
		// Make smaller in order to reduce the inference time required, this still covers all the relevant rooms for the example
		WumpusKnowledgeBase<WWAction> KB = new WumpusKnowledgeBase<WWAction>(dpll, 3, new PLParserWrapper()); 
		int t = 0;
		// 0
		step(KB, new AgentPercept(false, false, false, false, false), t);
		KB.makeActionSentence(new Forward(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST)), t);
		
		t++; // 1
		step(KB, new AgentPercept(false, true, false, false, false), t);
		KB.makeActionSentence(new TurnRight(AgentPosition.Orientation.FACING_EAST), t);
		
		t++; // 2
		step(KB, new AgentPercept(false, true, false, false, false), t);
		KB.makeActionSentence(new TurnRight(AgentPosition.Orientation.FACING_SOUTH), t);
		
		t++; // 3
		step(KB, new AgentPercept(false, true, false, false, false), t);
		KB.makeActionSentence(new Forward(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_WEST)), t);
	
		t++; // 4
		step(KB, new AgentPercept(false, false, false, false, false), t);
		KB.makeActionSentence(new TurnRight(AgentPosition.Orientation.FACING_WEST), t);
		
		t++; // 5
		step(KB, new AgentPercept(false, false, false, false, false), t);
		KB.makeActionSentence(new Forward(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_NORTH)), t);
		
		t++; // 6
		step(KB, new AgentPercept(true, false, false, false, false), t);
		
		Assert.assertTrue(KB.ask(KB.newSymbol(WumpusKnowledgeBase.LOCATION, t, 1, 2)));
		Assert.assertTrue(KB.ask(KB.newSymbol(WumpusKnowledgeBase.WUMPUS, 1, 3)));
		Assert.assertTrue(KB.ask(KB.newSymbol(WumpusKnowledgeBase.PIT, 3, 1)));
		Assert.assertTrue(KB.ask(KB.newSymbol(WumpusKnowledgeBase.OK_TO_MOVE_INTO, t, 2, 2)));
	}
	
	private void step(WumpusKnowledgeBase<WWAction> KB, AgentPercept percept, int t) {
		KB.tellTemporalPhysicsSentences(t);
		KB.makePerceptSentence(percept, t);
	}	
}