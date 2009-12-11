package aima.test.tvenvironmenttest;

import junit.framework.TestCase;

import aima.basic.BasicEnvironmentView;
import aima.basic.vaccum.SimpleReflexVaccumAgent;
import aima.basic.vaccum.TrivialVaccumEnvironment;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class SimpleReflexVaccumAgentTest extends TestCase {
	private SimpleReflexVaccumAgent agent;

	private StringBuffer envChanges;

	@Override
	public void setUp() {
		agent = new SimpleReflexVaccumAgent();
		envChanges = new StringBuffer();
	}

	public void testCleanClean() {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Clean",
				"Clean");
		tve.addAgent(agent, "A");

		tve.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command);
			}
		});

		tve.step(8);

		assertEquals("RightLeftRightLeftRightLeftRightLeft", envChanges
				.toString());
	}

	public void testCleanDirty() {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Clean",
				"Dirty");
		tve.addAgent(agent, "A");

		tve.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command);
			}
		});

		tve.step(8);

		assertEquals("RightSuckLeftRightLeftRightLeftRight", envChanges
				.toString());
	}

	public void testDirtyClean() {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Dirty",
				"Clean");
		tve.addAgent(agent, "A");

		tve.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command);
			}
		});

		tve.step(8);

		assertEquals("SuckRightLeftRightLeftRightLeftRight", envChanges
				.toString());
	}

	public void testDirtyDirty() {
		TrivialVaccumEnvironment tve = new TrivialVaccumEnvironment("Dirty",
				"Dirty");
		tve.addAgent(agent, "A");

		tve.registerView(new BasicEnvironmentView() {
			@Override
			public void envChanged(String command) {
				envChanges.append(command);
			}
		});

		tve.step(8);

		assertEquals("SuckRightSuckLeftRightLeftRightLeft", envChanges
				.toString());
	}
}
