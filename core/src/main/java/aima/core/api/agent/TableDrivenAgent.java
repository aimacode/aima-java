package aima.core.api.agent;

import java.util.List;
import java.util.Map;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function TABLE-DRIVEN-AGENT(percept) returns an action
 *   persistent: percepts, a sequence, initially empty
 *               table, a table of actions, indexed by percept sequences, initially fully specified
 *
 *   append percept to end of percepts
 *   action &lt;- LOOKUP(percepts, table)
 *   return action
 * </pre>
 *
 * Figure ?? The TABLE-DRIVEN-AGENT program is invoked for each new percept and
 * returns an action each time. It retains the complete percept sequence in
 * memory.
 *
 * @author Ciaran O'Reilly
 */
public interface TableDrivenAgent<A, P> extends Agent<A, P> {
    // persistent: percepts, a sequence, initially empty
    //             table, a table of actions, indexed by percept sequences, initially fully specified
    List<P>         percepts();
    Map<List<P>, A> table();

    // function TABLE-DRIVEN-AGENT(percept) returns an action
    @Override
    default A perceive(P percept) {
        // append percept to end of percepts
        percepts().add(percept);
        // action <- LOOKUP(percepts, table)
        A action = lookup(percepts(), table());
        // return action
        return action;
    }

    default A lookup(List<P> percepts, Map<List<P>, A> table) {
        A action = table.get(percepts);
        return action;
    }
}
