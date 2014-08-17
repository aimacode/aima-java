package aima.test.core.unit.environment.wumpusworld;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.wumpusworld.AgentPercept;
import aima.core.environment.wumpusworld.AgentPosition;
import aima.core.environment.wumpusworld.WumpusKnowledgeBase;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.TurnLeft;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class WumpusKnowledgeBaseTest {
	private WumpusKnowledgeBase KB = null;
	
	@Before
	public void setUp() {
		KB = new WumpusKnowledgeBase(4);
	}
	
	@Test
	public void testAskCurrentPosition() {	
		int t = 0;
		AgentPosition current;
		step(new AgentPercept(false, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_EAST), current);
		KB.makeActionSentence(new Forward(current), t);
		
		t++;
		step(new AgentPercept(false, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_EAST), current);
		KB.makeActionSentence(new TurnLeft(current.getOrientation()), t);
		
		t++;
		step(new AgentPercept(false, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_NORTH), current);
		KB.makeActionSentence(new TurnLeft(current.getOrientation()), t);
		
		t++;
		step(new AgentPercept(false, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(2, 1, AgentPosition.Orientation.FACING_WEST), current);
		KB.makeActionSentence(new Forward(current), t);
		
		t++;
		step(new AgentPercept(false, false, false, false, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_WEST), current);
		KB.makeActionSentence(new Forward(current), t);
		
		t++;
		step(new AgentPercept(false, false, false, true, false), t);	
		current = KB.askCurrentPosition(t);
		Assert.assertEquals(new AgentPosition(1, 1, AgentPosition.Orientation.FACING_WEST), current);
		KB.makeActionSentence(new Forward(current), t);
	}
	
	@Test
	public void testAskGlitter() {
		step(new AgentPercept(false, false, false, false, false), 0);
		Assert.assertFalse(KB.askGlitter(0));
		step(new AgentPercept(false, false, false, false, false), 1);
		Assert.assertFalse(KB.askGlitter(1));
		step(new AgentPercept(false, false, true, false, false), 2);
		Assert.assertTrue(KB.askGlitter(2));
		step(new AgentPercept(false, false, false, false, false), 3);
		Assert.assertFalse(KB.askGlitter(3));
	}
	
	private void step(AgentPercept percept, int t) {
		KB.tellTemporalPhysicsSentences(t);
		KB.makePerceptSentence(percept, t);
// TODO - remove		
//System.out.println("\n\n*** KB at time step "+t+"=\n"+KB.toString());	
	}
	
}
