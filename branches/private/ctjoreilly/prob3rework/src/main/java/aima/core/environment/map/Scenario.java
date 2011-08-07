package aima.core.environment.map;

/**
 * A scenario specifies an environment, the agent's knowledge about the
 * environment, and the agents initial location. It can be used to specify
 * settings for route planning agent applications.
 * 
 * @author Ruediger Lunde
 */
public class Scenario {
	/**
	 * A map-based environment. Note that the contained map must be of type
	 * {@link ExtendableMap}.
	 */
	private final MapEnvironment env;
	/** A map reflecting the knowledge of the agent about the environment. */
	private final Map agentMap;
	/** Initial location of the agent. */
	private final String initAgentLoc;

	/**
	 * Creates a scenario.
	 * 
	 * @param env
	 *            a map-based environment. Note that the contained map must be
	 *            of type {@link ExtendableMap}
	 * @param agentMap
	 *            a map reflecting the knowledge of the agent about the
	 *            environment
	 * @param agentLoc
	 *            initial location of the agent
	 */
	public Scenario(MapEnvironment env, Map agentMap, String agentLoc) {
		this.agentMap = agentMap;
		this.env = env;
		this.initAgentLoc = agentLoc;
	}

	public MapEnvironment getEnv() {
		return env;
	}

	public Map getEnvMap() {
		return env.getMap();
	}

	public Map getAgentMap() {
		return agentMap;
	}

	public String getInitAgentLocation() {
		return initAgentLoc;
	}
}
