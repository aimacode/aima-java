package aima.test.unit.agent;


import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import aima.core.agent.api.Agent;
import aima.core.agent.api.Rule;
import aima.core.agent.basic.TableDrivenAgent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Ciaran O'Reilly
 * @author Haris Riaz
 * @param <P>
 * @param <A>
 */
public class TableDrivenAgentTest<A, P>
{
	
		// Actions defined as strings
		private final String ACTION_1 = "action1";
		private final String ACTION_2 = "action2";
		private final String ACTION_3 = "action3";
		public static final String NO_OP = "NO_OPERATION";
		private Map<List<String>, String> perceptSequenceActions; // a parameterized table of actions, indexed by percept sequences defined as type
																  // String, initially fully specified
		private List<String> percept = new ArrayList<>(); // a parameterized sequence, initially empty of type String 
    	private TableDrivenAgent <String, String> t1;     // TableDrivenAgent object parameterized as a string 
		
    	@Before
		public void setUp()
		{
			perceptSequenceActions = new HashMap<List<String>,String>();
			percept = new ArrayList<>();
			percept.add("Percept1");
			perceptSequenceActions.put(percept, ACTION_1);
			percept.add("Percept2");
			perceptSequenceActions.put(percept, ACTION_2);
			percept.add("Percept3");
			perceptSequenceActions.put(percept, ACTION_3);
			
	    	t1 = new TableDrivenAgent<String,String>(perceptSequenceActions); 
	    	
		}
		
    @Ignore("TODO")
    @Test
    public void testExistingSequences() { 
    	
    	Assert.fail("TODO");
    	Assert.assertEquals(ACTION_1,
				t1.perceive((String) percept.get(0)));
		Assert.assertEquals(ACTION_2,
				t1.perceive((String) percept.get(1)));
		Assert.assertEquals(ACTION_3,
				t1.perceive((String) percept.get(2))); 
    }
    
    
    /*@Test
    public void testNonExistingSequence() {
    	
        Assert.assertEquals(ACTION_1,
    			t1.perceive((String) percept.get(0)));
        Assert.assertNotEquals(NO_OP,
				t1.perceive((String) percept.get(2)));
    }*/
	
}

