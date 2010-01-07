package aima.gui.applications.vacuum;

import aima.gui.framework.AgentAppFrame;

/**
 * Adds some selectors to the base class and adjusts its size.
 */
public class VacuumFrame extends AgentAppFrame {
	public static String ENV_SEL = "EnvSelection";
	public static String AGENT_SEL = "AgentSelection";

	public VacuumFrame() {
		setEnvView(new VacuumView());
		setSelectors(new String[] { ENV_SEL, AGENT_SEL }, new String[] {
				"Select Environment", "Select Agent" });
		setSelectorItems(ENV_SEL, new String[] { "A/B Environment" }, 0);
		setSelectorItems(AGENT_SEL, new String[] {
				"TableDrivenVacuumAgent",
				"ReflexVacuumAgent",
				"SimpleReflexVacuumAgent",
				"ModelBasedReflexVacuumAgent" },
				0);
		setTitle("Vacuum Agent Application");
		setSize(800, 400);
	}
}