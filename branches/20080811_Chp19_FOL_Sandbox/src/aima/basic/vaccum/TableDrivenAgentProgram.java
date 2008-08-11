package aima.basic.vaccum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aima.basic.AgentProgram;
import aima.basic.Percept;
import aima.basic.PerceptSequence;
import aima.util.Table;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.7, page 45.
 * <code>
 * function TABLE-DRIVEN-AGENT(percept) returns an action
 *   static: percepts, a sequence, initially empty
 *           table, a table of actions, indexed by percept sequences, initially fully specified
 *           
 *   append percept to end of percepts
 *   action <- LOOKUP(percepts, table)
 *   
 *   return action
 * </code>
 * Figure 2.7 The TABLE-DRIVEN-AGENT program is invoked for each new percept and 
 * returns an action each time. It keeps track of the percept sequence using its own private data
 * structure.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class TableDrivenAgentProgram extends AgentProgram {
	// Used to define No Operations/Action is to be performed.
	public static final String NO_OP = "NoOP";

	private PerceptSequence percepts = new PerceptSequence();

	private Table<PerceptSequence, String, String> table;

	private static final String ACTION = "action";

	// static: percepts, a sequence, initially empty
	// table, a table of actions, indexed by percept sequences, initially fully
	// specified
	public TableDrivenAgentProgram(
			Map<PerceptSequence, String> perceptSequenceActions) {
		List<PerceptSequence> rowHeaders = new ArrayList<PerceptSequence>(
				perceptSequenceActions.keySet());

		List<String> colHeaders = new ArrayList<String>();
		colHeaders.add(ACTION);

		table = new Table<PerceptSequence, String, String>(rowHeaders,
				colHeaders);

		for (PerceptSequence row : rowHeaders) {
			table.set(row, ACTION, perceptSequenceActions.get(row));
		}
	}

	// function TABLE-DRIVEN-AGENT(percept) returns an action
	@Override
	public String execute(Percept percept) {
		// append percept to end of percepts
		percepts.append(percept);

		// action <- LOOKUP(percepts, table)
		// return action
		return lookupCurrentAction();
	}

	//
	// PRIVATE METHODS
	//
	private String lookupCurrentAction() {
		String action;

		action = table.get(percepts, ACTION);
		if (null == action) {
			action = NO_OP;
		}

		return action;
	}
}
