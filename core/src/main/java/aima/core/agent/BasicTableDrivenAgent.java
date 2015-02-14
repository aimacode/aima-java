package aima.core.agent;

import aima.core.api.agent.Action;
import aima.core.api.agent.TableDrivenAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ciaran O'Reilly
 */
public class BasicTableDrivenAgent<P> implements TableDrivenAgent<P> {
    private List<P>              percepts = new ArrayList<>();
    private Map<List<P>, Action> table    = new HashMap<>();

    public BasicTableDrivenAgent(Map<List<P>, Action> table) {
        this.table.putAll(table);
    }

    @Override
    public List<P> percepts() {
        return percepts;
    }

    @Override
    public Map<List<P>, Action> table() {
        return table;
    }
}
