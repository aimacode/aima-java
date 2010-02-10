package aima.test.core.unit.agent.impl.aprog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicAction;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.NoOpAction;
import aima.core.agent.impl.aprog.TableDrivenAgentProgram;
import aima.test.core.unit.agent.impl.MockAgent;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class TableDrivenAgentProgramTest {

	private static final Action ACTION_1 = new DynamicAction("action1");
	private static final Action ACTION_2 = new DynamicAction("action2");
	private static final Action ACTION_3 = new DynamicAction("action3");

	private AbstractAgent agent;

	@Before
	public void setUp() {
		Map<List<Percept>, Action> perceptSequenceActions = new HashMap<List<Percept>, Action>();
		perceptSequenceActions.put(createPerceptSequence(new DynamicPercept(
				"key1", "value1")), ACTION_1);
		perceptSequenceActions.put(createPerceptSequence(new DynamicPercept(
				"key1", "value1"), new DynamicPercept("key1", "value2")),
				ACTION_2);
		perceptSequenceActions.put(createPerceptSequence(new DynamicPercept(
				"key1", "value1"), new DynamicPercept("key1", "value2"),
				new DynamicPercept("key1", "value3")), ACTION_3);

		agent = new MockAgent(new TableDrivenAgentProgram(
				perceptSequenceActions));
	}

	@Test
	public void testExistingSequences() {
		Assert.assertEquals(ACTION_1, agent.execute(new DynamicPercept("key1",
				"value1")));
		Assert.assertEquals(ACTION_2, agent.execute(new DynamicPercept("key1",
				"value2")));
		Assert.assertEquals(ACTION_3, agent.execute(new DynamicPercept("key1",
				"value3")));
	}

	@Test
	public void testNonExistingSequence() {
		Assert.assertEquals(ACTION_1, agent.execute(new DynamicPercept("key1",
				"value1")));
		Assert.assertEquals(NoOpAction.NO_OP, agent.execute(new DynamicPercept(
				"key1", "value3")));
	}

	private static List<Percept> createPerceptSequence(Percept... percepts) {
		List<Percept> perceptSequence = new ArrayList<Percept>();

		for (Percept p : percepts) {
			perceptSequence.add(p);
		}

		return perceptSequence;
	}
}
