package aima.core.agent;

import aima.core.api.agent.TableDrivenAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ciaran O'Reilly
 */
public class BasicTableDrivenAgent<A, P> implements TableDrivenAgent<A, P> {
    private List<P>         percepts = new ArrayList<>();
    private Map<List<P>, A> table    = new HashMap<>();

    public BasicTableDrivenAgent(Map<List<P>, A> table) {
        this.table.putAll(table);
    }

    @Override
    public List<P> percepts() {
        return percepts;
    }

    @Override
    public Map<List<P>, A> table() {
        return table;
    }
}
