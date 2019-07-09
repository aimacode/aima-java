package aima.core.agent.impl.aprog;

import aima.core.agent.AgentProgram;
import aima.core.util.datastructure.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 2.7, page 47.<br>
 * <br>
 * 
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
 * 
 */
public class TableDrivenAgentProgram<P, A> implements AgentProgram<P, A> {
	private List<P> percepts = new ArrayList<>();

	private Table<List<P>, String, A> table;

	private static final String ACTION = "action";

	// persistent: percepts, a sequence, initially empty
	// table, a table of actions, indexed by percept sequences, initially fully
	// specified
	/**
	 * Constructs a TableDrivenAgentProgram with a table of actions, indexed by
	 * percept sequences.
	 * 
	 * @param perceptsToActionMap
	 *            a table of actions, indexed by percept sequences
	 */
	public TableDrivenAgentProgram(Map<List<P>, A> perceptsToActionMap) {

		List<List<P>> rowHeaders = new ArrayList<>(perceptsToActionMap.keySet());
		List<String> colHeaders = new ArrayList<>();
		colHeaders.add(ACTION);

		table = new Table<>(rowHeaders, colHeaders);
		rowHeaders.forEach(row -> table.set(row, ACTION, perceptsToActionMap.get(row)));
	}

	// function TABLE-DRIVEN-AGENT(percept) returns an action
	public Optional<A> apply(P percept) {
		// append percept to end of percepts
		percepts.add(percept);

		// action <- LOOKUP(percepts, table)
		// return action
		return Optional.ofNullable(lookupCurrentAction());
	}

	private A lookupCurrentAction() {
		return table.get(percepts, ACTION);
	}
}
