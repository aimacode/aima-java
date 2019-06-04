package aima.test.core.unit.agent.impl.aprog;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleAgent;
import aima.core.agent.impl.DynamicAction;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.aprog.TableDrivenAgentProgram;
import aima.test.core.unit.agent.impl.MockAgent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class TableDrivenAgentProgramTest {

	private static final Action ACTION_1 = new DynamicAction("action1");
	private static final Action ACTION_2 = new DynamicAction("action2");
	private static final Action ACTION_3 = new DynamicAction("action3");

	private SimpleAgent<DynamicPercept, Action> agent;

	@Before
	public void setUp() {
		Map<List<DynamicPercept>, Action> perceptSequenceActions = new HashMap<>();
		perceptSequenceActions.put(createPerceptSequence(new DynamicPercept(
				"key1", "value1")), ACTION_1);
		perceptSequenceActions.put(
				createPerceptSequence(new DynamicPercept("key1", "value1"),
						new DynamicPercept("key1", "value2")), ACTION_2);
		perceptSequenceActions.put(
				createPerceptSequence(new DynamicPercept("key1", "value1"),
						new DynamicPercept("key1", "value2"),
						new DynamicPercept("key1", "value3")), ACTION_3);

		agent = new MockAgent<DynamicPercept, Action>(new TableDrivenAgentProgram<>(
				perceptSequenceActions));
	}

	@Test
	public void testExistingSequences() {
		Assert.assertEquals(Optional.of(ACTION_1),
				agent.act(new DynamicPercept("key1", "value1")));
		Assert.assertEquals(Optional.of(ACTION_2),
				agent.act(new DynamicPercept("key1", "value2")));
		Assert.assertEquals(Optional.of(ACTION_3),
				agent.act(new DynamicPercept("key1", "value3")));
	}

	@Test
	public void testNonExistingSequence() {
		Assert.assertEquals(Optional.of(ACTION_1),
				agent.act(new DynamicPercept("key1", "value1")));
		Assert.assertEquals(Optional.empty(),
				agent.act(new DynamicPercept("key1", "value3")));
	}

	private static List<DynamicPercept> createPerceptSequence(DynamicPercept... percepts) {
		return Arrays.asList(percepts);
	}
}
