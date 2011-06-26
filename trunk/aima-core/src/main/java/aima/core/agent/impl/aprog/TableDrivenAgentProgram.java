package aima.core.agent.impl.aprog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.core.agent.Action;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;
import aima.core.agent.impl.NoOpAction;
import aima.core.util.datastructure.Table;

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
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * 
 */
public class TableDrivenAgentProgram implements AgentProgram {
	private List<Percept> percepts = new ArrayList<Percept>();

	private Table<List<Percept>, String, Action> table;

	private static final String ACTION = "action";

	// persistent: percepts, a sequence, initially empty
	// table, a table of actions, indexed by percept sequences, initially fully
	// specified
	/**
	 * Constructs a TableDrivenAgentProgram with a table of actions, indexed by
	 * percept sequences.
	 * 
	 * @param perceptSequenceActions
	 *            a table of actions, indexed by percept sequences
	 */
	public TableDrivenAgentProgram(
			Map<List<Percept>, Action> perceptSequenceActions) {

		List<List<Percept>> rowHeaders = new ArrayList<List<Percept>>(
				perceptSequenceActions.keySet());

		List<String> colHeaders = new ArrayList<String>();
		colHeaders.add(ACTION);

		table = new Table<List<Percept>, String, Action>(rowHeaders, colHeaders);

		for (List<Percept> row : rowHeaders) {
			table.set(row, ACTION, perceptSequenceActions.get(row));
		}
	}

	//
	// START-AgentProgram

	// function TABLE-DRIVEN-AGENT(percept) returns an action
	public Action execute(Percept percept) {
		// append percept to end of percepts
		percepts.add(percept);

		// action <- LOOKUP(percepts, table)
		// return action
		return lookupCurrentAction();
	}

	// END-AgentProgram
	//

	//
	// PRIVATE METHODS
	//
	private Action lookupCurrentAction() {
		Action action = null;

		action = table.get(percepts, ACTION);
		if (null == action) {
			action = NoOpAction.NO_OP;
		}

		return action;
	}
}
