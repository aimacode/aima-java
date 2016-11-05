package aima.gui.swing.applications.agent;

import aima.gui.swing.framework.AgentAppFrame;

/**
 * Adds some selectors to the base class and adjusts its size.
 * 
 * @author Ruediger Lunde
 */
public class VacuumFrame extends AgentAppFrame {
	private static final long serialVersionUID = 1L;
	public static String ENV_SEL = "EnvSelection";
	public static String AGENT_SEL = "AgentSelection";

	public VacuumFrame() {
		setTitle("Vacuum Agent Application");
		setSelectors(new String[] { ENV_SEL, AGENT_SEL }, new String[] {
				"Select Environment", "Select Agent" });
		setSelectorItems(ENV_SEL, new String[] { "A/B Deterministic Environment",
				"A/B Non-Deterministic Environment"}, 
				0);
		setSelectorItems(AGENT_SEL, new String[] {
				"TableDrivenVacuumAgent",
				"ReflexVacuumAgent",
				"SimpleReflexVacuumAgent",
				"ModelBasedReflexVacuumAgent",
				"NondeterministicVacuumAgent" },
				0);
		setEnvView(new VacuumView());
		setSize(800, 400);
	}
}