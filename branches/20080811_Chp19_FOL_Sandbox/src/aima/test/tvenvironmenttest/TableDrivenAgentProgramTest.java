package aima.test.tvenvironmenttest;

import java.util.Hashtable;
import java.util.Map;

import junit.framework.TestCase;
import aima.basic.Agent;
import aima.basic.AgentProgram;
import aima.basic.Percept;
import aima.basic.PerceptSequence;
import aima.basic.vaccum.TableDrivenAgentProgram;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class TableDrivenAgentProgramTest extends TestCase {

	private Agent agent;

	@Override
	public void setUp() {
		Map<PerceptSequence, String> perceptSequenceActions = new Hashtable<PerceptSequence, String>();
		perceptSequenceActions.put(new PerceptSequence(new Percept("key1",
				"value1")), "action1");
		perceptSequenceActions.put(new PerceptSequence(new Percept("key1",
				"value1"), new Percept("key1", "value2")), "action2");
		perceptSequenceActions.put(new PerceptSequence(new Percept("key1",
				"value1"), new Percept("key1", "value2"), new Percept("key1",
				"value3")), "action3");

		agent = new TestAgent(new TableDrivenAgentProgram(
				perceptSequenceActions));
	}

	public void testExistingSequences() {
		assertEquals("action1", agent.execute(new Percept("key1", "value1")));
		assertEquals("action2", agent.execute(new Percept("key1", "value2")));
		assertEquals("action3", agent.execute(new Percept("key1", "value3")));
	}

	public void testNonExistingSequence() {
		assertEquals("action1", agent.execute(new Percept("key1", "value1")));
		assertEquals("NoOP", agent.execute(new Percept("key1", "value3")));
	}
}

class TestAgent extends Agent {
	public TestAgent(AgentProgram agent) {
		super(agent);
	}
}
