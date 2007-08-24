package aima.test.tvenvironmenttest;

import junit.framework.TestCase;

import aima.basic.BasicEnvironmentView;
import aima.basic.vaccum.TableDrivenVaccumAgent;
import aima.basic.vaccum.TrivialVaccumEnvironment;

/**
 * @author Ciaran O'Reilly
 * 
 */

public class TableDrivenVaccumAgentTest extends TestCase {
	private TableDrivenVaccumAgent agent;

	private StringBuffer envChanges;

	@Override
	public void setUp() {
		agent = new TableDrivenVaccumAgent();
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

		tve.stepUntilNoOp();

		assertEquals("RightLeftRightNoOP", envChanges.toString());
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

		tve.stepUntilNoOp();

		assertEquals("RightSuckLeftNoOP", envChanges.toString());
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

		tve.stepUntilNoOp();

		assertEquals("SuckRightLeftNoOP", envChanges.toString());
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

		tve.stepUntilNoOp();

		assertEquals("SuckRightSuckNoOP", envChanges.toString());
	}
}
