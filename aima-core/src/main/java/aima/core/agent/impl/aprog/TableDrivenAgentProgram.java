package aima.core.agent.impl.aprog;

import aima.core.agent.AgentProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.7, page 47.
 * <br><br>
 * <pre>
 * function TABLE-DRIVEN-AGENT(percept) returns an action
 *   persistent: percepts, a sequence, initially empty
 *               table, a table of actions, indexed by percept sequences, initially fully specified
 *           
 *   append percept to end of percepts
 *   action <- LOOKUP(percepts, table)
 *   return action
 * </pre>
 * 
 * Figure 2.7 The TABLE-DRIVEN-AGENT program is invoked for each new percept and
 * returns an action each time. It retains the complete percept sequence in
 * memory.
 *
 * @param <P> Type which is used to represent percepts
 * @param <A> Type which is used to represent actions
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class TableDrivenAgentProgram<P, A> implements AgentProgram<P, A> {
	private List<P> percepts = new ArrayList<>();
	private Map<List<P>, A> table;

	/**
	 * Constructs a TableDrivenAgentProgram with a table of actions, indexed by percept sequences.
	 * @param perceptsToActionMap
	 *            a table of actions, indexed by percept sequences
	 */
	public TableDrivenAgentProgram(Map<List<P>, A> perceptsToActionMap) {
		table = perceptsToActionMap;
	}

	/// function TABLE-DRIVEN-AGENT(percept) returns an action
	public Optional<A> apply(P percept) {
		percepts.add(percept);
		return Optional.ofNullable(table.get(percepts));
	}
}
