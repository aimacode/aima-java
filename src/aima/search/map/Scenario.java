package aima.search.map;

import aima.search.map.MapEnvironment;

/**
 * A scenario specifies an environment, the agent's knowledge about the
 * environment, and the agents initial location. It can be used to specify
 * settings for routing and touring problems.
 * @author R. Lunde
 */
public class Scenario {
	/**
	 * A map-based environment. Note that the contained map must be of
	 * type {@link MapWithSLD}.
	 */
	private MapEnvironment env;
	/** A map reflecting the knowledge of the agent about the environment. */
	private MapWithSLD agentMap;
	/** Initial location of the agent. */
	private String initAgentLoc;
	
	/**
	 * Creates a scenario.
	 * @param env       a map-based environment. Note that the contained map must
	 *                  be of type {@link MapWithSLD}
	 * @param agentMap  a map reflecting the knowledge of the agent about the environment
	 * @param agentLoc  initial location of the agent
	 */
	public Scenario(MapEnvironment env, MapWithSLD agentMap, String agentLoc) {
		this.agentMap = agentMap;
		this.env = env;
		this.initAgentLoc = agentLoc;
	}
	
	public MapEnvironment getEnv() { return env; }
	public MapWithSLD getEnvMap() { return  (MapWithSLD) env.getMap(); }
	public MapWithSLD getAgentMap() { return  agentMap; }
	public String getInitAgentLocation() { return initAgentLoc; }
}
