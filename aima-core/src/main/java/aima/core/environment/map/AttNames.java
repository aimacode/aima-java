package aima.core.environment.map;

/**
 * The AIMA framework uses dynamic attributes to make implementations of agents
 * and environments completely independent of each other. The disadvantage of
 * this concept is, that it's error-prone. This set of constants is designed to
 * make information exchange more reliable for map agents.
 * 
 * @author Ruediger Lunde
 */
public class AttNames {
	/**
	 * Name of a dynamic attribute in agent state, which contains the current
	 * location of the agent. Expected value type: String.
	 */
	public static final String AGENT_LOCATION = "location";
	/**
	 * Name of a dynamic attribute in percept, which tells the agent where it is.
	 * Expected value type: String.
	 */
	public static final String PERCEPT_IN = "in";
}
