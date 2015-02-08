package aima.core.api.agent;

import java.util.List;
import java.util.Map;

/**
 * Artificial Intelligence A Modern Approach (4thEdition): Figure ??, page ??.<br>
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
 * Figure ?? The TABLE-DRIVEN-AGENT program is invoked for each new percept and
 * returns an action each time. It retains the complete percept sequence in
 * memory.
 *
 * @author Ciaran O'Reilly
 */
public interface TableDrivenAgent<P extends Percept> extends Agent<P> {
    // persistent: percepts, a sequence, initially empty
    //             table, a table of actions, indexed by percept sequences, initially fully specified
    List<Percept>              percepts();
    Map<List<Percept>, Action> table();

    // function TABLE-DRIVEN-AGENT(percept) returns an action
    @Override
    default Action perceive(P percept) {
        // append percept to end of percepts
        percepts().add(percept);
        // action <- LOOKUP(percepts, table)
        Action action = lookup(percepts(), table());
        // return action
        return action;
    }

    default Action lookup(List<Percept> percepts, Map<List<Percept>, Action> table) {
        Action action = table.get(percepts);
        if (action == null) {
            action = Action.NoOp;
        }
        return action;
    }
}
